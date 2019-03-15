package com.indigo.zabbix.launcher.client;

import com.indigo.zabbix.launcher.beans.OrchestratorDeployment;
import com.indigo.zabbix.launcher.beans.OrchestratorResource;
import com.indigo.zabbix.launcher.beans.OrchestratorResponse;
import feign.Param;
import feign.RequestLine;

public interface OrchestratorClient {

  @RequestLine("GET /deployments")
  OrchestratorResponse<OrchestratorDeployment> getDeployments();

  @RequestLine("GET /deployments/{deploymentId}/resources")
  OrchestratorResponse<OrchestratorResource> getResources(
      @Param("deploymentId") String deploymentId);
}
