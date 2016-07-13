package it.reply.monitoringpillar.adapter.zabbix;

import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas.MonitoringPillarEventCallbackResponse;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MonitoringAdapterZabbixTest {

  @Mock
  public MonitoringAdapterZabbix adapterZabbixMocked = new MonitoringAdapterZabbix();

  @Test
  public void manageCallbackEventTest() {

    MonitoringPillarEventCallbackResponse expectedResponse =
        new MonitoringPillarEventCallbackResponse();
    expectedResponse.setDescription("whaterver");
    expectedResponse.setIp("8.8.8.8");
    expectedResponse.setMetricName("metricTest");
    expectedResponse.setServiceId("id");
    expectedResponse.setStatus("statusTest");
    expectedResponse.setTenant("tenantTest");
    expectedResponse.setTime(2L);
    expectedResponse.setTriggerName("triggerTest");
    expectedResponse.setVmName("vmTest");
    expectedResponse.setVmuuid("uuidTest");

    Mockito
        .when(adapterZabbixMocked.manageCallbackEvent(Mockito.anyString(), Mockito.anyString(),
            Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
            Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
        .thenReturn(expectedResponse);

    MonitoringPillarEventCallbackResponse actualValue =
        adapterZabbixMocked.manageCallbackEvent("zone", "group", "host", "vm", "service", "ip",
            "metric", "threshold", "status", "whatever");

    Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedResponse, actualValue));

  }

}
