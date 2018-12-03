package com.indigo.zabbix.utils.tests;

import com.indigo.zabbix.utils.ProbeThread;
import com.indigo.zabbix.utils.PropertiesManager;
import com.indigo.zabbix.utils.ZabbixClient;
import io.github.hengyunabc.zabbix.sender.SenderResult;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/** Created by jose on 9/12/16. */
public class TestProbeThread extends ProbeThread<TestCollector> {

  private String hostname;

  private boolean clearResult;
  private boolean createResult;
  private boolean retrieveResult;
  private boolean deleteResult;

  public TestProbeThread(
      String category,
      String group,
      String template,
      String hostname,
      boolean clearResult,
      boolean createResult,
      boolean retrieveResult,
      boolean deleteResult,
      ZabbixClient client) {
    super(client);
    this.hostname = hostname;
    this.clearResult = clearResult;
    this.createResult = createResult;
    this.retrieveResult = retrieveResult;
    this.deleteResult = deleteResult;
  }

  public Map<String, SenderResult> run() {
    return run("testprobe.properties", new String[] {});
  }

  @Override
  protected void loadConfiguration(String propertiesFile, String[] args) throws IOException {
    PropertiesManager.loadProperties(
        new InputStreamReader(this.getClass().getResourceAsStream("/testprobe.properties")));
  }

  @Override
  protected List<TestCollector> createCollectors() {
    return Arrays.asList(
        new TestCollector(hostname, clearResult, createResult, retrieveResult, deleteResult));
  }
}
