package com.indigo.zabbix.launcher;

import com.indigo.zabbix.launcher.beans.OrchestratorDeployment;
import com.indigo.zabbix.launcher.beans.OrchestratorResponse;
import com.indigo.zabbix.launcher.client.OrchestratorClient;
import com.indigo.zabbix.utils.MetricsCollector;
import com.indigo.zabbix.utils.ProbeClientFactory;
import com.indigo.zabbix.utils.ZabbixMetrics;

import java.net.MalformedURLException;
import java.net.URL;

public class OrchestratorCollector implements MetricsCollector {

  private String hostname;
  private OrchestratorClient client;

  public OrchestratorCollector(String orchestratorLocation) throws MalformedURLException {
    this.hostname = new URL(orchestratorLocation).getHost();
    client = ProbeClientFactory.getClient(OrchestratorClient.class, orchestratorLocation, true);
  }

  @Override
  public ZabbixMetrics getMetrics() {
    OrchestratorResponse<OrchestratorDeployment> deployments = client.getDeployments();
    long failedDeployments =
        deployments
            .getContent()
            .stream()
            .filter(deployment -> deployment.getStatus().contains("FAILED"))
            .count();
    return null;
  }

  @Override
  public String getHostName() {
    return hostname;
  }

  @Override
  public String getGroup() {
    return null;
  }
}
