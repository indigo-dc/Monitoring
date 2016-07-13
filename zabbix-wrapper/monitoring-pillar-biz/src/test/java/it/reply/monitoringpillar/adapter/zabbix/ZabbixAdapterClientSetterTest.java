package it.reply.monitoringpillar.adapter.zabbix;

import it.reply.monitoringpillar.adapter.zabbix.clientbuilder.ZabbixAdapterClientSetter;
import it.reply.monitoringpillar.adapter.zabbix.exception.ZabbixException;
import it.reply.monitoringpillar.adapter.zabbix.handler.ZabbixFeatures.ZabbixMethods;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.HostGroupResponse;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ZabbixAdapterClientSetterTest {

  @Mock
  public ZabbixAdapterClientSetter clientSetterMocked = new ZabbixAdapterClientSetter();

  @Test
  public void updateHostGroupResponseTest() throws ZabbixException {

    HostGroupResponse expectedResponse = new HostGroupResponse();
    List<String> groupids = new ArrayList<>();
    groupids.add("id");
    expectedResponse.setGroupids(groupids);

    Mockito.when(clientSetterMocked.updateHostGroupService(Mockito.any(String.class),
        Mockito.any(String.class), Mockito.any(String.class), Mockito.any(String.class),
        Mockito.any(String.class))).thenReturn(expectedResponse);

    HostGroupResponse actualValue = clientSetterMocked.updateHostGroupService("zone", "service",
        "gruopTest", "groupId", ZabbixMethods.HOSTGROUPUPDATE.getzabbixMethod());

    Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedResponse, actualValue));
  }

}
