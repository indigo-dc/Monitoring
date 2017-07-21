package com.indigo.zabbix.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.indigo.zabbix.utils.beans.KeystoneScopedTokenRequest;
import com.indigo.zabbix.utils.beans.OpenstackProjectsInfo;

import feign.Response;

/**
 * Created by jose on 4/04/17.
 */
public class KeystoneClient {

  public static final String TOKEN_RESULT_HEADER = "X-Subject-Token";
  private final KeystoneTokenProvider keystoneClient;

  public KeystoneClient(String location) {
//	  if(location.contains("v3")||location.contains("v2.0"))
		  location= location.contains("v3")  ? location.replace("v3","")  : location.contains("v2.0")  ? location.replace("v2.0","") : location;
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
  public String getUnscopedToken(String accessToken) {
	  
	   Response tokenInfo = keystoneClient.getKeystoneTokenOIDC(accessToken);
		    
		    if(tokenInfo.headers().get(TOKEN_RESULT_HEADER)==null)
		    	tokenInfo= keystoneClient.getKeystoneTokenIAMOIDC(accessToken);
		    
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
  public String getScopedToken(String accessToken, String projectName) {

    if (projectName != null && accessToken != null) {
    	
      String unscopedToken = getUnscopedToken(accessToken);

      if (unscopedToken != null) {
        List<OpenstackProjectsInfo.Project> projects = getProjects(unscopedToken);

        OpenstackProjectsInfo.Project found = null;

        if (projects != null) {
          found = projects.stream().filter(project -> projectName.equals(project.getName()))
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
