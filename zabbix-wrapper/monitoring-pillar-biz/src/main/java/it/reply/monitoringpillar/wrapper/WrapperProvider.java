package it.reply.monitoringpillar.wrapper;

import it.reply.monitoringpillar.domain.exception.MonitoringException;

import javax.enterprise.inject.Model;

@Model
public interface WrapperProvider<T> {

  /**
   * It manages and returns the most generic wrapper.
   */
  public T getWrapperIaaSPaaS(String adapterType, String zone, String serverType, String group,
      String host) throws MonitoringException;

  /**
   * It manages and returns a specific host related wrapper.
   */
  public T getWrapperHostComb(String adapterType, String zone, String serverType, String group,
      String host, Boolean thresholds, Boolean metrics, String serviceId)
          throws MonitoringException;
}
