package it.reply.monitoringpillar.wrapper.zabbix.paas;

import it.reply.monitoringpillar.adapter.zabbix.clientbuilder.ZabbixAdapterClientSetter;
import it.reply.monitoringpillar.adapter.zabbix.exception.ZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas.HostAffected;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas.TriggerShot;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas.WrappedIaasHealthByTrigger;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixItemResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostResponseV24;
import it.reply.monitoringpillar.utility.TimestampMonitoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class WrapperTriggerPaaSBean {

  @Inject
  private ZabbixAdapterClientSetter<?> zabAdapClientSetter;

  /**
   * Get triggers.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param hosts
   *          list of hosts
   * @param groupName
   *          name
   * @return wrapped trigger
   * @throws ZabbixException
   *           exep
   */
  public WrappedIaasHealthByTrigger getTriggerWrapper(String zone, String serverType,
      List<ZabbixMonitoredHostResponseV24> hosts, String groupName) throws ZabbixException {

    WrappedIaasHealthByTrigger wrappedIaasHealthByTrigger = new WrappedIaasHealthByTrigger();

    // Put into the map all the host found by cycling on triggers
    Map<String, HostAffected> hostAffectedMap = new HashMap<String, HostAffected>();

    // It retrieves all shuttered triggers
    for (ZabbixMonitoredHostResponseV24 host : hosts) {
      List<ZabbixItemResponse> triggers = zabAdapClientSetter.getTriggerWithProblemsService(zone,
          serverType, host.getHostid(), null, ZabbixMethods.TRIGGER.getzabbixMethod(), null);

      for (ZabbixItemResponse zabbixItemResponse : triggers) {

        // If the host is in the map just add it
        if (!hostAffectedMap.containsKey(host.getName())) {
          HostAffected hostAffected = new HostAffected();
          hostAffected.setHostName(host.getName());
          hostAffected.setTriggerShots(new ArrayList<TriggerShot>());
          hostAffectedMap.put(host.getName(), hostAffected);
        }
        // set the trigger
        TriggerShot triggerShot = new TriggerShot();
        triggerShot.setTriggerId(zabbixItemResponse.getTriggerid());
        triggerShot.setDescription(zabbixItemResponse.getDescription());
        triggerShot.setExpression(zabbixItemResponse.getExpression());
        if (zabbixItemResponse.getLastchange() != null) {
          String timeFromZabbix = TimestampMonitoring
              .decodUnixTime2Date(Long.parseLong(zabbixItemResponse.getLastchange()));

          /*
           * The next piece of commmented code is to be used whenever trigger shot not before than
           * 5mins ago are needed to be taken in consideration
           */
          // if (Long.valueOf(zabbixItemResponse.getLastchange()) >
          // (System.currentTimeMillis() - 5 * 60 * 1000)) {
          // triggerShot.setTime(timeFromZabbix);
          // }
          triggerShot.setTime(timeFromZabbix);
        } else {
          triggerShot.setTime("0");
        }
        // associate it to the host
        hostAffectedMap.get(host.getName()).getTriggerShots().add(triggerShot);
      }
      wrappedIaasHealthByTrigger.setHostGroup(groupName);
      wrappedIaasHealthByTrigger
          .setHostAffecteds(new ArrayList<HostAffected>(hostAffectedMap.values()));
    }
    return wrappedIaasHealthByTrigger;
  }
}