package com.indigo.zabbix.utils.tests;

import com.indigo.zabbix.utils.ZabbixClient;
import com.indigo.zabbix.utils.ZabbixHost;
import com.indigo.zabbix.utils.ZabbixWrapperClient;
import com.indigo.zabbix.utils.beans.AppOperation;

import feign.Response;

import io.github.hengyunabc.zabbix.sender.DataObject;
import io.github.hengyunabc.zabbix.sender.SenderResult;
import io.github.hengyunabc.zabbix.sender.ZabbixSender;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jose on 1/12/16.
 */
public class ZabbixClientTest {

  private Map<String, Map<String, ZabbixHost>> hosts = new HashMap<>();

  private Map<String, Map<String, Class>> templates = new HashMap<>();

  private ZabbixWrapperClient wrapperClient;
  private ZabbixSender sender;

  private Response fakeResponse(int code) {
    return Response.create(code, null,
        new HashMap<String, Collection<String>>(), null, Charset.defaultCharset());
  }

  private Map<String, Class> createAppOperationTemplate() {
    Map<String, Class> template = new HashMap<>();

    for (AppOperation.Operation operation : AppOperation.Operation.values()) {
      String prefix = operation.name().toLowerCase();
      template.put(prefix + ".result", Boolean.class);
      template.put(prefix + ".status", Integer.class);
      template.put(prefix + ".responseTime", Long.class);
    }

    return template;
  }

  private ZabbixWrapperClient mockWrapper() {
    ZabbixWrapperClient result = Mockito.mock(ZabbixWrapperClient.class);

    Mockito.when(result.registerHost(Matchers.anyString(), Matchers.anyString(), Matchers.any()))
        .thenAnswer(new Answer<Response>() {
          @Override
          public Response answer(InvocationOnMock invocationOnMock) throws Throwable {
            String hostName = invocationOnMock.getArgumentAt(0, String.class);
            String group = invocationOnMock.getArgumentAt(1, String.class);
            ZabbixHost hostInfo = invocationOnMock.getArgumentAt(2, ZabbixHost.class);

            Map<String, ZabbixHost> groupMap = hosts.get(group);

            if (groupMap == null) {
              groupMap = new HashMap<String, ZabbixHost>();
              hosts.put(group, groupMap);
            }

            ZabbixHost existing = groupMap.get(hostName);
            if (existing != null) {
              return fakeResponse(405);
            }

            groupMap.put(hostName, hostInfo);

            return fakeResponse(200);
          }
        });

    Mockito.when(result.getHostInfo(Matchers.anyString(), Matchers.anyString())).thenAnswer(
        new Answer<Response>() {
          @Override
          public Response answer(InvocationOnMock invocationOnMock) throws Throwable {
            String hostName = invocationOnMock.getArgumentAt(0, String.class);
            String group = invocationOnMock.getArgumentAt(1, String.class);

            Map<String, ZabbixHost> groupMap = hosts.get(group);

            if (groupMap == null) {
              return fakeResponse(404);
            } else {
              ZabbixHost host = groupMap.get(hostName);
              if (host == null) {
                return fakeResponse(404);
              } else {
                return fakeResponse(200);
              }
            }
          }
        }
    );

    return result;
  }

  private ZabbixHost getHost(String hostName) {
    for (Map<String, ZabbixHost> groupHosts : hosts.values()) {
      ZabbixHost host = groupHosts.get(hostName);
      if (host != null) {
        return host;
      }
    }
    return null;
  }

  private ZabbixSender mockSender() throws IOException {
    ZabbixSender result = Mockito.mock(ZabbixSender.class);

    Mockito.when(result.send(Matchers.anyList())).thenAnswer(
        new Answer<SenderResult>() {

          @Override
          public SenderResult answer(InvocationOnMock invocationOnMock) throws Throwable {
            List<DataObject> data = invocationOnMock.getArgumentAt(0, List.class);

            int total = data.size();
            int processed = 0;
            int fail = 0;

            for (DataObject object : data) {

              String hostName = object.getHost();
              ZabbixHost host = getHost(hostName);
              if (host != null) {
                String[] templateList = host.getAtomicServices();
                if (templateList != null) {
                  String template = templateList[0];
                  Map<String, Class> templateInfo = templates.get(template);

                  if (templateInfo != null && templateInfo.get(object.getKey()) != null) {
                    processed++;
                  } else {
                    fail++;
                  }
                } else {
                  fail++;
                }
              } else {
                fail++;
              }
            }

            SenderResult result = new SenderResult();
            result.setTotal(total);
            result.setProcessed(processed);
            result.setFailed(fail);

            return result;
          }
        }
    );

    return result;
  }

  @Before
  public void prepare() {

    wrapperClient = mockWrapper();

    try {
      sender = mockSender();
    } catch (IOException e) {
      e.printStackTrace();
    }

    templates.put("testTemplate", createAppOperationTemplate());

  }

  @Test
  public void testAppOperation() {

    ZabbixClient client = new ZabbixClient("testCategory", "testGroup",
        "testTemplate", wrapperClient, sender);

    TestProbeThread testCorrect = new TestProbeThread(
        "testCategory", "testGroup","testTemplate", "testHost",
        true, true, true, true, client);

    SenderResult result = testCorrect.run();

    assert result != null;
    assert result.success();

  }

}
