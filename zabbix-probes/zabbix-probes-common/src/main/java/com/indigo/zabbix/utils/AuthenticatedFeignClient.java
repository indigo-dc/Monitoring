package com.indigo.zabbix.utils;

import com.nimbusds.openid.connect.sdk.token.OIDCTokens;
import feign.Client;
import feign.Request;
import feign.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuthenticatedFeignClient implements Client {

  private Date issued;
  private OIDCTokens currentTokens;
  private Client client = new Client.Default(null, null);

  private void refreshToken() {
    if (currentTokens == null) {
      currentTokens = IamClient.getAccessToken();
      issued = new Date();
    } else {
      long seconds = (new Date().getTime() - issued.getTime()) / 1000L;
      if (seconds > currentTokens.getAccessToken().getLifetime()) {
        currentTokens = IamClient.refreshToken(currentTokens.getRefreshToken().toString());
        issued = new Date();
      }
    }
  }

  private Response execute(Request request, Request.Options options, boolean retry)
      throws IOException {

    refreshToken();
    Request newRequest = request;
    if (currentTokens != null) {
      Map<String, Collection<String>> newHeaders = new HashMap<>(request.headers());
      newHeaders.put(
          "Authorization",
          Arrays.asList(new String[] {"Bearer " + currentTokens.getAccessToken().toString()}));
      newRequest =
          Request.create(
              request.httpMethod(), request.url(), newHeaders, request.body(), request.charset());
    }

    Response response = client.execute(newRequest, options);

    if (response.status() == 401 && retry) {
      return execute(request, options, false);
    } else {
      return response;
    }
  }

  @Override
  public Response execute(Request request, Request.Options options) throws IOException {
    return execute(request, options, true);
  }
}
