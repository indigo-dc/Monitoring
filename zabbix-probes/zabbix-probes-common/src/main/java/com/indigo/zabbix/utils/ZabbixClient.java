package com.indigo.zabbix.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.indigo.zabbix.utils.beans.DocDataType;
import com.indigo.zabbix.utils.beans.ZabbixWrapperGroupCreationRequest;
import com.indigo.zabbix.utils.beans.ZabbixWrapperGroupsResult;
import com.indigo.zabbix.utils.beans.ZabbixWrapperResponse;
import feign.Response;
import io.github.hengyunabc.zabbix.sender.DataObject;
import io.github.hengyunabc.zabbix.sender.SenderResult;
import io.github.hengyunabc.zabbix.sender.ZabbixSender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Created by jose on 22/09/16. */
public class ZabbixClient {

  public static final Integer ZABBIX_DEFAULT_PORT = 10051;

  private String zabbixCategory;
  private String zabbixTemplate;

  private static final Log logger = LogFactory.getLog(ZabbixClient.class);

  private ZabbixWrapperClient wrapperClient;
  private ZabbixSender sender;

  /**
   * Constructor used for testing.
   *
   * @param wrapperClient mock wrapper client.
   * @param sender mock wrapper sender.
   */
  public ZabbixClient(
      String category, String template, ZabbixWrapperClient wrapperClient, ZabbixSender sender) {
    this.zabbixCategory = category;
    this.zabbixTemplate = template;
    this.wrapperClient = wrapperClient;
    this.sender = sender;
  }

  /** Default constructor that will read the information from the configuration properties. */
  public ZabbixClient(String category, String template) {
    this(
        PropertiesManager.getProperty(ProbesTags.ZABBIX_WRAPPER_ENDPOINT),
        category,
        template,
        PropertiesManager.getProperty(ProbesTags.ZABBIX_HOST),
        new Integer(
            PropertiesManager.getProperty(ProbesTags.ZABBIX_PORT, ZABBIX_DEFAULT_PORT.toString())));
  }

  /** Default constructor. */
  public ZabbixClient(
      String wrapperEndpoint,
      String category,
      String template,
      String zabbixHost,
      Integer zabbixPort) {

    wrapperClient = ProbeClientFactory.getZabbixWrapperClient(wrapperEndpoint);

    Integer port = (zabbixPort != null) ? zabbixPort : ZABBIX_DEFAULT_PORT;

    sender = new ZabbixSender(zabbixHost, port);

    zabbixCategory = category;
    zabbixTemplate = template;
  }

  /**
   * Ensures that a group exists in the Zabbix wrapper. Otherwise it registers it.
   *
   * @param group The group name to search or register.
   * @return The group information.
   */
  public ZabbixWrapperGroupsResult.Group ensureGroupRegistration(String group) {

    Response response = wrapperClient.getGroup(group);
    switch (response.status()) {
      case 200:
        Gson serializer = new Gson();
        Type responseType =
            new TypeToken<ZabbixWrapperResponse<ZabbixWrapperGroupsResult>>() {}.getType();
        try {
          ZabbixWrapperResponse<ZabbixWrapperGroupsResult> groupInfo =
              serializer.fromJson(response.body().asReader(), responseType);
          ZabbixWrapperGroupsResult groups = groupInfo.getResult();
          Optional<ZabbixWrapperGroupsResult.Group> result =
              groups.getGroups().stream().filter(g -> group.equals(g.getGroupName())).findFirst();
          if (result.isPresent()) {
            return result.get();
          } else {
            logger.error(
                "Can't find group " + group + "in response although it's supposed to exist");
          }
        } catch (IOException e) {
          logger.error("Error serializing group " + group + "information", e);
        }
        break;
      case 404:
        response = wrapperClient.registerGroup(new ZabbixWrapperGroupCreationRequest(group));
        if (response.status() == 201) {
          return new ZabbixWrapperGroupsResult.Group(group);
        } else {
          logger.error("Error creating group " + group + ": " + response.toString());
        }
        break;
      default:
        logger.error(
            "Unexpected code "
                + response.status()
                + " when getting information of group "
                + group
                + ": "
                + response.toString());
        break;
    }
    return null;
  }

  /**
   * Ensure that a host is registered and if not, register it.
   *
   * @param host The host name.
   * @return The registration status.
   */
  public boolean ensureRegistration(
      String host, String zabbixGroup, DocDataType.ServiceType serviceType, boolean register)
      throws Exception {

    ZabbixWrapperGroupsResult.Group groupInfo = ensureGroupRegistration(zabbixGroup);
    if (groupInfo != null) {
      boolean registered =
          groupInfo.getPaasMachines().stream()
              .filter(machine -> host.equals(machine.getMachineName()))
              .findFirst()
              .isPresent();
      if (registered) {
        return true;
      } else {
        Response registrationResult =
            wrapperClient.registerHost(
                host,
                zabbixGroup,
                new ZabbixHost(host, serviceType, zabbixCategory, zabbixGroup, zabbixTemplate));
        if (registrationResult.status() == 201) {
          return true;
        } else {
          logger.error(
              "Error registering host "
                  + host
                  + " in group "
                  + zabbixGroup
                  + ": "
                  + registrationResult.toString());
          return false;
        }
      }
    } else {
      logger.error(
          "Can't register host "
              + host
              + " in group "
              + zabbixGroup
              + " because group creation failed. ");
      return false;
    }
  }

  /**
   * Sends a series of metrics associated to a host.
   *
   * @param metrics The metrics to send.
   */
  public SenderResult sendMetrics(ZabbixMetrics metrics) {
    try {
      if (ensureRegistration(
          metrics.getHostName(), metrics.getHostGroup(), metrics.getServiceType(), true)) {
        long timeSecs = metrics.getTimestamp() / 1000;
        String zabbixHost = PropertiesManager.getProperty(ProbesTags.ZABBIX_HOST);
        if (zabbixHost != null) {

          List<DataObject> toSend =
              metrics.getMetrics().entrySet().stream()
                  .map(
                      entry -> {
                        DataObject dataObject = new DataObject();
                        dataObject.setHost(metrics.getHostName());
                        dataObject.setKey(entry.getKey());
                        dataObject.setValue(entry.getValue());
                        dataObject.setClock(timeSecs);

                        return dataObject;
                      })
                  .collect(Collectors.toList());

          try {
            SenderResult sendResult = sender.send(toSend);
            if (!sendResult.success()) {
              /*
               * Sometimes zabbix fails to process data the first time it's sent. Specially when the
               * host is first registered. The reason why? It's a mystery and given the huge amount
               * of feedback we have to debug the situation, let's just try to send it once again
               * before failing.
               */
              sendResult = sender.send(toSend);
              if (!sendResult.success()) {
                logger.error(
                    "Error sending values for host "
                        + metrics.getHostName()
                        + ": "
                        + "\nTotal: "
                        + sendResult.getTotal()
                        + "\nProcessed: "
                        + sendResult.getProcessed()
                        + "\nFailed: "
                        + sendResult.getFailed()
                        + ": "
                        + metrics.getMetrics().toString());
              } else {
                logger.info(
                    "Successfully sent "
                        + sendResult.getProcessed()
                        + " metrics for host "
                        + metrics.getHostName());
              }
            } else {
              logger.info(
                  "Successfully sent "
                      + sendResult.getProcessed()
                      + " metrics for host "
                      + metrics.getHostName());
            }
            return sendResult;

          } catch (IOException e) {
            logger.error("Error sending values for host " + metrics.getHostName(), e);
          }
        }
      }
    } catch (Exception exc) {
      logger.error("Unable to send metrics to Zabbix server because of: " + exc.getMessage(), exc);
    }
    return null;
  }
}
