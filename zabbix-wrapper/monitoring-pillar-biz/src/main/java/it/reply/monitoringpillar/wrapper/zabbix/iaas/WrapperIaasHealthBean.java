package it.reply.monitoringpillar.wrapper.zabbix.iaas;

import it.reply.monitoringpillar.adapter.zabbix.clientbuilder.ZabbixAdapterClientSetter;
import it.reply.monitoringpillar.adapter.zabbix.exception.ZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas.HostAffected;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas.TriggerShot;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas.WrappedIaasHealthByTrigger;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixItemResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostsResponse;
import it.reply.monitoringpillar.utility.TimestampMonitoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class WrapperIaasHealthBean {

  @Inject
  private ZabbixAdapterClientSetter zabAdapClientSetter;

  /**
   * Get Trigger wrapped.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param group
   *          name
   * @return trigger wrapped
   * @throws ZabbixException
   *           exe
   */
  public WrappedIaasHealthByTrigger getTriggerforIaasHealth(String zone, String serverType,
      String group) throws ZabbixException {

    WrappedIaasHealthByTrigger wrappedIaasHealthByTrigger = new WrappedIaasHealthByTrigger();

    // Put into the map all the host found by cycling on triggers
    Map<String, HostAffected> hostAffectedMap = new HashMap<String, HostAffected>();

    // It retrieves all shuttered triggers
    List<ZabbixItemResponse> triggers = zabAdapClientSetter.getTriggerWithProblemsService(zone,
        serverType, null, null, ZabbixMethods.TRIGGER.getzabbixMethod(), group);

    for (ZabbixItemResponse zabbixItemResponse : triggers) {
      // get the host associated to trigger
      List<ZabbixMonitoredHostsResponse> hosts = zabAdapClientSetter.getHostsService(zone,
          serverType, ZabbixMethods.HOST.getzabbixMethod(), null, null, null,
          zabbixItemResponse.getTriggerid());

      // If the host is in the map just add it
      if (!hostAffectedMap.containsKey(hosts.get(0).getName())) {
        HostAffected hostAffected = new HostAffected();
        hostAffected.setHostName(hosts.get(0).getName());
        hostAffected.setTriggerShots(new ArrayList<TriggerShot>());
        hostAffectedMap.put(hosts.get(0).getName(), hostAffected);
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
      hostAffectedMap.get(hosts.get(0).getName()).getTriggerShots().add(triggerShot);

    }
    wrappedIaasHealthByTrigger.setHostGroup(group);
    wrappedIaasHealthByTrigger
        .setHostAffecteds(new ArrayList<HostAffected>(hostAffectedMap.values()));
    return wrappedIaasHealthByTrigger;
  }
}
