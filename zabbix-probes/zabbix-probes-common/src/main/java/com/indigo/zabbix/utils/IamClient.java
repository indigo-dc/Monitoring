package com.indigo.zabbix.utils;

import java.io.IOException;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest.TokenRequestBuilder;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.token.OAuthToken;

public class IamClient {

	public static OAuthJSONAccessTokenResponse getJsonToken() {

		OAuthJSONAccessTokenResponse response = new OAuthJSONAccessTokenResponse();
		// Start the monitoring process
		try {
			PropertiesManager.loadProperties("openstackprobe.properties");
			/*
			 * ProbeThread probeManager = ProbeThread.instance();
			 * probeManager.startMonitoringProcess();
			 */

			IamFeature iamFeature = new IamFeature();

			// OAuthClientRequest request = OAuthClientRequest
			// .authorizationProvider("https://keystone.ifca.es:5000/v3/OS-FEDERATION/identity_providers/indigo-dc/protocols/oidc/auth/")
			// .setClientId("to-be-registered-to-iam")
			//// .setRedirectURI("http://www.example.com/redirect")
			// .buildQueryMessage();

			OAuthClientRequest request = OAuthClientRequest
					.authorizationLocation(
							"https://keystone.ifca.es:5000/v3/OS-FEDERATION/identity_providers/indigo-dc/protocols/oidc/auth/")
					.setClientId("dffcbaa0-1f58-4ea0-89f9-6077f7725784").buildQueryMessage();

			// TokenRequestBuilder oauthrequest = new OAuthClientRequest
			// .TokenRequestBuilder(PropertiesManager.getProperty(iamFeature.getIamLocation())
			// .setUsername(PropertiesManager.getProperty(iamFeature.getIamUsername())
			// .setPassword(PropertiesManager.getProperty(iamFeature.getIamPassword())
			// .setGrantType(GrantType.CLIENT_CREDENTIALS)
			// .setScope("openid scope")
			// .buildQueryMessage())));

			OAuthClient client = new OAuthClient(new URLConnectionClient());

			response = client.accessToken(request);
			OAuthToken oauthToken = response.getOAuthToken();
			String accessToken = response.getAccessToken();

			System.out.println("Access token: " + response.getAccessToken());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		} catch (OAuthProblemException e) {
			e.printStackTrace();
		}
		return response;
	}

	public static void main(String[] args) {

		System.out.println(getJsonToken());

	}

}
