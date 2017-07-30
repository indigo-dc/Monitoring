package com.indigo.zabbix.utils;

import com.indigo.zabbix.utils.beans.KeystoneScopedTokenRequest;
import com.indigo.zabbix.utils.beans.OpenstackProjectsInfo;
import feign.Response;
import java.util.Collection;
import java.util.List;
import java.util.Map;



/**
 * Created by jose on 4/04/17.
 */
public class KeystoneClient {

  public static final String TOKEN_RESULT_HEADER = "X-Subject-Token";
  private final KeystoneTokenProvider keystoneClient;

  public KeystoneClient(String location) {
	  if (location.endsWith("/v3")) {
		location = location.replace("/v3", "");
	  } else {
		throw new IllegalArgumentException("Error generating Keystone client.\nOpenstack endpoint <" + location + "> provided but only v3 endpoints are supported");  
	  }
		this.keystoneClient = ProbeClientFactory.getClient(KeystoneTokenProvider.class, location);
  }

  /**
   * Constructor used for testing.
   * @param provider Client for accessing keystone.
   */
  public KeystoneClient(KeystoneTokenProvider provider) {
    this.keystoneClient = provider;
  }

  /**
   * Gets an unscoped token from an access token provided by the IAM.
   * @param accessToken The access token proviced by the IAM.
   * @return A keystone unscoped token.
   */
  public String getUnscopedToken(String accessToken, String provider, String protocol) {
    Response tokenInfo = keystoneClient.getKeystoneToken(accessToken, provider, protocol);
    return getTokenHeader(tokenInfo);
  }

  /**
   * Gets a list of available projects.
   * @param unscopedToken A valid Keystone unscoped token.
   * @return A list of projects available on the server.
   */
  public List<OpenstackProjectsInfo.Project> getProjects(String unscopedToken) {
    return keystoneClient.getProjects(unscopedToken).getProjects();
  }

  /**
   * Gets a scoped token for a project in Keystone.
   * @param accessToken The access token provided by the IAM.
   * @param projectName The project name to scope.
   * @return The scoped Keystone token.
   */
  public String getScopedToken(String accessToken, String projectName, String provider,  String protocol) {

    if (projectName != null && accessToken != null) {

      String unscopedToken = getUnscopedToken(accessToken, provider, protocol);

      if (unscopedToken != null) {
        List<OpenstackProjectsInfo.Project> projects = getProjects(unscopedToken);

        OpenstackProjectsInfo.Project found = null;

        if (projects != null) {
          found = projects.stream().filter(project -> projectName.equalsIgnoreCase(project.getName()))
              .findFirst().orElse(null);
        }

        if (found != null) {

          return getTokenHeader(keystoneClient.getScopedToken(
              new KeystoneScopedTokenRequest(unscopedToken, found.getId())));

        }
      }

    }

    return null;
  }

  private String getTokenHeader(Response response) {

    Map<String, Collection<String>> tokenHeaders = response.headers();

    Collection<String> tokenList = tokenHeaders.get(TOKEN_RESULT_HEADER);

    if (tokenList != null && !tokenList.isEmpty()) {
      return tokenList.iterator().next();
    } else {
      return null;
    }

  }
}
