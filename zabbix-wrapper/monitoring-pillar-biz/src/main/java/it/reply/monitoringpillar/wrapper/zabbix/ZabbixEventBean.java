package it.reply.monitoringpillar.wrapper.zabbix;

import it.reply.monitoringpillar.adapter.zabbix.clientbuilder.ZabbixAdapterClientSetter;
import it.reply.monitoringpillar.adapter.zabbix.exception.ZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.DescriptionEvent;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.MonitPillarEventDescriptor;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.MonitPillarEventResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.FilterTimeRequest;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixHostGroupResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixItemResponse;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.ZabbixMonitoredHostResponseV24;
import it.reply.monitoringpillar.utility.TimestampMonitoring;
import it.reply.monitoringpillar.utils.datetime.FilterTimeRequestHandlerMonitoring;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class ZabbixEventBean {

  private final String problemEvent = "1";

  @Inject
  private ZabbixAdapterClientSetter<?> zabAdapPaas;

  /**
   * Get event.
   * 
   * @param zone
   *          name
   * @param serverType
   *          type
   * @param hosts
   *          list
   * @param hostId
   *          id
   * @param groups
   *          list
   * @param groupId
   *          id
   * @param tagService
   *          id
   * @param requestTime
   *          pojo
   * @return event response
   * @throws ZabbixException
   *           exe
   */
  public MonitPillarEventResponse getEvent(String zone, String serverType,
      List<ZabbixMonitoredHostResponseV24> hosts, String hostId,
      List<ZabbixHostGroupResponse> groups, String groupId, String tagService,
      FilterTimeRequest requestTime) throws ZabbixException {

    MonitPillarEventResponse wrappedEvent = new MonitPillarEventResponse();
    ArrayList<MonitPillarEventDescriptor> eventDescript = new ArrayList<>();
    Long dateFromEncoded = null;
    Long dateToEncoded = null;
    // check whether there is a time filter or not. If so, check it has been
    // written correctly
    if (requestTime != null) {
      // &&
      // FilterTimeRequestHandlerMonitoring.checkDateFormat(requestTime))
      // {
      // If dateTo is before dateFrom it throws an Exception
      // dateFromEncoded =
      // TimestampMonitoring.getDateFromFormatter(requestTime.getDateFrom());
      // dateToEncoded =
      // TimestampMonitoring.getDateToFormatter(requestTime.getDateTo());
      dateFromEncoded = requestTime.getFrom();
      dateToEncoded = requestTime.getTo();
      if (dateToEncoded < dateFromEncoded) {
        throw new IllegalArgumentException(
            "Incorrect date inserted: DateTo is before than DateFrom");
      }
    }

    // GET ALL EVENTS BY PASSING HOSTID
    List<ZabbixItemResponse> events = zabAdapPaas.getItemsService(zone, serverType, hostId, null,
        null, null, ZabbixMethods.EVENT.getzabbixMethod(), requestTime);

    // Loop into Events
    // For each event get the items associated with every trigger that shot
    // the event iteself
    for (ZabbixItemResponse event : events) {

      if (!event.getValue().equals(problemEvent)) {
        System.out.println("Just Considering Negative Events for now. "
            + "For the other way round change the constant to 0 in Zabbix Constant");
      } else {
        String triggerid = event.getObjectid();

        // Take the items 1:many relations with triggers
        List<ZabbixItemResponse> items = zabAdapPaas.getItemsService(zone, serverType, null, null,
            null, triggerid, ZabbixMethods.METRIC.getzabbixMethod(), requestTime);

        for (ZabbixItemResponse item : items) {
          String metricName = item.getName();

          if (event.getValue().equals(problemEvent)
          // || event.getValue().equals("0")
          ) {
            MonitPillarEventDescriptor wrappedEventDescritp = new MonitPillarEventDescriptor();

            /*
             * Logic for processing the data to return in function of requestTime
             */
            String clockEvent =
                TimestampMonitoring.decodUnixTime2Date(Long.parseLong(event.getClock()));
            DescriptionEvent descriptEvent = new DescriptionEvent();

            if (requestTime != null
                && FilterTimeRequestHandlerMonitoring.checkDateFormat(requestTime)) {
              try {
                // If dateTo is before dateFrom it launches an
                // Exception
                dateFromEncoded =
                    TimestampMonitoring.getDateFromFormatter(requestTime.getDateFrom());
                dateToEncoded = TimestampMonitoring.getDateToFormatter(requestTime.getDateTo());

                String clockEventFiletered = clockEvent;
                wrappedEventDescritp.setClock(clockEventFiletered);

                descriptEvent.setGruopName(groups.get(0).getName());
                descriptEvent.setHostName(hosts.get(0).getName());
                descriptEvent.setMetricName(metricName);
                wrappedEventDescritp.setDescription(descriptEvent);
                String key =
                    metricName + "." + hosts.get(0).getName() + "." + groups.get(0).getName();
                wrappedEventDescritp.setKey(key);
                eventDescript.add(wrappedEventDescritp);
              } catch (NullPointerException ne) {

                wrappedEventDescritp.setClock(
                    TimestampMonitoring.decodUnixTime2Date(Long.parseLong(event.getClock())));
                if ((event.getDescription() != null)) {
                  descriptEvent.setMetricName(event.getDescription());
                  wrappedEventDescritp.setDescription(descriptEvent);
                } else {
                  wrappedEventDescritp.setDescription(descriptEvent);
                }
                String key =
                    metricName + "." + hosts.get(0).getName() + "." + groups.get(0).getName();
                wrappedEventDescritp.setKey(key);

                eventDescript.add(wrappedEventDescritp);
              }

              // set Events in case time Filter is not applied
            } else {
              String clockEventFiletered = clockEvent;
              wrappedEventDescritp.setClock(clockEventFiletered);

              descriptEvent.setGruopName(groups.get(0).getName());
              descriptEvent.setHostName(hosts.get(0).getName());
              descriptEvent.setMetricName(metricName);
              wrappedEventDescritp.setDescription(descriptEvent);
              String key =
                  metricName + "." + hosts.get(0).getName() + "." + groups.get(0).getName();
              wrappedEventDescritp.setKey(key);
              eventDescript.add(wrappedEventDescritp);
            }
          }
        }
      }
    }
    wrappedEvent.setEvents(eventDescript);
    return wrappedEvent;
  }
}