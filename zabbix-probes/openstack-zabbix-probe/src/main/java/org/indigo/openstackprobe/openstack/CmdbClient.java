/**
Copyright 2016 ATOS SPAIN S.A.

Licensed under the Apache License, Version 2.0 (the License);
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Authors Contact:
Francisco Javier Nieto. Atos Research and Innovation, Atos SPAIN SA
@email francisco.nieto@atos.net
**/

package org.indigo.openstackprobe.openstack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;

import java.util.ArrayList;
import java.util.Iterator;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * The CmdbClient class is in charge of the interactions between the probe and
 * the CMDB component. Such component provides information about the available
 * providers, such as their name, location, list of services provided, etc...
 * 
 * @author Atos
 *
 */
public class CmdbClient {
  private Client client = null;
  private String cmdbUrl;
  
  /**
   * It constructs an object of the CmdbClient type, retrieving certain properties
   * and initializing a Jersey client.
   */
  public CmdbClient() {
    // Retrieve properties
    PropertiesManager myProp = new PropertiesManager();
    cmdbUrl = myProp.getProperty(PropertiesManager.CMDB_URL);
    
    // Create the Client
    ClientConfig cc = new ClientConfig();
    client = JerseyClientBuilder.newClient(cc);
  }
  
  /**
   * This is a constructor for unit testing purposes.
   * @param mock Mock of the Jersey Client class
   */
  public CmdbClient(Client mock) {
    client = mock;
  }
  
  /**
   * Using the created Jersey client, it invokes the CMDB REST API in 
   * order to retrieve the full list of Cloud providers which are
   * currently available.
   * @return Strings array with the identifiers of the providers found.
   */
  public String[] getProvidersList() {
    // Call to CMDB API
    WebTarget target = client.target(cmdbUrl + "/provider/list");
    Invocation.Builder invocationBuilder = target.request();
    Response response = invocationBuilder.get();
    String message = response.readEntity(String.class);    
    
    //System.out.println(message);
    
    // Retrieve the providers list
    JsonElement jelement = new JsonParser().parse(message);
    JsonObject parsedRes = jelement.getAsJsonObject();
    JsonArray listArray = parsedRes.getAsJsonArray("rows");
    
    ArrayList<String> providersList = new ArrayList<String>();
    Iterator<JsonElement> myIter = listArray.iterator();
    while (myIter.hasNext()) {
      JsonObject currentResource = myIter.next().getAsJsonObject();
      String providerId = currentResource.get("id").getAsString();
      providersList.add(providerId);
    }
    
    // Prepare the result
    providersList.trimToSize();
    String[] resultList = new String[providersList.size()];
    providersList.toArray(resultList);
    
    return resultList;
  }
  
  /**
   * This method access the CMDB service in order to retrieve the available 
   * data from a Cloud Provider (i.e. its location, provided services, etc.)
   * @param providerId Represents the identifier of the Cloud provider
   * @return An object with all the information retrieved
   */
  public CloudProviderInfo getProviderData(String providerId) {
    // Call to CMDB API
    String providerUrl = cmdbUrl + "/provider/id/" + providerId 
        + "/has_many/services?include_docs=true";
    WebTarget target = client.target(providerUrl);
    Invocation.Builder invocationBuilder = target.request();
    Response response = invocationBuilder.get();
    String message = response.readEntity(String.class);
    
    //System.out.println(message);
    
    // Retrieve the services list
    JsonElement jelement = new JsonParser().parse(message);
    JsonObject parsedRes = jelement.getAsJsonObject();
    JsonArray listArray = parsedRes.getAsJsonArray("rows");
    if (listArray.isJsonNull() || listArray.size() == 0) {
      return null;
    }
    
    String occiEndpoint = null;
    String keystoneEndpoint = null;
    int type = CloudProviderInfo.OPENNEBULA;
    boolean isMonitored = false;
    boolean isBeta = false;
    boolean isProduction = false;    
    
    Iterator<JsonElement> myIter = listArray.iterator();
    while (myIter.hasNext()) {
      JsonObject currentResource = myIter.next().getAsJsonObject();
      JsonObject currentDoc = currentResource.get("doc").getAsJsonObject();
      JsonObject currentData = currentDoc.get("data").getAsJsonObject();
            
      String currentServiceType = currentData.get("service_type").getAsString();      
      String currentType = currentData.get("type").getAsString();
      JsonElement jsonEndpoint = currentData.get("endpoint"); 
      String currentEndpoint = null;
      if (jsonEndpoint == null || jsonEndpoint.isJsonNull()) {
        return null;
      }
      currentEndpoint = jsonEndpoint.getAsString();
      
      if (currentServiceType.equalsIgnoreCase("eu.egi.cloud.vm-management.occi")) {
        occiEndpoint = currentEndpoint;
        JsonElement currentBeta = currentData.get("beta");
        JsonElement currentProduction = currentData.get("in_production");
        JsonElement currentMonitored = currentData.get("node_monitored");
        
        // Retrieve the rest of info from the OCCI service
        if (currentBeta != null && currentBeta.getAsString().equalsIgnoreCase("Y")) {
          isBeta = true;
        }
        if (currentMonitored != null && currentMonitored.getAsString().equalsIgnoreCase("Y")) {
          isMonitored = true;
        }
        if (currentProduction != null && currentProduction.getAsString().equalsIgnoreCase("Y")) {
          isProduction = true;
        }
      } else if (currentServiceType.equalsIgnoreCase("eu.egi.cloud.vm-management.openstack")) {
        keystoneEndpoint = currentEndpoint;
        type = CloudProviderInfo.OPENSTACK;
      }
    }
    
    CloudProviderInfo myProvider = new CloudProviderInfo(providerId, occiEndpoint, 
        keystoneEndpoint, type, isMonitored, isBeta, isProduction);
    
    return myProvider;
  }
  
  /**
   * It makes use of the methods for listing providers and for getting individual information
   * in order to perform a filtering of providers, selecting only those suitable for the probe
   * at this stage: OpenStack providers, in production and with monitoring.
   * @return An ArrayList including info about the selected providers.
   */
  public ArrayList<CloudProviderInfo> getFeasibleProvidersInfo() {
    // Create the resulting list object
    ArrayList<CloudProviderInfo> myResult = new ArrayList<CloudProviderInfo>();
    
    // First, retrieve the whole list of providers
    String[] providersList = getProvidersList();
    
    // Then, iterate all the providers and select
    for (int i = 0; i < providersList.length; i ++) {
      // Retrieve all the info
      CloudProviderInfo currentInfo = getProviderData(providersList[i]);
      
      // Now check it is compliant with our requirements
      if (currentInfo != null) {
        int cloudType = currentInfo.getCloudType();
        boolean isMonitored = currentInfo.getIsMonitored();
        boolean isProduction = currentInfo.getIsProduction();
        if (cloudType == CloudProviderInfo.OPENSTACK) {
          myResult.add(currentInfo);
        }
      }
    }
    
    return myResult;
  }
  
  /**
   * Temporary main for testing.
   * @param args Typical arguments
   */
  public static void main(String[] args) {
    CmdbClient myClient = new CmdbClient();
    String[] providers = myClient.getProvidersList();
  
    CloudProviderInfo myInfo = myClient.getProviderData(providers[0]);
    System.out.println("Provider: " + myInfo.getProviderId());
    System.out.println("OCCI URL: " + myInfo.getOcciEndpoint());
    System.out.println("Keystone URL: " + myInfo.getKeystoneEndpoint());
    System.out.println("Provider Type: " + myInfo.getCloudType());
    System.out.println("Is Monitored? " + myInfo.getIsMonitored());
    System.out.println("Is Beta? " + myInfo.getIsBeta());
    System.out.println("Is Production? " + myInfo.getIsProduction());
    
    /*
    ArrayList<CloudProviderInfo> myList = myClient.getFeasibleProvidersInfo();
    System.out.println("Number of providers: " + myList.size());
    Iterator<CloudProviderInfo> myIter = myList.iterator();
    while (myIter.hasNext()) {
      System.out.println("Provider: " + myIter.next().getProviderId());
    }
    */
  }
}
