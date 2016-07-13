package it.reply.monitoringpillar;

import it.reply.monitoringpillar.config.Configuration;
import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.protocol.MonitoringResponse.MonitoringResponseBuilder;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@RunWith(MockitoJUnitRunner.class)
public class MonitoringPillarWsImplTest {

  private static final String token = "90e28ba39e52a22247051f1452f2d15b";
  private static final String apdapterTestDev = "zabbix";
  private static final String zone = "paas-dev-core";

  @Mock
  private MonitoringPillarWsImpl wsImplMocked = new MonitoringPillarWsImpl();

  @InjectMocks
  private Configuration configMocked = new Configuration();

  @Inject
  private HttpServletRequest httpRequest;

  @Inject
  private HttpServletResponse httpResponse;

  @Test
  public void getAdaptersTest() {

    String adapterMocked = "zabbix";

    MonitoringResponseBuilder monitResponseBuilderMocked =
        new MonitoringResponseBuilder(Status.OK, adapterMocked);
    Response responseMocked = monitResponseBuilderMocked.build().build();

    // Mock
    Mockito.when(wsImplMocked.listAllAdapter(Mockito.any(HttpServletRequest.class),
        Mockito.any(HttpServletResponse.class))).thenReturn(responseMocked);

    // Mock Actual response method under test

    Response responseActual = wsImplMocked.listAllAdapter(httpRequest, httpResponse);

    Assert.assertTrue(EqualsBuilder.reflectionEquals(responseMocked, responseActual));

  }
}
