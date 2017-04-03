package com.indigo.zabbix.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

/**
 * Created by jose on 31/03/17.
 */
public class IAMClient {

  private static final Log logger = LogFactory.getLog(IAMClient.class);

  public static OAuthJSONAccessTokenResponse getAccessToken(String location,
                                                            String username, String password,
                                                            String clientId, String clientSecret) {
    try {
      OAuthClientRequest request = new OAuthClientRequest
          .TokenRequestBuilder(location)
          .setUsername(username)
          .setPassword(password)
          .setGrantType(GrantType.PASSWORD)
          .setClientId(clientId)
          .setClientSecret(clientSecret)
          .buildBodyMessage();

      OAuthClient client = new OAuthClient(new URLConnectionClient());

      return client.accessToken(request);

    } catch (OAuthSystemException e) {
      logger.error("Error building IAM request",e);
    } catch (OAuthProblemException e) {
      logger.error("Error getting access token from IAM",e);
    }

    return null;
  }

  public  static OAuthJSONAccessTokenResponse getAccessToken() {

    return getAccessToken(PropertiesManager.getProperty(ProbesTags.IAM_LOCATION),
        PropertiesManager.getProperty(ProbesTags.IAM_USERNAME),
        PropertiesManager.getProperty(ProbesTags.IAM_PASSWORD),
        PropertiesManager.getProperty(ProbesTags.IAM_CLIENTID),
        PropertiesManager.getProperty(ProbesTags.IAM_CLIENTSECRET));

  }

}
