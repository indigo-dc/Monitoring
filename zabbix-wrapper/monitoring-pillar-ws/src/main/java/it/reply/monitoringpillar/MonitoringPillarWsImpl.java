package it.reply.monitoringpillar;

import it.reply.monitoringpillar.adapter.DelegatorAdapter;
import it.reply.monitoringpillar.config.Configuration;
import it.reply.monitoringpillar.domain.dsl.monitoring.InfoType;
import it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.request.HostGroupMonitoringCreateRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.request.HostMonitoringRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.protocol.MonitoringResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.MonitPillarEventResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.ProxyCreationRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas.WrappedIaasHealthByTrigger;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.EventCallbackRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.FilterTimeRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringPillarEventCallbackResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaas;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaasGroups;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaasGroups4HostList;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.SendMailRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.UpdateGroupName;
import it.reply.monitoringpillar.domain.exception.MonitoringException;
import it.reply.monitoringpillar.exception.MonitoringWsExceptionHandler;
import it.reply.monitoringpillar.wrapper.ServerTypesWrapper;
import it.reply.monitoringpillar.wrapper.WrapperProvider;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * 
 * @author m.grandolfo <br>
 *         <br>
 *         Class containing RESTful API that wrap Monitoring products' API such as Zabbix's ones
 * 
 */
@Interceptors(LoggingInterceptor.class)
public class MonitoringPillarWsImpl implements MonitoringPillarWs {

  private static String errorServerTypeUnallowed =
      "Error: Not Allowed for Infrastructure type, just Paas Platform type";
  private Calendar calendar = Calendar.getInstance();
  private Date now = calendar.getTime();

  @Inject
  DelegatorAdapter delegateAdapt;

  @Inject
  private WrapperProvider<?> wrapper;

  @Inject
  private Configuration config;

  @Inject
  private ServerTypesWrapper serverTypesWrapper;

  @Inject
  private MonitoringWsExceptionHandler monitExceptionHandler;

  /***************
   * GET ADAPTERS.
   ***************/
  @Override
  public Response listAllAdapter(@Context HttpServletRequest request,
      @Context HttpServletResponse response) {

    return MonitoringResponse
        .status(Status.OK, config.getMonitoringConfigurations().getAdapter().getType()) // new
        // PillarAdapter().withAdapters(Adapter.getAllAdapter()))
        .build().build();
  }

  /**
   * GET ALL ZONES.
   */
  @Override
  public Response listAllZones(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType) {
    try {
      return MonitoringResponse.status(Status.OK, config.getZoneNames()).build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }
  }

  /**********************
   * GET LIST OF SERVERS.
   **********************/
  @Override
  public Response listAllServerType(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone) {

    delegateAdapt.getAdapter(adapterType);
    try {
      return MonitoringResponse.status(Status.OK, serverTypesWrapper.getServerTypesWrapped(zone))
          .build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }
  }

  /*******************************
   * GET GROUPLIST WRAPPED MINIMAL.
   *****************************/
  @Override
  public Response getGroupList(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType) {
    try {
      MonitoringWrappedResponsePaasGroups wrappedGroup = new MonitoringWrappedResponsePaasGroups();
      wrappedGroup = delegateAdapt.getAdapter(adapterType).getGroupsInfoWrapped(zone, serverType);

      return MonitoringResponse.status(Status.OK, wrappedGroup).build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }
  }

  /**
   * Method returns the list of hosts for the specified group.
   * 
   * @param request
   *          http
   * @param response
   *          http
   * @param adapterType
   *          name
   * @param zone
   *          name
   * @param serverType
   *          name
   * @param groupName
   *          name
   * @return list of wrapped host response
   */
  public Response getHostList(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String groupName) {
    MonitoringWrappedResponsePaasGroups4HostList wrappedHostList =
        delegateAdapt.getAdapter(adapterType).getHostsInfoWrapped(zone, serverType, groupName);
    try {
      return MonitoringResponse.status(Status.OK, wrappedHostList).build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }

  }

  /*******************
   * UPDATE HOSTGROUP.
   ******************/
  @Override
  public Response updateGroup(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String groupName, UpdateGroupName newGroupName) {
    try {
      delegateAdapt.getAdapter(adapterType).updateMonitoredHostGroup(zone, serverType, groupName,
          newGroupName);
      return MonitoringResponse.status(Status.ACCEPTED, newGroupName.getNewHostGroupName()).build()
          .build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }
  }

  /*****************
   * IAAS / PAAS.
   *****************/

  /*****************************************
   * CREATE GROUP INTO PAAS/IAAS PLATFORM.
   * 
   * @param adapterType
   *          name
   * @return the name of group
   */
  @Override
  public Response createGroup(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      HostGroupMonitoringCreateRequest group) {
    try {
      delegateAdapt.getAdapter(adapterType).creationMonitoredHostGroup(zone, serverType,
          group.getHostGroupName());
      return MonitoringResponse.status(Status.CREATED, group.getHostGroupName()).build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }
  }

  /**********************
   * CREATE PROXY SERVER.
   **********************/
  @Override
  public Response createProxy(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String workGroup, ProxyCreationRequest proxy) {
    try {
      delegateAdapt.getAdapter(adapterType).creationMonitoredProxy(zone, serverType, workGroup,
          proxy.getProxyName());
      return MonitoringResponse.status(Status.CREATED, proxy.getProxyName()).build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }
  }

  /**********************
   * CREATE PROXY SERVER.
   **********************/
  @Override
  public Response deleteProxy(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String workGroup, String proxyName) {
    try {
      delegateAdapt.getAdapter(adapterType).deleteMonitoredProxy(zone, serverType, proxyName);
      return MonitoringResponse.status(Status.NO_CONTENT).build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }
  }

  /*****************************************
   * DELETE GROUP FROM PAAS / IAAS PLATFORM.
   * 
   * @param adapterType
   *          name
   * @param zone
   *          name
   * @param serverType
   *          name
   * @param groupName
   *          name
   * @return Rest response no content
   */
  @Override
  public Response deleteGroup(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String groupName) {
    try {
      delegateAdapt.getAdapter(adapterType).deleteMonitoredHostGroup(zone, serverType, groupName);
      return MonitoringResponse.status(Status.NO_CONTENT).build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }
  }

  /***************************************
   * CREATE HOST INTO IAAS /PAAS PLATFORM.
   * 
   * @param adapterType
   *          zabbix server
   * @return the name of host created
   * @throws MonitoringException
   *           which wraps the generic one generic one
   */
  @Override
  public Response createHost(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String groupName, String hostName, HostMonitoringRequest hostMonitoringRequest) {
    try {
      delegateAdapt.getAdapter(adapterType).creationMonitoredHost(zone, serverType, groupName,
          hostName, hostMonitoringRequest.getUuid(), hostMonitoringRequest.getIp(),
          hostMonitoringRequest.getServiceCategory(), hostMonitoringRequest.getServiceId(),
          hostMonitoringRequest.getAtomicServices(), hostMonitoringRequest.getActiveMode(),
          hostMonitoringRequest.getPort(), hostMonitoringRequest.getProxyName());
      return MonitoringResponse.status(Status.CREATED, hostName).build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }

  }

  /***************************************
   * DELETE HOST FROM IAAS /PAAS PLATFORM.
   * 
   * @param adapterType
   *          zabbix server
   * @return Response REST with no content
   * @throws MonitoringException
   *           which wraps the generic one generic one
   */
  @Override
  public Response deleteHost(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String groupName, String hostName) {
    try {
      delegateAdapt.getAdapter(adapterType).deleteMonitoredHost(zone, serverType, hostName, null);
      return MonitoringResponse.status(Status.NO_CONTENT).build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }

  }

  /**************************
   * GET Info by Groups.
   **************************/
  /**
   * Method that wraps Zabbix API in order to provide info about the gruop containing all the
   * monitored hosts, the metrics associated to themselves, as well as trigger. The API consumer
   * will be able to take actions based on the metrics and triggers' actual values. All this coming
   * from a specific testbed, zabbix server (in this case zabbic iaas) and adapter.
   */
  @Override
  public Response getGroupInfo(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String group) {
    try {
      return MonitoringResponse
          .status(Status.OK, wrapper.getWrapperIaaSPaaS(adapterType, zone, serverType, group, null))
          .build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }

  }

  /*******************************************
   * GET platform Info by GROUP and HOSTNAMES.
   ******************************************/
  @Override
  public Response getHostInfo(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String group, String host) {
    try {
      return MonitoringResponse
          .status(Status.OK, wrapper.getWrapperIaaSPaaS(adapterType, zone, serverType, group, host))
          .build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }

  }

  /******************************************************
   * GET HOSTs INFO, OR hosts affected by shot THRESHOLDS, OR HOSTs INFO By serviceId A deployed
   * service might be composed of two or more machines, so that it's needed to call those hosts
   * belonging to the category by searching them by the serviceID attached to themselves at the
   * creation stage. It retrieves all hosts belonging to a specific ATOMIC Service (properly tagged
   * since the creation of it) created by the user.
   * 
   * @param adapterType
   *          name
   * @param zone
   *          name
   * @param serverType
   *          name
   * @param group
   *          name
   * @param thresholds
   *          list of triggers
   * @param serviceId
   *          id
   * @return Rest response with host json wrapped
   */
  @Override
  public Response getHost(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String group, Boolean thresholds, String serviceId) {
    try {
      return MonitoringResponse.status(Status.OK, wrapper.getWrapperHostComb(adapterType, zone,
          serverType, group, null, thresholds, null, serviceId)).build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }

  }

  /********************************************************
   * GET THRESHOLDS STATUS (EVENT API Equivalent) BY HOST.
   * 
   * @param adapterType
   *          name
   * @param zone
   *          name
   * @param serverType
   *          name
   * @param group
   *          name
   * @param host
   *          name
   * @return Rest response with the trigger wrapped
   */

  @Override
  public Response getThresholdsByHost(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String group, String host) {
    try {
      WrappedIaasHealthByTrigger wrapperTrigger =
          delegateAdapt.getAdapter(adapterType).getTriggerByHost(zone, serverType, group, host);
      return MonitoringResponse.status(Status.OK, wrapperTrigger).build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }
  }

  /*********************************
   * HOST's EVENTS NO FILTER TIME.
   ********************************/
  /**
   * Method useful for having Events happening into the monitoring platform. They can be filtered by
   * period of time as much as the history.
   */
  @Override
  public Response getEvents(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String group, String host) {
    try {
      MonitPillarEventResponse wrappedEvent = delegateAdapt.getAdapter(adapterType)
          .getOverallServerEvents(zone, serverType, host, group, null, null);
      return MonitoringResponse.status(Status.OK, wrappedEvent).build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }
  }

  /*********************************
   * HOST's EVENTS WITH FILTER TIME.
   ********************************/
  /**
   * Method useful for having Events happening into the monitoring platform. They can be filtered by
   * period of time as much as the history.
   * 
   * @param adapterType
   *          name
   * @param zone
   *          naem
   * @param serverType
   *          name
   * @param group
   *          name
   * @param host
   *          name
   * @param requestTime
   *          json
   * @return the wrapped Zabbix APIs describing
   */
  @Deprecated
  @Override
  public Response getFilteredEvents(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String group, String host, FilterTimeRequest requestTime) {
    try {
      MonitPillarEventResponse wrappedEvent = delegateAdapt.getAdapter(adapterType)
          .getOverallServerEvents(zone, serverType, null, null, null, requestTime);
      return MonitoringResponse.status(Status.OK, wrappedEvent).build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }
  }

  @Override
  public Response getFilteredEventsByTimestamp(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String group, String host, Long from, Long to) {
    FilterTimeRequest filterTime = new FilterTimeRequest();
    filterTime.setFrom(from);
    filterTime.setTo(to);
    try {
      MonitPillarEventResponse wrappedEvent = delegateAdapt.getAdapter(adapterType)
          .getOverallServerEvents(zone, serverType, host, group, null, filterTime);
      return MonitoringResponse.status(Status.OK, wrappedEvent).build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }
  }

  /*******
   * PaaS.
   *******/

  /*************************************************
   * GET LIST OF METRICS ASSOCIATED A sPECIFIC HOST.
   */
  @Override
  public Response getMetricList(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String group, String host) {
    try {
      return MonitoringResponse.status(Status.OK,
          wrapper.getWrapperHostComb(adapterType, zone, serverType, group, host, null, true, null))
          .build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }
  }

  /****************************
   * SPECIFIC METRIC REQUESTED.
   ***************************/
  /**
   * Method useful for having a specific metric info (identified by its name) belonging to a
   * specific host, category group, atomic service which all are to be specified in the request.
   * 
   * @param adapterType
   *          name
   * @param zone
   *          name
   * @param group
   *          name
   * @param host
   *          name
   * @param metric
   *          name
   * @return MonitoringWrappedResponsePaaS Object that wraps Zabbix APIs in order to give needed
   *         information about a host, service category, atomic services, metrics, triggers
   */

  @Override
  public Response getMetric(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String group, String host, String metric) {
    try {
      if (!isTypeAllowed(serverType)) {
        return MonitoringResponse.status(Status.BAD_REQUEST, errorServerTypeUnallowed).build()
            .build();
      } else {
        MonitoringWrappedResponsePaas wrappedPaas = (MonitoringWrappedResponsePaas) delegateAdapt
            .getAdapter(adapterType).getInfoWrapperGeneric(zone, serverType, group, host, null,
                null, null, metric, null, null, null);
        return MonitoringResponse.status(Status.OK, wrappedPaas).build().build();
      }
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }
  }

  /*****************************
   * HISTORY without filter Time.
   * 
   * @param adapterType
   *          name
   * @param zone
   *          name
   * @param serverType
   *          name
   * @param group
   *          name
   * @param host
   *          name
   * @param metric
   *          name
   * @return REST response listing the values and history
   */

  @Override
  public Response getHistory(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String group, String host, String metric) {
    try {
      String history = "true";
      if (!isTypeAllowed(serverType)) {
        return MonitoringResponse.status(Status.BAD_REQUEST, errorServerTypeUnallowed).build()
            .build();
      }

      MonitoringWrappedResponsePaas wrappedPaas = (MonitoringWrappedResponsePaas) delegateAdapt
          .getAdapter(adapterType).getInfoWrapperGeneric(zone, serverType, group, host, null, null,
              null, metric, null, history, null);
      return MonitoringResponse.status(Status.OK, wrappedPaas).build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }

  }

  /*********************************
   * HISTORY BY FILTER TIME.
   ********************************/
  /**
   * Method useful for having a specific metric info (identified by its name) belonging to a
   * specific host, category group, atomic service which all are to be specified in the request.
   * 
   * @return MonitoringWrappedResponsePaaS Object that wraps Zabbix APIs in order to give needed
   *         information about a host, service category, atomic services, metrics, triggers
   */

  @Override
  public Response getHistoryByTime(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String group, String host, String metric, FilterTimeRequest requestTime) {
    try {
      String history = "true";
      if (!isTypeAllowed(serverType)) {
        return MonitoringResponse.status(Status.BAD_REQUEST, errorServerTypeUnallowed).build()
            .build();
      } else {
        MonitoringWrappedResponsePaas wrappedPaas = (MonitoringWrappedResponsePaas) delegateAdapt
            .getAdapter(adapterType).getInfoWrapperGeneric(zone, serverType, group, host, null,
                null, null, metric, null, history, requestTime);
        return MonitoringResponse.status(Status.OK, wrappedPaas).build().build();
      }
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }

  }

  /*********************************
   * History By Timestamp FilterTime.
   ********************************/
  @Override
  public Response getHistoryByTime(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String group, String host, String metric, Long from, Long to) {
    FilterTimeRequest filterTime = new FilterTimeRequest();
    filterTime.setFrom(from);
    filterTime.setTo(to);
    try {
      MonitoringWrappedResponsePaas wrappedPaas = (MonitoringWrappedResponsePaas) delegateAdapt
          .getAdapter(adapterType).getInfoWrapperGeneric(zone, serverType, group, host, null, null,
              null, metric, null, "true", filterTime);
      return MonitoringResponse.status(Status.OK, wrappedPaas).build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }
  }

  // Method to validate Date format
  private String ckeckDateTo(FilterTimeRequest requestTime) {
    if (requestTime.getDateTo().getDay() != null) {

      return requestTime.getDateTo().getYear() + ", " + "{month: "
          + requestTime.getDateTo().getMonth() + ", " + "{day: " + requestTime.getDateTo().getDay()
          + ",  " + "{time: {" + requestTime.getDateTo().getTime().getHh() + ", " + "{"
          + requestTime.getDateTo().getTime().getMm() + ", " + "{"
          + requestTime.getDateTo().getTime().getSs() + "}," + "upToNow: "
          + requestTime.getDateTo().getUpToNow() + " } }";

    } else {
      return requestTime.getDateTo().getUpToNow() + " } }";
    }

  }

  /***********************
   * HISTORY BY SERVICEID.
   ***********************/
  @Override
  public Response getHistoryByServiceandTime(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String group, Long serviceId, String metric, FilterTimeRequest requestTime) {
    try {
      String history = "true";
      if (!isTypeAllowed(serverType)) {
        return MonitoringResponse.status(Status.BAD_REQUEST, errorServerTypeUnallowed).build()
            .build();
      } else {
        MonitoringWrappedResponsePaas wrappedPaas = (MonitoringWrappedResponsePaas) delegateAdapt
            .getAdapter(adapterType).getInfoWrapperGeneric(zone, serverType, group, null, null,
                String.valueOf(serviceId), null, metric, null, history, requestTime);
        return MonitoringResponse.status(Status.OK, wrappedPaas).build().build();
      }
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }
  }

  /***************
   * DISABLE HOST.
   ***************/
  /**
   * Method useful for having Events happening into the monitoring platform. They can be filtered by
   * period of time as much as the history.
   * 
   * @param adapterType
   *          name
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param group
   *          name
   * @param hostName
   *          name
   * @param update
   *          parameter
   * @return the wrapped Zabbix APIs describing
   */

  @Override
  public Response getDisableHost(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String group, String hostName, String update) {
    try {
      delegateAdapt.getAdapter(adapterType).getDisablingHost(zone, serverType, group, hostName,
          null, null, update);
      return MonitoringResponse.status(Status.OK, hostName).build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }

  }

  /*****************
   * DISABLE METRIC.
   *****************/
  @Override
  public Response getDisableMetric(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      String group, String hostName, String metric, String update) {
    try {
      delegateAdapt.getAdapter(adapterType).getDisablingItem(zone, serverType, group, hostName,
          metric, null, update);

      return MonitoringResponse.status(Status.OK, metric).build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }
  }

  /***********************
   * EVENT CALLBACK UPDATE.
   ***********************/
  @Override
  public Response getEventCallback(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      EventCallbackRequest eventCallback) {
    try {
      MonitoringPillarEventCallbackResponse wrappedEventCallback =
          delegateAdapt.getAdapter(adapterType).manageCallbackEvent(zone, eventCallback.getGroup(),
              eventCallback.getHostName(), eventCallback.getHostId(),
              eventCallback.getPaaSServiceId(), eventCallback.getIp(), eventCallback.getMetric(),
              eventCallback.getThreshold(), eventCallback.getTriggerStatus(),
              eventCallback.getDescription());

      return MonitoringResponse.status(Status.OK, wrappedEventCallback).build().build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }
  }

  /**************
   * SEND MAILS.
   **************/
  @Override
  public Response getSendMail(@Context HttpServletRequest request,
      @Context HttpServletResponse response, String adapterType, String zone, String serverType,
      SendMailRequest sendMailRequest) {
    try {
      delegateAdapt.getAdapter(adapterType).getSendMails(zone, serverType, sendMailRequest);
      return MonitoringResponse.status(Status.CREATED, sendMailRequest.getSendmailTo()).build()
          .build();
    } catch (MonitoringException me) {
      return monitExceptionHandler.toResponse(me);
    }

  }

  /**
   * Method for returning warning about using just the paas platform.
   * 
   * @param serverType
   *          name
   * @return a boolean type
   */
  public boolean isTypeAllowed(String serverType) {
    if (serverType.equalsIgnoreCase(InfoType.INFRASTRUCTURE.getInfoType())) {
      return false;
    } else {
      return true;
    }
  }

}