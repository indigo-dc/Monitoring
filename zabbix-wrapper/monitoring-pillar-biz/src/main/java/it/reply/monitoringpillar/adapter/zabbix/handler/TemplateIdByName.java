package it.reply.monitoringpillar.adapter.zabbix.handler;

import it.reply.monitoringpillar.adapter.zabbix.ZabbixConstant;
import it.reply.monitoringpillar.adapter.zabbix.clientbuilder.ZabbixAdapterClientSetter;
import it.reply.monitoringpillar.adapter.zabbix.exception.NotFoundZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.exception.ZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.reply.monitoringpillar.config.Configuration;
import it.reply.monitoringpillar.domain.dsl.monitoring.InfoType;
import it.reply.monitoringpillar.domain.dsl.monitoring.MonitoringConstant;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixTemplateResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class TemplateIdByName implements Serializable {

  private static final long serialVersionUID = 1L;

  @Inject
  private ZabbixAdapterClientSetter<?> zabAdapMetrics;

  @Inject
  private Configuration config;

  /**
   * Get TemplateId.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param services
   *          list
   * @param activeMode
   *          condition
   * @return list of template
   * @throws ZabbixException
   *           wrapper
   */
  public List<String> getTemplateId(String zone, String serverType, List<String> services,
      Boolean activeMode) throws ZabbixException {

    ArrayList<ZabbixTemplateResponse> templateList =
        (ArrayList<ZabbixTemplateResponse>) zabAdapMetrics.getTemplateService(zone, serverType,
            ZabbixMethods.TEMPLATE.getzabbixMethod());

    ArrayList<String> templateIDs = new ArrayList<String>();

    String templateMode = null;
    String templateName = null;
    boolean templateIaaSFoundAndMatchingWithProp = false;
    if (activeMode == null || activeMode == true) {
      templateMode = ZabbixConstant.TEMPLATE_RADIX_ACTIVE_MODE;
    } else if (activeMode == false) {
      templateMode = ZabbixConstant.TEMPLATE_RADIX_PASSIVE_MODE;
    }
    // FOR EACH SERVICE PASSED
    for (String service : services) {
      // FOR EACH TEMPLATE verify whether its name matches with the one
      // passed
      for (int j = 0; j < templateList.size(); j++) {
        templateName = templateList.get(j).getName();

        if (!config.getMonitoringConfigurations().getOptions().isMultipleServers()
            && templateName.equalsIgnoreCase(service)) {
          templateIDs.add(templateList.get(j).getTemplateid());
          break;
        }
        // If Ceilometer External Script has been used -->
        // ADD IAAS TEMPLATE NO MATTER WHAT.. and check Active mode if
        // set
        if (config.getMonitoringConfigurations().getOptions().isIaasMonitoring()) {
          if (templateName
              .equalsIgnoreCase(templateMode + config.getMonitoringMappings().getServiceMonitoring()
                  .getIaasCategoryService(MonitoringConstant.ATOMIC_SERVICE_IAAS)
                  .getIaasAtomicService(MonitoringConstant.ATOMIC_SERVICE_IAAS).getName())
              && serverType.equalsIgnoreCase(InfoType.SERVICE.getInfoType())
              && (!service.contains(MonitoringConstant.ATOMIC_SERVICE_AGGREGATOR))
              && templateIaaSFoundAndMatchingWithProp == false) {
            templateIDs.add(templateList.get(j).getTemplateid());
            templateIaaSFoundAndMatchingWithProp = true;
          }
        }
        if (templateName.equalsIgnoreCase(templateMode + service)
            || (service.contains(MonitoringConstant.ATOMIC_SERVICE_AGGREGATOR)
                && templateName.equalsIgnoreCase(ZabbixConstant.TEMPLATE_AGGREGATOR))) {
          templateIDs.add(templateList.get(j).getTemplateid());
          if (templateIaaSFoundAndMatchingWithProp) {
            break;
          }
        }
      }
    }
    if (templateIDs.size() - 1 != services.size()
        && (serverType.equalsIgnoreCase(InfoType.SERVICE.getInfoType())
            && !services.get(0).contains(MonitoringConstant.ATOMIC_SERVICE_AGGREGATOR))
        && !config.getMonitoringConfigurations().getOptions().isMultipleServers()) {
      throw new NotFoundZabbixException(
          "One of the passed Atomic Services: " + Arrays.toString(services.toArray())
              + " does NOT have a corresponding Template into Zabbix Metrics.");
    }

    if (!templateIaaSFoundAndMatchingWithProp
        && serverType.equalsIgnoreCase(InfoType.SERVICE.getInfoType())
        && !services.get(0).contains(MonitoringConstant.ATOMIC_SERVICE_AGGREGATOR)) {
      throw new NotFoundZabbixException(
          "The Template IaaS Name does not exist into Monitoring Platform");
    }

    return templateIDs;
  }
}