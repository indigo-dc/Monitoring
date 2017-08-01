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
public class IamClient {

  private static final Log logger = LogFactory.getLog(IamClient.class);

  /**
   * Gets an access token from the IAM service.
   * 
   * @param location Location of the IAM server.
   * @param username Username for the authorized user.
   * @param password Password for the authorized user.
   * @param clientId Client ID to authorize.
   * @param clientSecret Client secret of the client ID application.
   * @return An access token for the provided user and application.
   */
  public static OAuthJSONAccessTokenResponse getAccessToken(String location, String username,
      String password, String clientId, String clientSecret) {
    try {
      OAuthClientRequest request = new OAuthClientRequest.TokenRequestBuilder(location)
          .setUsername(username).setPassword(password).setGrantType(GrantType.PASSWORD)
          .setClientId(clientId).setClientSecret(clientSecret).buildBodyMessage();

      OAuthClient client = new OAuthClient(new URLConnectionClient());

      return client.accessToken(request);

    } catch (OAuthSystemException e) {
      logger.error("Error building IAM request", e);
    } catch (OAuthProblemException e) {
      logger.error("Error getting access token from IAM", e);
    }

    return null;
  }

  /**
   * Utility method that will return an access token from parameters provided in the configuration
   * file. - iam.location: Location of the IAM server. - iam.username: Username for the authorized
   * user. - iam.password: Password for the authorized user. - iam.clientid: Client ID to authorize.
   * - iam.clientsecret: Client secret of the client ID application.
   * 
   * @return An access token for the provided user and application.
   */
  public static OAuthJSONAccessTokenResponse getAccessToken() {

    return getAccessToken(PropertiesManager.getProperty(ProbesTags.IAM_LOCATION),
        PropertiesManager.getProperty(ProbesTags.IAM_USERNAME),
        PropertiesManager.getProperty(ProbesTags.IAM_PASSWORD),
        PropertiesManager.getProperty(ProbesTags.IAM_CLIENTID),
        PropertiesManager.getProperty(ProbesTags.IAM_CLIENTSECRET));

  }

}
