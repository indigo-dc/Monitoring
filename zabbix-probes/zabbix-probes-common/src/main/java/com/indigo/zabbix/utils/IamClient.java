package com.indigo.zabbix.utils;

import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.ResourceOwnerPasswordCredentialsGrant;
import com.nimbusds.oauth2.sdk.TokenErrorResponse;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponse;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponseParser;
import com.nimbusds.openid.connect.sdk.token.OIDCTokens;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/** Created by jose on 31/03/17. */
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
  public static OIDCTokens getAccessToken(
      String location, String username, String password, String clientId, String clientSecret) {
    try {
      AuthorizationGrant codeGrant =
          new ResourceOwnerPasswordCredentialsGrant(username, new Secret(password));
      // The credentials to authenticate the client at the token endpoint
      ClientID clientIdObj = new ClientID(clientId);
      Secret clientSecretObj = new Secret(clientSecret);
      ClientAuthentication clientAuth = new ClientSecretBasic(clientIdObj, clientSecretObj);

      // The token endpoint
      URI tokenEndpoint = new URI(location);

      // Make the token request
      TokenRequest request = new TokenRequest(tokenEndpoint, clientAuth, codeGrant);

      TokenResponse tokenResponse = OIDCTokenResponseParser.parse(request.toHTTPRequest().send());

      if (!tokenResponse.indicatesSuccess()) {
        // We got an error response...
        TokenErrorResponse errorResponse = tokenResponse.toErrorResponse();
        logger.error("Error getting access token: " + errorResponse.toJSONObject().toJSONString());
      } else {
        OIDCTokenResponse successResponse = (OIDCTokenResponse) tokenResponse.toSuccessResponse();
        return successResponse.getOIDCTokens();
      }

    } catch (ParseException e) {
      logger.error("Error parsing access token", e);
    } catch (IOException e) {
      logger.error("Error sending token request", e);
    } catch (URISyntaxException e) {
      logger.error("Invalid location URL", e);
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
  public static OIDCTokens getAccessToken() {

    return getAccessToken(
        PropertiesManager.getProperty(ProbesTags.IAM_LOCATION),
        PropertiesManager.getProperty(ProbesTags.IAM_USERNAME),
        PropertiesManager.getProperty(ProbesTags.IAM_PASSWORD),
        PropertiesManager.getProperty(ProbesTags.IAM_CLIENTID),
        PropertiesManager.getProperty(ProbesTags.IAM_CLIENTSECRET));
  }
}
