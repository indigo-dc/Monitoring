package it.reply.monitoringpillar.wrapper.zabbix.paas;

import it.reply.monitoringpillar.adapter.zabbix.clientbuilder.ZabbixAdapterClientSetter;
import it.reply.monitoringpillar.adapter.zabbix.exception.ZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.handler.MetricsParserHelper;
import it.reply.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.reply.monitoringpillar.config.Configuration;
import it.reply.monitoringpillar.domain.dsl.monitoring.InfoType;
import it.reply.monitoringpillar.domain.dsl.monitoring.MonitoringConstant;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.FilterTimeRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.Group;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringWrappedResponsePaas;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.PaaSMetric;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.PaasMachine;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.PaasThreshold;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.PaasThreshold.PaasThresholdStatus;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.Service;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixHostGroupResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixItemResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostResponseV24;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixTemplateResponseV24;
import it.reply.monitoringpillar.domain.exception.MonitoringException;
import it.reply.monitoringpillar.domain.exception.NotFoundMonitoringException;
import it.reply.monitoringpillar.utility.TimestampMonitoring;
import it.reply.monitoringpillar.utils.datetime.FilterTimeRequestHandlerMonitoring;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class WrapperPaasBean implements Serializable {

  private static final long serialVersionUID = 1L;

  @Inject
  private ZabbixAdapterClientSetter<?> zabAdapSetter;

  @Inject
  private Configuration config;

  /**
   * Get the wrapped response for PaaS.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param groupName
   *          name
   * @param vmuuid
   *          name
   * @param serviceCategory
   *          name
   * @param tagService
   *          id
   * @param atomicServiceId
   *          id
   * @param metricsId
   *          id
   * @param triggerId
   *          id
   * @param history
   *          condition
   * @param requestTime
   *          pojo
   * @param groupInfo
   *          name
   * @param groupId
   *          id
   * @param hostInfo
   *          name
   * @param hostId
   *          id
   * @param items
   *          list of metrics
   * @param templates
   *          list of services
   * @param hostsByTagMap
   *          id
   * @param itemsByTemplateMap
   *          map of services
   * @return pojo
   * @throws ZabbixException
   *           wrapper
   * @throws MonitoringException
   *           wrapper
   */
  public MonitoringWrappedResponsePaas getWrappedPaas(String zone, String serverType,
      String groupName, String vmuuid, String serviceCategory, String tagService,
      List<String> atomicServiceId, String metricsId, List<String> triggerId, String history,
      FilterTimeRequest requestTime,

      List<ZabbixHostGroupResponse> groupInfo, String groupId,
      List<ZabbixMonitoredHostResponseV24> hostInfo, String hostId, List<ZabbixItemResponse> items,
      List<ZabbixTemplateResponseV24> templates,
      Map<ZabbixMonitoredHostResponseV24, List<ZabbixTemplateResponseV24>> hostsByTagMap,
      Map<ZabbixTemplateResponseV24, List<ZabbixItemResponse>> itemsByTemplateMap)
          throws ZabbixException, MonitoringException {

    MonitoringWrappedResponsePaas wrappedPaas = new MonitoringWrappedResponsePaas();

    // wrappedPaas.setEnvironment(testbedType);

    wrappedPaas.setGroups(getGroupInfo(zone, serverType, groupName, vmuuid, serviceCategory,
        tagService, atomicServiceId, metricsId, triggerId, history, requestTime, groupInfo, groupId,
        hostInfo, hostId, items, templates, hostsByTagMap, itemsByTemplateMap));
    return wrappedPaas;
  }

  /**
   * Get group wrapped infromation.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param groupName
   *          name
   * @param vmuuid
   *          uuid
   * @param serviceCategory
   *          name
   * @param tagService
   *          id
   * @param atomicServiceId
   *          id
   * @param metricsId
   *          id
   * @param triggerId
   *          id
   * @param history
   *          condition
   * @param requestTime
   *          pojo
   * @param workgroupInfo
   *          pojo
   * @param groupId
   *          id
   * @param hostInfo
   *          pojo
   * @param hostId
   *          id
   * @param items
   *          list metrics
   * @param templates
   *          list of services
   * @param hostsByTagMap
   *          map
   * @param itemsByTemplateMap
   *          map
   * @return list of groups
   * @throws ZabbixException
   *           wrapper
   * @throws MonitoringException
   *           wrapper
   */
  public ArrayList<Group> getGroupInfo(String zone, String serverType, String groupName,
      String vmuuid, String serviceCategory, String tagService, List<String> atomicServiceId,
      String metricsId, List<String> triggerId, String history, FilterTimeRequest requestTime,
      List<ZabbixHostGroupResponse> workgroupInfo, String groupId,
      List<ZabbixMonitoredHostResponseV24> hostInfo, String hostId, List<ZabbixItemResponse> items,
      List<ZabbixTemplateResponseV24> templates,
      Map<ZabbixMonitoredHostResponseV24, List<ZabbixTemplateResponseV24>> hostsByTagMap,
      Map<ZabbixTemplateResponseV24, List<ZabbixItemResponse>> itemsByTemplateMap)
          throws ZabbixException, MonitoringException {

    // if (IaaS==true)
    String hostGroupIdtoAdapter = null;

    // Prepare the Array for groups
    List<Group> groups = new ArrayList<>();
    boolean workgroupFound = false;
    String hostName = null;
    String workgroupName = null;
    String serviceName = null;

    // For each group into array set the name and ask collect the machines
    for (ZabbixHostGroupResponse workgroup : workgroupInfo) {
      Group workGroup = new Group();
      workGroup.setPaasMachines(new ArrayList<PaasMachine>());

      workgroupName = workgroup.getName();
      if (groupName.equals(workgroupName)) {
        workgroupFound = true;
        workGroup.setGroupName(workgroupName);
      }

      /********************************
       * HOST UUID OR TAG ID AVAILABLE.
       ********************************/
      if (hostInfo != null && hostId != null) {

        String metricIdtoAdapter = null;
        String templateIdtoAdapter = null;
        String triggerIdtoAdapter = null;

        String tag = "";

        // Loop inside every host into array coming from Manager

        for (ZabbixMonitoredHostResponseV24 host : hostInfo) {
          PaasMachine paasMachine = new PaasMachine();
          paasMachine.setServices(new ArrayList<Service>());

          if (tagService != null) {
            templates = hostsByTagMap.get(host);
            tag = tagService;
          }

          serviceCategory = host.getInventory().getType();
          paasMachine.setServiceCategory(serviceCategory);

          hostName = host.getName();

          paasMachine.setServiceId(tag);

          hostId = host.getHostid();

          hostGroupIdtoAdapter = workgroup.getGroupid();

          workGroup.getPaasMachines()
              .addAll(getMachinesList(zone, serverType, hostGroupIdtoAdapter, templateIdtoAdapter,
                  metricIdtoAdapter, triggerIdtoAdapter, history, workgroupName, serviceName,
                  hostName, metricsId, requestTime, hostInfo, host, hostId, paasMachine, items,
                  templates, itemsByTemplateMap));
        }
        groups.add(workGroup);

        /**********************
         * Only group specified.
         **********************/

      } else {
        String metricIdtoAdapter = null;
        String templateIdtoAdapter = null;
        String triggerIdtoAdapter = null;

        hostGroupIdtoAdapter = groupId;
        workgroupName = workgroup.getName();
        workGroup.setGroupName(workgroupName);

        for (ZabbixMonitoredHostResponseV24 host : hostInfo) {

          hostId = host.getHostid();
          // GET the list of items
          items = host.getItems();
          // GET the list of templates EXTENDED (useful for cycling on
          // metrics associated to em.. From extended host answer
          // couldn't know it)
          templates = zabAdapSetter.getTemplatesExtendedService(zone, serverType, hostId,
              ZabbixMethods.TEMPLATE.getzabbixMethod());

          hostsByTagMap.put(host, templates);
          for (ZabbixTemplateResponseV24 template : templates) {

            // GET ONLY USEFUL ITEMS REALLY ASSOCIATED TO TEMPLATES
            List<ZabbixItemResponse> usefulItems = new ArrayList<>();
            for (ZabbixItemResponse itemFromExtHost : items) {
              for (ZabbixItemResponse item : template.getItems()) {
                if (item.getName().equals(itemFromExtHost.getName())) {
                  usefulItems.add(itemFromExtHost);
                }
              }
            }
            itemsByTemplateMap.put(template, usefulItems);
          }

          workGroup.getPaasMachines()
              .addAll(getMachinesList(zone, serverType, hostGroupIdtoAdapter, templateIdtoAdapter,
                  metricIdtoAdapter, triggerIdtoAdapter, history, workgroupName, serviceName,
                  hostName, metricsId, requestTime, hostInfo, host, hostId, new PaasMachine(),
                  items, templates, itemsByTemplateMap));
        }
        groups.add(workGroup);
      }
    }

    return (ArrayList<Group>) groups;
  }

  /**
   * Retrieves Machines depending on passed parameters.
   * 
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param hostGroupIdtoAdapter
   *          id
   * @param templateIdtoAdapter
   *          id
   * @param metricIdtoAdapter
   *          id
   * @param triggerIdtoAdapter
   *          id
   * @param history
   *          condition
   * @param hostgroupName
   *          name
   * @param serviceName
   *          name
   * @param hostName
   *          name
   * @param metricName
   *          name
   * @param requestTime
   *          name
   * @param hostInfo
   *          condition
   * @param host
   *          name
   * @param hostId
   *          id
   * @param paasMachine
   *          pojo
   * @param items
   *          list of metrics
   * @param templates
   *          list of services
   * @param itemsByTemplateMap
   *          metrics by services
   * @return list of paas machines
   * @throws ZabbixException
   *           wrapper
   * @throws MonitoringException
   *           wrapper
   */
  public ArrayList<PaasMachine> getMachinesList(String zone, String serverType,
      String hostGroupIdtoAdapter, String templateIdtoAdapter, String metricIdtoAdapter,
      String triggerIdtoAdapter, String history, String hostgroupName, String serviceName,
      String hostName, String metricName, FilterTimeRequest requestTime,
      List<ZabbixMonitoredHostResponseV24> hostInfo, ZabbixMonitoredHostResponseV24 host,
      String hostId, PaasMachine paasMachine, List<ZabbixItemResponse> items,
      List<ZabbixTemplateResponseV24> templates,
      Map<ZabbixTemplateResponseV24, List<ZabbixItemResponse>> itemsByTemplateMap)
          throws ZabbixException, MonitoringException {

    String vmip = null;
    ArrayList<ZabbixItemResponse> zabbixinterfaceResponse = new ArrayList<>();

    hostName = host.getName();
    hostId = host.getHostid();

    // API useful for getting IP and Host's networkInfo
    zabbixinterfaceResponse =
        (ArrayList<ZabbixItemResponse>) zabAdapSetter.getZabbixFeatureMultiService(zone, serverType,
            hostId, templateIdtoAdapter, ZabbixMethods.INTERFACE.getzabbixMethod());

    for (ZabbixItemResponse zabbixresult : zabbixinterfaceResponse) {
      vmip = zabbixresult.getIp();
    }
    paasMachine.setMachineName(hostName);
    paasMachine.setIp(vmip);
    paasMachine.setServiceCategory(host.getInventory().getType());
    paasMachine.setServiceId(host.getInventory().getTag());

    paasMachine.setServices(getAtomicService(zone, serverType, hostId, templateIdtoAdapter,
        metricIdtoAdapter, triggerIdtoAdapter, history, hostgroupName, serviceName, hostName,
        metricName, items, templates, itemsByTemplateMap, requestTime));
    List<PaasMachine> machines = new ArrayList<>();
    machines.add(paasMachine);

    if (machines.isEmpty()) {
      // if(paasMachine==null){
      throw new NotFoundMonitoringException("Host ID is not present in zabbix server");
    }
    // return paasMachine;
    return (ArrayList<PaasMachine>) machines;
  }

  /**
   * Get atomic service.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param hostIdtoAdapter
   *          id
   * @param templateIdtoAdapter
   *          id
   * @param metricIdtoAdapter
   *          id
   * @param triggerIdtoAdapter
   *          id
   * @param history
   *          copndition
   * @param hostGroupName
   *          name
   * @param serviceName
   *          service name
   * @param hostName
   *          name
   * @param metricsId
   *          id
   * @param items
   *          list of metrics
   * @param templates
   *          templates
   * @param itemsByTemplateMap
   *          map
   * @param requestTime
   *          pojo
   * @return list of pojos services
   * @throws ZabbixException
   *           wrapper
   * @throws MonitoringException
   *           wrapper
   */
  public List<Service> getAtomicService(String zone, String serverType, String hostIdtoAdapter,
      String templateIdtoAdapter, String metricIdtoAdapter, String triggerIdtoAdapter,
      String history, String hostGroupName, String serviceName, String hostName, String metricsId,
      List<ZabbixItemResponse> items, List<ZabbixTemplateResponseV24> templates,
      Map<ZabbixTemplateResponseV24, List<ZabbixItemResponse>> itemsByTemplateMap,
      FilterTimeRequest requestTime) throws ZabbixException, MonitoringException {

    ArrayList<Service> services = new ArrayList<>();
    int gr = 0;
    boolean itemMatched = false;
    for (gr = 0; gr < templates.size(); gr++) {
      Service service = new Service();

      String templateName = templates.get(gr).getName();
      String stringToFilter = templateName;

      if (templateName.contains(MonitoringConstant.ACTIVE)) {
        serviceName = stringToFilter.substring(stringToFilter.lastIndexOf("ive") + 3);
      } else {
        serviceName = stringToFilter.substring(stringToFilter.lastIndexOf("ate") + 3);
        // else {
        // throw new
        // ZabbixException("This host is in wrong MODE (Passive Agents
        // in
        // Pull mode): Platform has been configured for functioning in
        // active mode. Contact the administrator to switch it to
        // Passive");
        // }
      }
      service.setServiceName(serviceName);

      // if metric has been specified, then get just that
      if (metricsId != null && !(itemsByTemplateMap.isEmpty())) {
        for (ZabbixItemResponse item : itemsByTemplateMap.get(templates.get(gr))) {
          if (item.getName().equalsIgnoreCase(metricsId)) {
            itemMatched = true;
            break;
          }
        }
        // if(item didn't match just go back to check out whether there
        // are other templates to match the coming item with)
      }
      if (metricsId != null && !itemMatched) {
        ;
      } else {
        if (!(itemsByTemplateMap.isEmpty())) {
          items = itemsByTemplateMap.get(templates.get(gr));
        } else {
          // GET ONLY USEFUL ITEMS REALLY ASSOCIATED TO TEMPLATES
          List<ZabbixItemResponse> usefulItems = new ArrayList<>();
          for (ZabbixItemResponse itemFromExtHost : items) {
            for (ZabbixItemResponse item : templates.get(gr).getItems()) {
              if (item.getName().equals(itemFromExtHost.getName())) {
                usefulItems.add(itemFromExtHost);
                items = usefulItems;
              }
            }
          }
        }
        service
            .setPaasMetrics(getPaasMetrics(zone, serverType, null, templateName, metricIdtoAdapter,
                items, history, hostGroupName, serviceName, hostName, metricsId, requestTime));

        services.add(service);
        // In only one/more metrics have been specified, then only
        // one/more services have to be taken in consideration
        if (metricsId != null && !(services.isEmpty())) {
          break;
        }
      }
    }
    if (gr == templates.size() && services.isEmpty() && (metricsId != null && !itemMatched)) {
      throw new NotFoundMonitoringException("There is no match between template and passed metric: "
          + metricsId + ". Either metric name is wrong or is associated to host without template");
    }
    return services;
  }

  /**
   * For a certain host and a certain service category gets metrics associated to it.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param hostidtoAdapter
   *          id
   * @param templateName
   *          name
   * @param metricIdtoAdapter
   *          name
   * @param items
   *          names
   * @param history
   *          parameter
   * @param hostGroupName
   *          name
   * @param serviceName
   *          service
   * @param hostName
   *          name
   * @param metricsId
   *          id
   * @param requestTime
   *          pojo
   * @return list of pojos metrics
   * @throws ZabbixException
   *           wrapper
   * @throws MonitoringException
   *           wrapper
   */
  public List<PaaSMetric> getPaasMetrics(String zone, String serverType, String hostidtoAdapter,
      String templateName, String metricIdtoAdapter, List<ZabbixItemResponse> items, String history,
      String hostGroupName, String serviceName, String hostName, String metricsId,
      FilterTimeRequest requestTime) throws ZabbixException, MonitoringException {

    List<PaaSMetric> metrics = new ArrayList<>();

    /**********************************************
     * In case an External Script has been used and a particular Ceilometer metric is coming for a
     * specific Metric into API.
     ************************************************/
    if (metricsId != null
        && metricsId.toLowerCase().contains(MonitoringConstant.CEILOMETER.toLowerCase())
        && serverType.equalsIgnoreCase(InfoType.SERVICE.getInfoType())) {
      if (config.isIaaSCeilometerMetric(zone, serverType, metricsId)) {
        ;
      } else {
        throw new NotFoundMonitoringException(
            "Ceilometer Metric: " + metricsId + " does not exist into template");
      }

    }
    if (history == null) {

      if (metricsId != null) {
        /********************************
         * METRIC LAST VALUE - NO HISTORY. Only for the the specified Metric into API.
         *************/
        boolean metricFound = false;

        for (ZabbixItemResponse item : items) {
          PaaSMetric paasMetric = new PaaSMetric();
          if (item.getName().equals(metricsId)) {
            metricFound = true;
            setMetrics(zone, serverType, hostidtoAdapter, item, items, hostGroupName, hostName,
                serviceName, metrics, paasMetric);
          }
        }
        if (metricFound = false) {
          throw new NotFoundMonitoringException("Wrong Metric Name inserted: " + metricsId);
        }
      } else {
        for (ZabbixItemResponse item : items) {
          PaaSMetric paasMetric = new PaaSMetric();
          setMetrics(zone, serverType, hostidtoAdapter, item, items, hostGroupName, hostName,
              serviceName, metrics, paasMetric);
        }
        return metrics;
      }

      /**********
       * HISTORY.
       ***********/
    } else if (history != null) {

      PaaSMetric paasMetrics = new PaaSMetric();
      String valueType = null;
      boolean metricFound = false;
      String metricUnit = null;

      String actualMetricName = null;
      List<ZabbixItemResponse> historyTrendValues = new ArrayList<>();
      ArrayList<Float> values4Graph = new ArrayList<>();
      List<String> values4Graphstr = new ArrayList<String>();
      ArrayList<String> clock4Graph = new ArrayList<String>();
      List<String> clockTimestamp4Graph = new ArrayList<String>();
      Float metricValue = null;
      String metricTime = null;
      String metricValuestr = null;

      // Get the right combination of items to deal with
      if (metricsId != null) {
        ZabbixItemResponse actualItem = new ZabbixItemResponse();
        for (ZabbixItemResponse item : items) {
          // paasMetrics = new PaaSMetric();
          if (item.getName().equals(metricsId)) {
            actualItem = item;
            actualMetricName = metricsId;
            paasMetrics.setMetricName(actualMetricName);
            valueType = item.getValueType();
            paasMetrics.setMetricValue(Float.parseFloat(valueType));
            metricTime =
                TimestampMonitoring.decodUnixTime2Date(Long.parseLong(item.getLastclock()));
            paasMetrics.setMetricTime(metricTime);
            metricUnit = item.getUnits();
            paasMetrics.setMetricUnit(metricUnit);
            metricFound = true;
            break;
          }
        }
        if (metricFound = false) {
          throw new NotFoundMonitoringException("Wrong Metric Name inserted: " + metricsId);
        }

        /************************
         * NO FILTER TIME HISTORY.
         *************************/
        if (requestTime == null) {

          actualItem = new ZabbixItemResponse();
          for (ZabbixItemResponse item : items) {
            // paasMetrics = new PaaSMetric();
            if (item.getName().equals(metricsId)) {
              actualItem = item;
              actualMetricName = metricsId;
              paasMetrics.setMetricName(actualMetricName);
              valueType = item.getValueType();
              paasMetrics.setMetricValue(Float.parseFloat(valueType));
              metricTime =
                  TimestampMonitoring.decodUnixTime2Date(Long.parseLong(item.getLastclock()));
              paasMetrics.setMetricTime(metricTime);
              metricUnit = item.getUnits();
              paasMetrics.setMetricUnit(metricUnit);
              metricFound = true;
              break;
            }
          }
          if (metricFound = false) {
            throw new NotFoundMonitoringException("Wrong Metric Name inserted: " + metricsId);
          }

          historyTrendValues = zabAdapSetter.getItemsService(zone, serverType, null, null,
              actualItem.getItemid(), null, ZabbixMethods.HISTORY.getzabbixMethod(), requestTime);
          if (!(historyTrendValues.isEmpty())) {
            for (ZabbixItemResponse historyTrendItem : historyTrendValues) {
              try {
                metricValue = MetricsParserHelper.getMetricParsedValue(valueType,
                    historyTrendItem.getValue());
              } catch (NumberFormatException ne) {
                System.out.println(ne);
              }
              try {
                clock4Graph.add(TimestampMonitoring
                    .decodUnixTime2Date(Long.parseLong(historyTrendItem.getClock())));
              } catch (NumberFormatException ne) {
                System.out.println(ne);
              }

              setMetrics4History(paasMetrics, values4Graph, clock4Graph, actualMetricName,
                  metricValue, metricTime, hostGroupName, hostName, serviceName, metricUnit);

              values4Graph.add(roundFourDecimals(metricValue));
              paasMetrics.setHistoryValues(values4Graph);
              // Check this out better (the value of Type coming)
              paasMetrics.setMetricValue(roundFourDecimals(metricValue));
              paasMetrics.setMetricTime(metricTime);

              paasMetrics.setHistoryClock(clock4Graph);
              // paasMetrics.setMetricTime(clock4Graph.get(0));
              paasMetrics.setMetricName(actualMetricName);
              String metricKeyDebug =
                  hostGroupName + "." + hostName + "." + serviceName + "." + actualMetricName;
              paasMetrics.setMetricKey(metricKeyDebug);
              paasMetrics.setMetricUnit(metricUnit);
            }
          } else {
            setMetricAffectedByError(paasMetrics, values4Graph, metricTime, clock4Graph,
                actualMetricName, hostGroupName, serviceName, hostName, metricUnit);
          }
        }

        /**********************
         * FILTER TIME HISTORY.
         **********************/
        if (requestTime != null) {
          // HISTORY CASE vs TREND
          if (((requestTime.getDateFrom() != null || requestTime.getDateTo() != null))
              || (requestTime.getFrom() != null || requestTime.getTo() != null)) {
            setHistoryByTimestamp(zone, requestTime, serverType, valueType, actualItem, paasMetrics,
                hostGroupName, serviceName, hostName);
            // TODO: TREND when is gonna be used it
          } else if (requestTime != null
              && FilterTimeRequestHandlerMonitoring.checkDateFormat(requestTime)) {
            // TO Decomment in case Trend API starts working with
            // ceilometer metrics if
            // (FilterTimeRequestHandler.isBeforeThen24h(requestTime))
            // {
            // historyTrendValues = zabAdapSetter.getTrendService(
            // serverType, null, null, actualItem.getItemid(),
            // null, ZabbixMethods.TREND.getzabbixMethod(),
            // requestTime);
            // }

            // If the user wants value until 24hr before current
            // time
            // (it's the case when there isn't any filter time) uses
            // HISTORY API
            // else //TREND
            historyTrendValues = zabAdapSetter.getItemsService(zone, serverType, null, null,
                actualItem.getItemid(), null, ZabbixMethods.HISTORY.getzabbixMethod(), requestTime);
            // If the value of metric in trend(getValue_avg) or
            // history(getValue) is different from null and from
            // a empty string and time returned is null as well,
            // then
            // return metric as -1 value
            if (!(historyTrendValues.isEmpty()) || !(historyTrendValues.isEmpty())) {
              for (ZabbixItemResponse historyTrendItem : historyTrendValues) {

                if (historyTrendItem.getValue() != null || historyTrendItem.getValue() != ""
                    || historyTrendItem.getValue_avg() != null
                    || historyTrendItem.getValue_avg() != null) {

                  // TODO: Decomment when Trend API starts
                  // working
                  // ZABBIX 3.0
                  // with ceilometer metrics
                  // if (FilterTimeRequestHandler
                  // .isBeforeThen24h(requestTime)) {
                  // try {
                  // // TREND VALUE (getValue_avg)
                  // metricValue = MetricsParserHelper
                  // .getMetricParsedValue(
                  // valueType,
                  // historyTrendItem
                  // .getValue_avg());
                  // } catch (NumberFormatException ne) {
                  // System.out.println(ne);
                  // }
                  // } else
                  {
                    try {
                      // HISTORY (getValue)
                      metricValue = MetricsParserHelper.getMetricParsedValue(valueType,
                          historyTrendItem.getValue());
                      metricValuestr = String.valueOf(historyTrendItem.getValue());

                      metricValue = MetricsParserHelper.getMetricParsedValue(valueType,
                          historyTrendItem.getValue());

                    } catch (NumberFormatException ne) {
                      System.out.println(ne);
                    }
                  }
                  try {
                    clockTimestamp4Graph.add(historyTrendItem.getClock());
                    clock4Graph.add(TimestampMonitoring
                        .decodUnixTime2Date(Long.parseLong(historyTrendItem.getClock())));

                  } catch (NumberFormatException ne) {
                    System.out.println(ne);
                  }
                  values4Graph.add(roundFourDecimals(metricValue));
                  values4Graphstr.add(metricValuestr);

                  values4Graph.add(roundFourDecimals(metricValue));
                  paasMetrics.setHistoryValues(values4Graph);
                  // Check this out better (the value of Type
                  // coming)
                  paasMetrics.setMetricValue(roundFourDecimals(metricValue));
                  paasMetrics.setMetricTime(metricTime);

                  paasMetrics.setHistoryClock(clock4Graph);
                  // paasMetrics.setMetricTime(clock4Graph.get(0));
                  paasMetrics.setMetricName(actualMetricName);
                  String metricKeyDebug =
                      hostGroupName + "." + hostName + "." + serviceName + "." + actualMetricName;
                  paasMetrics.setMetricKey(metricKeyDebug);
                  paasMetrics.setMetricUnit(metricUnit);

                } else {
                  setMetricAffectedByError(paasMetrics, values4Graph, metricTime, clock4Graph,
                      actualMetricName, hostGroupName, serviceName, hostName, metricUnit);
                }
              }
              Map<String, List<String>> valuesAndClock =
                  getCorrectValues(values4Graphstr, clockTimestamp4Graph);
              List<String> correctedClocks4Graph = new ArrayList<String>();
              for (String clockCorrected : valuesAndClock.get("clockAdjusted")) {
                correctedClocks4Graph
                    .add(TimestampMonitoring.decodUnixTime2Date(Long.parseLong(clockCorrected)));
              }
              List<Float> correctedValues4Graph = new ArrayList<Float>();
              Float valueCorrectedFloat = 0f;
              for (String valueCorrected : valuesAndClock.get("valueAdjusted")) {
                valueCorrectedFloat = Float.valueOf(valueCorrected);
                correctedValues4Graph.add(valueCorrectedFloat);
              }
              setMetrics4History(paasMetrics, correctedValues4Graph, correctedClocks4Graph,
                  actualMetricName, metricValue, metricTime, hostGroupName, hostName, serviceName,
                  metricUnit);
            }
          } else {
            setMetricAffectedByError(paasMetrics, values4Graph, metricTime, clock4Graph,
                actualMetricName, hostGroupName, serviceName, hostName, metricUnit);
          }
        }
      }

      metrics.add(paasMetrics);
    }
    return metrics;
  }

  // SetHistory when a timestamp is coming straight from API (method for
  // maintaining compatibility with legacy)
  private void setHistoryByTimestamp(String zone, FilterTimeRequest requestTime, String serverType,
      String valueType, ZabbixItemResponse actualItem, PaaSMetric paasMetrics, String hostGroupName,
      String serviceName, String hostName) throws ZabbixException, MonitoringException {

    boolean metricFound = false;
    String metricUnit = null;
    String actualMetricName = null;
    List<ZabbixItemResponse> historyTrendValues = new ArrayList<>();
    ArrayList<Float> values4Graph = new ArrayList<>();
    List<String> values4Graphstr = new ArrayList<String>();
    ArrayList<String> clock4Graph = new ArrayList<String>();
    List<String> clockTimestamp4Graph = new ArrayList<String>();
    Float metricValue = null;
    String metricTime = null;
    String metricValuestr = null;

    historyTrendValues = zabAdapSetter.getItemsService(zone, serverType, null, null,
        actualItem.getItemid(), null, ZabbixMethods.HISTORY.getzabbixMethod(), requestTime);
    // If the value of metric in trend(getValue_avg) or
    // history(getValue) is different from null and from
    // a empty string and time returned is null as well, then
    // return metric as -1 value
    if (!(historyTrendValues.isEmpty()) || !(historyTrendValues.isEmpty())) {
      for (ZabbixItemResponse historyTrendItem : historyTrendValues) {

        if (historyTrendItem.getValue() != null || historyTrendItem.getValue() != ""
            || historyTrendItem.getValue_avg() != null || historyTrendItem.getValue_avg() != null) {
          {
            try {
              // HISTORY (getValue)
              metricValue =
                  MetricsParserHelper.getMetricParsedValue(valueType, historyTrendItem.getValue());
              metricValuestr = String.valueOf(historyTrendItem.getValue());
              metricValue =
                  MetricsParserHelper.getMetricParsedValue(valueType, historyTrendItem.getValue());
            } catch (NumberFormatException ne) {
              System.out.println(ne);
            }
          }
          try {
            clockTimestamp4Graph.add(historyTrendItem.getClock());
            clock4Graph.add(TimestampMonitoring
                .decodUnixTime2Date(Long.parseLong(historyTrendItem.getClock())));
          } catch (NumberFormatException ne) {
            System.out.println(ne);
          }
          values4Graph.add(roundFourDecimals(metricValue));
          values4Graphstr.add(metricValuestr);

          values4Graph.add(roundFourDecimals(metricValue));
          paasMetrics.setHistoryValues(values4Graph);
          paasMetrics.setMetricValue(roundFourDecimals(metricValue));
          paasMetrics.setMetricTime(metricTime);
          paasMetrics.setHistoryClock(clock4Graph);
          paasMetrics.setMetricName(actualMetricName);
          String metricKeyDebug =
              hostGroupName + "." + hostName + "." + serviceName + "." + actualMetricName;
          paasMetrics.setMetricKey(metricKeyDebug);
          paasMetrics.setMetricUnit(metricUnit);

        } else {
          setMetricAffectedByError(paasMetrics, values4Graph, metricTime, clock4Graph,
              actualMetricName, hostGroupName, serviceName, hostName, metricUnit);
        }
      }
      Map<String, List<String>> valuesAndClock =
          getCorrectValues(values4Graphstr, clockTimestamp4Graph);
      List<String> correctedClocks4Graph = new ArrayList<String>();
      for (String clockCorrected : valuesAndClock.get("clockAdjusted")) {
        correctedClocks4Graph
            .add(TimestampMonitoring.decodUnixTime2Date(Long.parseLong(clockCorrected)));
      }
      List<Float> correctedValues4Graph = new ArrayList<Float>();
      Float valueCorrectedFloat = 0f;
      for (String valueCorrected : valuesAndClock.get("valueAdjusted")) {
        valueCorrectedFloat = Float.valueOf(valueCorrected);
        correctedValues4Graph.add(valueCorrectedFloat);
      }
      setMetrics4History(paasMetrics, correctedValues4Graph, correctedClocks4Graph,
          actualMetricName, metricValue, metricTime, hostGroupName, hostName, serviceName,
          metricUnit);
    } else {
      setMetricAffectedByError(paasMetrics, values4Graph, metricTime, clock4Graph, actualMetricName,
          hostGroupName, serviceName, hostName, metricUnit);
    }
  }

  // Set the field to be inserted into the response body of history
  private void setMetrics4History(PaaSMetric paasMetrics, List<Float> values4Graph,
      List<String> clock4Graph, String actualMetricName, Float metricValue, String metricTime,
      String hostGroupName, String hostName, String serviceName, String metricUnit) {
    paasMetrics.setMetricValue(roundFourDecimals(metricValue));
    paasMetrics.setMetricTime(metricTime);
    paasMetrics.setHistoryValues(values4Graph);
    paasMetrics.setHistoryClock(clock4Graph);
    paasMetrics.setMetricName(actualMetricName);
    String metricKeyDebug =
        hostGroupName + "." + hostName + "." + serviceName + "." + actualMetricName;
    paasMetrics.setMetricKey(metricKeyDebug);
    paasMetrics.setMetricUnit(metricUnit);
  }

  private Map<String, List<String>> getCorrectValues(List<String> values4Graphstr,
      List<String> clockTimestamp4Graph) {

    Map<String, List<String>> valuesAndClocks = new HashMap<String, List<String>>();
    long previousValue = 0L;
    long currentValue = 0L;
    for (int m = 0; m < clockTimestamp4Graph.size(); m++) {

      if (m > 0) {
        previousValue = Long.valueOf(clockTimestamp4Graph.get(m--));
        currentValue = Long.valueOf(clockTimestamp4Graph.get(m));
        m++;

        if (!isTenMinsLater(previousValue, currentValue)) {
          int numberOfCellToFill =
              (int) ((Long.valueOf(previousValue) - Long.valueOf(currentValue)) / 600);

          for (int z = 1; z < numberOfCellToFill; z++) {
            long tenMinsLaterMissingValue = currentValue + 600;
            clockTimestamp4Graph.add(m, String.valueOf(tenMinsLaterMissingValue));
            values4Graphstr.add(m, "0");
            currentValue += 600;
            m++;
          }
        }
      }
    }
    List<String> clocksCorrected = clockTimestamp4Graph;
    List<String> valuesCorrected = values4Graphstr;
    valuesAndClocks.put("clockAdjusted", clocksCorrected);
    valuesAndClocks.put("valueAdjusted", valuesCorrected);
    return valuesAndClocks;
  }

  private boolean isTenMinsLater(long previousTime, long currentTime) {
    // boolean result = (currentTime == previousTime + 600) ? false : true;
    if (currentTime == (previousTime - 600)) {
      return true;
    } else {
      return false;
    }
  }

  // Method for truncate the decimal numbers after comma
  private Float roundFourDecimals(Float metricValue) {
    BigDecimal bd = new BigDecimal(metricValue);
    bd = bd.setScale(4, BigDecimal.ROUND_DOWN);
    return bd.floatValue();
    // DecimalFormat fourDForm = new DecimalFormat("#.####");
    // return Float.valueOf(fourDForm.format(metricValue));
  }

  private void setMetrics(String zone, String serverType, String hostidtoAdapter,
      ZabbixItemResponse item, List<ZabbixItemResponse> items, String serviceCategoryName,
      String hostName, String serviceName, List<PaaSMetric> metrics, PaaSMetric paasMetric)
          throws ZabbixException, MonitoringException {

    String metricName = item.getName();

    String metricValueType = item.getValueType();

    // If the value of metric is different from null and from a empty string
    // and time returned is null as well then return metric as -1 value
    Float metricValue = null;

    if (item.getLastvalue() != null && !(item.getLastvalue().equals(""))
    // && !(metricInstantTime.contains("1970"))
    ) {
      metricValue =
          (Float) MetricsParserHelper.getMetricParsedValue(metricValueType, item.getLastvalue());
    } else {
      metricValue = (float) -1;
    }

    paasMetric.setMetricName(metricName);
    paasMetric.setMetricValue(metricValue);
    String metricKeyDebug =
        serviceCategoryName + "." + hostName + "." + serviceName + "." + metricName;
    paasMetric.setMetricKey(metricKeyDebug);
    paasMetric.setMetricUnit(item.getUnits());
    String time = item.getLastclock();
    String metricInstantTime = TimestampMonitoring.decodUnixTime2Date(Long.parseLong(time));
    if (metricInstantTime.contains("1970")) {
      paasMetric.setMetricTime("Instant null because no metrics were returned in the last 24hs");
    } else {
      paasMetric.setMetricTime(metricInstantTime);
    }
    String metricIdtoAdapter = item.getItemid();
    paasMetric.setPaasThresholds(getThreshold(zone, serverType, hostidtoAdapter, metricIdtoAdapter,
        serviceCategoryName, serviceName, hostName, metricName, metricValue, metricValueType));
    metrics.add(paasMetric);
  }

  /**
   * Set the metric values.
   * 
   * @param paasMetrics
   *          pojo
   * @param values4Graph
   *          values
   * @param metricTime
   *          pojo time
   * @param clock4Graph
   *          value
   * @param actualMetricName
   *          name
   * @param hostGroupName
   *          name
   * @param serviceName
   *          service name
   * @param hostName
   *          name
   * @param metricUnit
   *          unit of measure
   */
  public void setMetricAffectedByError(PaaSMetric paasMetrics, List<Float> values4Graph,
      String metricTime, List<String> clock4Graph, String actualMetricName, String hostGroupName,
      String serviceName, String hostName, String metricUnit) {

    Float metricValue = -1f;
    values4Graph.add(0f);

    paasMetrics.setHistoryValues(values4Graph);

    paasMetrics.setMetricValue(metricValue);
    paasMetrics.setMetricTime(
        TimestampMonitoring.decodUnixTime2Date(FilterTimeRequestHandlerMonitoring.now));

    clock4Graph.add(TimestampMonitoring.decodUnixTime2Date(FilterTimeRequestHandlerMonitoring.now));

    paasMetrics.setHistoryClock(clock4Graph);
    paasMetrics.setMetricName(actualMetricName);
    String metricKeyDebug =
        hostGroupName + "." + hostName + "." + serviceName + "." + actualMetricName;
    paasMetrics.setMetricKey(metricKeyDebug);
    paasMetrics.setMetricUnit(metricUnit);
  }

  /**
   * Get Triggers for each metric.
   * 
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param hostidtoAdapter
   *          id
   * @param metricIdtoAdapter
   *          id
   * @param serviceCategoryName
   *          name
   * @param serviceName
   *          naem
   * @param hostName
   *          name
   * @param metricName
   *          name
   * @param metricValue
   *          value
   * @param metricValueType
   *          type
   * @return list of triggers pojo
   * @throws ZabbixException
   *           wrapper
   * @throws MonitoringException
   *           wrapper
   */
  public List<PaasThreshold> getThreshold(String zone, String serverType, String hostidtoAdapter,
      String metricIdtoAdapter, String serviceCategoryName, String serviceName, String hostName,
      String metricName, Float metricValue, String metricValueType)
          throws ZabbixException, MonitoringException {

    List<PaasThreshold> thresholds = new ArrayList<>();
    String triggerValue = null;
    List<ZabbixItemResponse> trigger4Host = new ArrayList<>();

    trigger4Host = zabAdapSetter.getTriggerService(zone, serverType, hostidtoAdapter,
        metricIdtoAdapter, ZabbixMethods.TRIGGER.getzabbixMethod());

    for (ZabbixItemResponse triggerList : trigger4Host) {
      PaasThreshold thresholdtype = new PaasThreshold();
      String triggerExpression = triggerList.getExpression();
      triggerValue = triggerList.getValue();

      // case: trigger with negative value for boolean metric,
      // metricValueType=3(boolean); metricValueType=0(float);
      // (useful when the
      // check for running Service is coming from BL)
      if ((triggerValue.equals("0")) && (metricValueType.equals("3") || metricValueType.equals("0"))
          && metricValue == 0.0) {
        thresholdtype.setThresholdStatus(PaasThresholdStatus.PROBLEM);

        // generic case: trigger positive value matricValueType
        // different
        // from boolean
      } else if ((triggerValue.equals("0")) && metricValue != -1) {
        thresholdtype.setThresholdStatus(PaasThresholdStatus.OK);

        // case metric not returned yet (trigger value is positive when
        // function cannot be resolved)
      } else if (triggerValue.equals("0") && metricValue == -1) {
        thresholdtype.setThresholdStatus(PaasThresholdStatus.PROBLEM);
      } else {
        // normal case: trigger with negative value =1
        thresholdtype.setThresholdStatus(PaasThresholdStatus.PROBLEM);
      }
      thresholdtype.setThresholdExpression(triggerExpression);
      thresholdtype.setThresholdName(triggerList.getDescription());
      thresholds.add(thresholdtype);
    }
    return thresholds;
  }
}
