package com.indigo.zabbix.utils.tests;

import com.indigo.zabbix.utils.CollectorThread;
import com.indigo.zabbix.utils.PropertiesManager;
import com.indigo.zabbix.utils.ZabbixClient;

import io.github.hengyunabc.zabbix.sender.SenderResult;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by jose on 9/12/16.
 */
public class TestCollectorThread extends CollectorThread<TestCollector> {

  private String hostname;

  private boolean clearResult;
  private boolean createResult;
  private boolean retrieveResult;
  private boolean deleteResult;

  public TestCollectorThread(String category, String group, String template, String hostname,
                                boolean clearResult, boolean createResult, boolean retrieveResult,
                                boolean deleteResult, ZabbixClient client ) {
    super(client);
    this.hostname = hostname;
    this.clearResult = clearResult;
    this.createResult = createResult;
    this.retrieveResult = retrieveResult;
    this.deleteResult = deleteResult;
  }

  public SenderResult run() {
    return run("testprobe.properties");
  }

  @Override
  protected void loadConfiguration(String propertiesFile) throws IOException {
    PropertiesManager.loadProperties(new InputStreamReader(
        this.getClass().getResourceAsStream("/testprobe.properties")));
  }

  @Override
  protected TestCollector createCollector() {
    return new TestCollector(hostname, clearResult, createResult, retrieveResult, deleteResult);
  }
}
