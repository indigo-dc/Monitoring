package com.indigo.zabbix.launcher.client;

import com.indigo.zabbix.launcher.beans.OrchestratorDeployment;
import com.indigo.zabbix.launcher.beans.OrchestratorResource;
import com.indigo.zabbix.launcher.beans.OrchestratorResponse;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface OrchestratorClient {

    @RequestLine("GET /deployments")
    @Headers("Authorization: Bearer {token}")
    OrchestratorResponse<OrchestratorDeployment> getDeployments(@Param("token") String token);

    @RequestLine("GET /deployments/{deploymentId}/resources")
    @Headers("Authorization: Bearer {token}")
    OrchestratorResponse<OrchestratorResource> getResources(@Param("token") String token,
                                            @Param("deploymentId") String deploymentId);

}
