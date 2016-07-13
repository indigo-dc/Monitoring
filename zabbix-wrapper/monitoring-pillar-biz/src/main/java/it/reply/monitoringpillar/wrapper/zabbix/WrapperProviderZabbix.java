package it.reply.monitoringpillar.wrapper.zabbix;

import it.reply.monitoringpillar.adapter.DelegatorAdapter;
import it.reply.monitoringpillar.config.Configuration;
import it.reply.monitoringpillar.domain.dsl.monitoring.InfoType;
import it.reply.monitoringpillar.domain.exception.MonitoringException;
import it.reply.monitoringpillar.domain.exception.NotImplementedMonitoringException;
import it.reply.monitoringpillar.wrapper.WrapperProvider;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
// @Local(WrapperProviderZabbix.class)
public class WrapperProviderZabbix<T> implements WrapperProvider<T>, Serializable {

  private static final long serialVersionUID = 1L;

  @EJB
  DelegatorAdapter determineAdapt;

  @EJB
  Configuration config;

  @SuppressWarnings({ "unchecked" })
  @Override
  public T getWrapperIaaSPaaS(String adapterType, String zone, String serverType, String group,
      String host) throws MonitoringException {

    return (T) determineAdapt.getAdapter(adapterType).getInfoWrapperGeneric(zone, serverType, group,
        host, null, null, null, null, null, null, null);
  }

  @Override
  public T getWrapperHostComb(String adapterType, String zone, String serverType, String group,
      String host, Boolean thresholds, Boolean metrics, String serviceId)
          throws MonitoringException {
    // Get all hosts info belonging to a group
    if ((thresholds == null || !thresholds) && serviceId == null && metrics == null) {
      return (T) determineAdapt.getAdapter(adapterType).getHostsInfoWrapped(zone, serverType,
          group);
      // Get thresholds wrapped response belonging to a Group
    } else if (thresholds != null && thresholds && serviceId == null) {
      return (T) determineAdapt.getAdapter(adapterType).getTriggerByGroup(zone, serverType, group);
      // Get metrics wrapped response belonging to a specific host
    } else if (thresholds == null && metrics != null && serviceId == null) {
      return (T) determineAdapt.getAdapter(adapterType).getMetricsByHost(zone, serverType, group,
          host);
      // Error case: Operation not allowed in infrastructure platform
    } else if ((thresholds == null || !thresholds) && serviceId != null) {
      if (serverType.equalsIgnoreCase(InfoType.INFRASTRUCTURE.getInfoType())
          && config.getMonitoringConfigurations().getOptions().isMultipleServers()) {
        throw new NotImplementedMonitoringException(
            "Error: Not Allowed for Infrastructure type, just Paas Platform type");
      } else {
        // Get generic wrapper for giving all info about the requested
        // resource
        return (T) determineAdapt.getAdapter(adapterType).getInfoWrapperGeneric(zone, serverType,
            group, null, null, serviceId, null, null, null, null, null);
      }
    } else {
      throw new NotImplementedMonitoringException("Error: functionality not implemented");
    }
  }
}