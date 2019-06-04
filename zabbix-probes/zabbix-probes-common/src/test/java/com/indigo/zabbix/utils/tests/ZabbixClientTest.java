package com.indigo.zabbix.utils.tests;

import com.indigo.zabbix.utils.ZabbixClient;
import com.indigo.zabbix.utils.ZabbixHost;
import com.indigo.zabbix.utils.ZabbixWrapperClient;
import com.indigo.zabbix.utils.beans.AppOperation;
import feign.Request;
import feign.Response;
import io.github.hengyunabc.zabbix.sender.DataObject;
import io.github.hengyunabc.zabbix.sender.SenderResult;
import io.github.hengyunabc.zabbix.sender.ZabbixSender;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Created by jose on 1/12/16. */
public class ZabbixClientTest {

  private Map<String, Map<String, ZabbixHost>> hosts = new HashMap<>();

  private Map<String, Map<String, Class>> templates = new HashMap<>();

  private ZabbixWrapperClient wrapperClient;
  private ZabbixSender sender;

  private Response fakeResponse(int code) {
    return Response.builder()
        .status(code)
        .request(
            Request.create(
                Request.HttpMethod.GET,
                "http://example.com",
                new HashMap<>(),
                new byte[] {},
                Charset.defaultCharset()))
        .headers(new HashMap<>())
        .build();
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

    Mockito.when(
            result.registerHost(
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.any()))
        .thenAnswer(
            new Answer<Response>() {

              @Override
              public Response answer(InvocationOnMock invocationOnMock) {
                String hostName = invocationOnMock.getArgument(0);
                String group = invocationOnMock.getArgument(1);
                ZabbixHost hostInfo = invocationOnMock.getArgument(2);

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

    Mockito.when(result.getHostInfo(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
        .thenAnswer(
            new Answer<Response>() {
              @Override
              public Response answer(InvocationOnMock invocationOnMock) {
                String hostName = invocationOnMock.getArgument(0);
                String group = invocationOnMock.getArgument(1);

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
            });

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

    Mockito.when(result.send(ArgumentMatchers.anyList()))
        .thenAnswer(
            new Answer<SenderResult>() {

              @Override
              public SenderResult answer(InvocationOnMock invocationOnMock) {
                List<DataObject> data = invocationOnMock.getArgument(0);

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
            });

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

    ZabbixClient client =
        new ZabbixClient("testCategory", "testTemplate", wrapperClient, sender);

    TestProbeThread testCorrect =
        new TestProbeThread(
            "testHost",
            true,
            true,
            true,
            true,
            client);

    Map<String, SenderResult> result = testCorrect.run();

    assert result != null;
    assert !result.isEmpty();
    assert result.values().iterator().next().success();
  }
}
