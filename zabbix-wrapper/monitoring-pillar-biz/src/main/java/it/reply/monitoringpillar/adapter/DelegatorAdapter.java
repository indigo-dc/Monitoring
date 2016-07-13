package it.reply.monitoringpillar.adapter;

import it.reply.monitoringpillar.adapter.nagios.IMonitAdaptNagios;
import it.reply.monitoringpillar.adapter.nagios.MonitoringAdapteeNagios;
import it.reply.monitoringpillar.adapter.zabbix.IMonitAdaptZabbix;
import it.reply.monitoringpillar.domain.dsl.monitoring.Adapter;
import it.reply.monitoringpillar.domain.exception.IllegalArgumentMonitoringException;
import it.reply.monitoringpillar.domain.exception.MonitoringException;
import it.reply.monitoringpillar.domain.exception.NotFoundMonitoringException;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class DelegatorAdapter {

  @Inject
  @IMonitAdaptZabbix
  MonitoringTarget monitAdaptZabbix;

  @Inject
  @IMonitAdaptNagios
  MonitoringTarget monitAdaptNagios;

  @Inject
  MonitoringAdapteeZabbix adapteeZabbix;

  @Inject
  MonitoringAdapteeNagios adapteeNagios;

  /**
   * Get target.
   * 
   * @param adapter
   *          adapter to be routed
   * @return target
   * @throws MonitoringException
   *           exep
   * @throws NotFoundMonitoringException
   *           exep
   */
  public MonitoringTarget getAdapter(String adapter)
      throws MonitoringException, NotFoundMonitoringException {
    try {
      Adapter adap = Adapter.findByName(adapter);
      switch (adap) {
        case NAGIOS:
          return monitAdaptNagios;
        case ZABBIX:
          return monitAdaptZabbix;
        default:
          throw new IllegalArgumentMonitoringException("Cannot find implemetation for [" + adapter
              + "] into " + Adapter.class.getCanonicalName());
      }
    } catch (IllegalArgumentException ie) {
      throw new NotFoundMonitoringException("Cannot find [" + adapter + "] adapter", ie);
    }
  }
}