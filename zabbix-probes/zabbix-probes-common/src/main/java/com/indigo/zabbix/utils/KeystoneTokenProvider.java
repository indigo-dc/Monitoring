package com.indigo.zabbix.utils;

import com.indigo.zabbix.utils.beans.KeystoneScopedTokenRequest;
import com.indigo.zabbix.utils.beans.OpenstackProjectsInfo;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;

/**
 * Created by jose on 30/03/17.
 */
public interface KeystoneTokenProvider {

  @RequestLine("GET /v3/OS-FEDERATION/identity_providers/indigo-dc/protocols/oidc/auth")
  @Headers("Authorization: Bearer {token}")
  Response getKeystoneTokenOIDC(@Param("token") String accessToken);

  @RequestLine("GET /v3/OS-FEDERATION/identity_providers/indigo-dc/protocols/iamoidc/auth")
  @Headers("Authorization: Bearer {token}")
  Response getKeystoneTokenIAMOIDC(@Param("token") String accessToken);
  
  @RequestLine("GET /v3/auth/projects")
  @Headers("X-Auth-Token: {token}")
  OpenstackProjectsInfo getProjects(@Param("token") String keystoneToken);

  @RequestLine("POST /v3/auth/tokens")
  @Headers("Content-type: application/json")
  Response getScopedToken(KeystoneScopedTokenRequest request);

}