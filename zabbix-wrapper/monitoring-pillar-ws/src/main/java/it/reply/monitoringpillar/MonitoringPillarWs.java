package it.reply.monitoringpillar;

import it.reply.monitoringpillar.adapter.zabbix.exception.ZabbixException;
import it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.request.HostGroupMonitoringCreateRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.request.HostMonitoringRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.ProxyCreationRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.EventCallbackRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.FilterTimeRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.SendMailRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request.UpdateGroupName;
import it.reply.monitoringpillar.domain.exception.MonitoringException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/adapters")
public interface MonitoringPillarWs {

  /**********************
   * IAAS / PAAS.
   *********************/

  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response listAllAdapter(@Context HttpServletRequest request,
      @Context HttpServletResponse response) throws MonitoringException;

  @GET
  @Path("/{adapterType}/zones")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response listAllZones(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType)
          throws MonitoringException;

  @GET
  @Path("/{adapterType}/zones/{zone}/types")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response listAllServerType(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone) throws MonitoringException;

  // TODO: if an endpoint didn't manage some groups how are we supposed to be
  // dealing with that?

  /******************************
   * GET IaaS/PaaS Info by Groups.
   ******************************/
  @GET
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getGroupList(@Context HttpServletRequest request,
      @Context HttpServletResponse response,
      @DefaultValue("zabbix") @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type) throws MonitoringException;

  /***************************************
   * GET IaaS/PaaS Info of specific Group.
   ***************************************/
  @GET
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getGroupInfo(@Context HttpServletRequest request,
      @Context HttpServletResponse response,
      @DefaultValue("zabbix") @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group) throws MonitoringException;

  /*****************************************************************
   * CREATE GROUP INTO PAAS and IAAS PLATFORM by passing all as type.
   *****************************************************************/
  @POST
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createGroup(@Context HttpServletRequest request,
      @Context HttpServletResponse response,
      @DefaultValue("zabbix") @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      HostGroupMonitoringCreateRequest group) throws MonitoringException;

  /*******************************************
   * DELETE GROUP FROM PAAS and IAAS PLATFORM.
   ******************************************/
  @DELETE
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response deleteGroup(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group) throws MonitoringException;

  /***********************
   * UPDATE GROUP NAME.
   **********************/
  @PUT
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateGroup(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group, UpdateGroupName newGroupName) throws MonitoringException;

  /*****************************************************************
   * CREATE PROXY INTO PAAS and IAAS PLATFORM by passing all as type.
   * 
   * @throws ZabbixException
   **************************************************************/
  @POST
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}/proxies")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createProxy(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group, ProxyCreationRequest proxy) throws MonitoringException;

  /********************************************
   * DELETE PROXY FROM Monitoring PLATFORM.
   * 
   * @throws ZabbixException
   *********************************************/
  @DELETE
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}/proxies/{proxy}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response deleteProxy(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group, @PathParam("proxy") String proxy)
          throws MonitoringException;

  /*****************
   * GET HOSTS LIST.
   ******************/
  @GET
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}/hosts")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getHost(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group, @QueryParam("thresholds") Boolean thresholds,
      @QueryParam("service-id") String serviceId) throws MonitoringException;

  /************************************
   * CREATE HOST INTO IAAS/PAAS PLATFORM
   *
   * @throws it.reply.monitoringpillar.domain.exception.MonitoringException
   ************************************/
  @POST
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}/hosts/{host}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createHost(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group, @PathParam("host") String host,
      HostMonitoringRequest hostMonitoringRequest) throws MonitoringException;

  /***************************************
   * DELETE HOST FROM IAAS /PAAS PLATFORM.
   **************************************/
  @DELETE
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}/hosts/{host}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response deleteHost(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group, @PathParam("host") String host) throws MonitoringException;

  /*******************************************************************************
   * GET A SPECIFIC HOST INFO (and its carachteristics ) BY ITS NAME/UUID.
   ******************************************************************************/
  /**
   * Method useful for having a SPECIFIC HOST info (and its characteristics ) belonging to a
   * SPECIFIC Category service regardless of the fact that it could be belonging to a group of host
   * composing a Category service identified by the ID.
   */
  @GET
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}/hosts/{host}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getHostInfo(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group, @PathParam("host") String host) throws MonitoringException;

  /*****************
   * DISABLE HOST.
   *****************/
  /**
   * Method useful for having Events happening into the monitoring platform. They can be filtered by
   * period of time as much as the history
   */
  @PUT
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}/hosts/{host}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getDisableHost(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group, @PathParam("host") String host,
      @QueryParam("update") String update) throws MonitoringException;

  /*********************************************************
   * GET TRIGGERS STATUS (EVENT API Equivalent) BY HOSTNAME.
   **********************************************************/
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}/hosts/{host}/thresholds")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getThresholdsByHost(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group, @PathParam("host") String host) throws MonitoringException;

  /*********************************
   * HOST's EVENTS - NO FILTER TIME.
   ********************************/
  /**
   * Method useful for having Events happening into the monitoring platform. They can be filtered by
   * period of time as much as the history
   */
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}/hosts/{host}/events")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getEvents(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group, @PathParam("host") String host) throws MonitoringException;

  /*********************************
   * HOST's EVENTS WITH FILTER TIME.
   ********************************/
  /**
   * Method useful for having Events happening into the monitoring platform. They can be filtered by
   * period of time as much as the history
   */
  @Deprecated
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}/hosts/{host}/events")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getFilteredEvents(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group, @PathParam("host") String host,
      FilterTimeRequest requestTime) throws MonitoringException;

  /*********************************
   * HOST's EVENTS WITH FILTER TIME.
   ********************************/
  /**
   * Method useful for having Events happening into the monitoring platform. They can be filtered by
   * period of time as much as the history
   */
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}/hosts/{host}/events-filtered")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getFilteredEventsByTimestamp(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group, @PathParam("host") String host,
      @QueryParam("from") Long from, @QueryParam("to") Long to) throws MonitoringException;

  /***********************************************
   * LIST OF METRICS ASSOCIATED TO a SPECIFIC HOST.
   ***********************************************/
  /**
   * Method useful for having a specific metric info (identified by its name) belonging to a
   * specific host, category group, atomic service which all are to be specified in the request.
   */
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}/hosts/{host}/metrics")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getMetricList(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group, @PathParam("host") String host) throws MonitoringException;

  /*********************************
   * SPECIFIC METRIC REQUESTED.
   ********************************/
  /**
   * Method useful for having a specific metric info (identified by its name) belonging to a
   * specific host, category group, atomic service which all are to be specified in the request.
   */
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}/hosts/{host}/metrics/{metric}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getMetric(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group, @PathParam("host") String host,
      @PathParam("metric") String metric) throws MonitoringException;

  /*****************************
   * History without filter Time.
   ****************************/
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}/hosts/{host}/"
      + "metrics/{metric}/history")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getHistory(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group, @PathParam("host") String host,
      @PathParam("metric") String metric) throws MonitoringException;

  /*************************************
   * HISTORY BY HOSTNAME AND FILTER TIME.
   ************************************/
  /**
   * Method useful for having a specific metric info (identified by its name) belonging to a
   * specific host, category group, atomic service which all are to be specified in the request.
   */
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}/hosts/{host}/"
      + "metrics/{metric}/history")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getHistoryByTime(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group, @PathParam("host") String host,
      @PathParam("metric") String metric, FilterTimeRequest requestTime) throws MonitoringException;

  /************************************
   * HISTORY BY HOSTNAME AND FILTER TIME.
   ************************************/
  /**
   * Method useful for having a specific metric info (identified by its name) belonging to a
   * specific host, category group, atomic service which all are to be specified in the request.
   */
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}/hosts/{host}/"
      + "metrics/{metric}/filtered-history")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getHistoryByTime(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group, @PathParam("host") String host,
      @PathParam("metric") String metric, @QueryParam("from") Long from, @QueryParam("to") Long to)
          throws MonitoringException;

  /**************************************
   * HISTORY BY SERVICEID AND FILTER TIME.
   **************************************/
  /**
   * Method useful for having a specific metric info (identified by its name) belonging to a
   * specific host, category group, atomic service which all are to be specified in the request.
   */
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}/services/{service}/"
      + "metrics/{metric}/history")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getHistoryByServiceandTime(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group, @PathParam("service") Long service,
      @PathParam("metric") String metric, FilterTimeRequest requestTime) throws MonitoringException;

  /*****************
   * DISABLE METRIC.
   *****************/
  @Path("/{adapterType}/zones/{zone}/types/{type}/groups/{group}/hosts/{host}/metrics/{metric}")
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getDisableMetric(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      @PathParam("group") String group, @PathParam("host") String host,
      @PathParam("metric") String metric, @QueryParam("update") String update)
          throws MonitoringException;

  /************************
   * CALLBACK EVENT UPDATE.
   ***********************/
  @Path("/{adapterType}/zones/{zone}/types/{type}/event-callbacks")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getEventCallback(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      EventCallbackRequest eventCallback) throws MonitoringException;

  /*****************************
   * SEND EMAIL TO CREATED USER.
   ****************************/
  /************************
   * SEND MAILs.
   ***********************/
  @Path("/{adapterType}/zones/{zone}/types/{type}/sendmails")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getSendMail(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @PathParam("adapterType") String adapterType,
      @PathParam("zone") String zone, @PathParam("type") String type,
      SendMailRequest sendMailRequest) throws MonitoringException;

}