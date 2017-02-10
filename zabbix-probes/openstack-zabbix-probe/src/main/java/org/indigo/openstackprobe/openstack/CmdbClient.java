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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The CmdbClient class is in charge of the interactions between the probe and the CMDB component.
 * Such component provides information about the available providers, such as their name, location,
 * list of services provided, etc...
 * 
 * @author Reply
 *
 */
public class CmdbClient {
  private Client client = null;
  private String cmdbUrl;
  private static final String SERVICE_TYPE = "eu.egi.cloud.vm-management.openstack";
  private static final String NOVA_DEFUALT_PORT = "8774";
  private static final String IDENTITY_DEFAULT_PORT = "5000";
  private static final String VERSION_2 = "v2.0";
  private static final String VERSION_3 = "v3";

  /**
   * It constructs an object of the CmdbClient type, retrieving certain properties and initializing
   * a Jersey client.
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
   * 
   * @param mock
   *          Mock of the Jersey Client class
   */
  public CmdbClient(Client mock) {
    client = mock;
  }

  /**
   * Using the created Jersey client, it invokes the CMDB REST API in order to retrieve the full
   * list of Cloud providers which are currently available.
   * 
   * @return Strings array with the identifiers of the providers found.
   */
  public String[] getProvidersList() {
    // Call to CMDB API
    WebTarget target = client.target(cmdbUrl + "/provider/list");
    Invocation.Builder invocationBuilder = target.request();
    Response response = invocationBuilder.get();
    String message = response.readEntity(String.class);

    // System.out.println(message);

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
  
  public String[] getImageList() {
    // Call to CMDB API
    WebTarget target = client.target(cmdbUrl + "/image/list");
    Invocation.Builder invocationBuilder = target.request();
    Response response = invocationBuilder.get();
    String message = response.readEntity(String.class);

    // Retrieve the providers list
    JsonElement jelement = new JsonParser().parse(message);
    JsonObject parsedRes = jelement.getAsJsonObject();
    JsonArray listArray = parsedRes.getAsJsonArray("rows");

    ArrayList<String> imageList = new ArrayList<>();
    Iterator<JsonElement> myIter = listArray.iterator();
    while (myIter.hasNext()) {
      JsonObject currentResource = myIter.next().getAsJsonObject();
      JsonObject valueObject = currentResource.getAsJsonObject("value");
      String imageJsonId = valueObject.get("image_id").getAsString();
      
      String imageId = imageJsonId!=null ? imageJsonId : currentResource.get("id").getAsString();
      imageList.add(imageId);
    }

    // Prepare the result
    imageList.trimToSize();
    String[] resultList = new String[imageList.size()];
    imageList.toArray(resultList);

    return resultList;
  }

  /**
   * This method access the CMDB service in order to retrieve the available data from a Cloud
   * Provider (i.e. its location, provided services, etc.)
   * 
   * @param providerId
   *          Represents the identifier of the Cloud provider
   * @return An object with all the information retrieved
   */
  public CloudProviderInfo getProviderData(String providerId) {
    // Call to CMDB API
    String providerUrl =
        cmdbUrl + "/provider/id/" + providerId + "/has_many/services?include_docs=true";
    WebTarget target = client.target(providerUrl);
    Invocation.Builder invocationBuilder = target.request();
    Response response = invocationBuilder.get();
    String message = response.readEntity(String.class);

    // System.out.println(message);

    // Retrieve the services list
    JsonElement jelement = new JsonParser().parse(message);
    JsonObject parsedRes = jelement.getAsJsonObject();
    JsonArray listArray = parsedRes.getAsJsonArray("rows");
    if (listArray.isJsonNull() || listArray.size() == 0) {
      return null;
    }

    String novaEndpoint = null;
    String keystoneEndpoint = null;
    int type = CloudProviderInfo.OPENSTACK;
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

      // the service type represents the filter this class works on
      if (currentServiceType.equalsIgnoreCase(SERVICE_TYPE
      // "eu.egi.cloud.vm-management.occi"
      )) {
        keystoneEndpoint = currentEndpoint;

        novaEndpoint = currentEndpoint.contains(VERSION_2)
            ? currentEndpoint.replace(IDENTITY_DEFAULT_PORT + VERSION_2, NOVA_DEFUALT_PORT)
            : currentEndpoint.replace(IDENTITY_DEFAULT_PORT + VERSION_3, NOVA_DEFUALT_PORT);
        JsonElement currentBeta = currentData.get("beta");
        JsonElement currentProduction = currentData.get("in_production");
        JsonElement currentMonitored = currentData.get("node_monitored");

        // Retrieve the rest of info from the Nova service
        if (currentBeta != null && currentBeta.getAsString().equalsIgnoreCase("Y")) {
          isBeta = true;
        }
        if (currentMonitored != null && currentMonitored.getAsString().equalsIgnoreCase("Y")) {
          isMonitored = true;
        }
        if (currentProduction != null && currentProduction.getAsString().equalsIgnoreCase("Y")) {
          isProduction = true;
        }
      } else if (currentServiceType.equalsIgnoreCase(SERVICE_TYPE
      // "eu.egi.cloud.vm-management.occi"
      )) {
        keystoneEndpoint = currentEndpoint;
        type = CloudProviderInfo.OPENNEBULA;
      }
    }

    CloudProviderInfo myProvider = new CloudProviderInfo(providerId, novaEndpoint, keystoneEndpoint,
        type, isMonitored, isBeta, isProduction);

    return myProvider;
  }

  /**
   * It makes use of the methods for listing providers and for getting individual information in
   * order to perform a filtering of providers, selecting only those suitable for the probe at this
   * stage: OpenStack providers, in production and with monitoring.
   * 
   * @return An ArrayList including info about the selected providers.
   */
  public ArrayList<CloudProviderInfo> getFeasibleProvidersInfo() {
    // Create the resulting list object
    ArrayList<CloudProviderInfo> myResult = new ArrayList<CloudProviderInfo>();

    // First, retrieve the whole list of providers
    String[] providersList = getProvidersList();

    // Then, iterate all the providers and select
    for (int i = 0; i < providersList.length; i++) {
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
   * 
   * @param args
   *          Typical arguments
   */
  public static void main(String[] args) {
    CmdbClient myClient = new CmdbClient();
    String[] providers = myClient.getProvidersList();

    CloudProviderInfo myInfo = myClient.getProviderData(providers[450]);
    System.out.println("Provider: " + myInfo.getProviderId());
    System.out.println("COMPUTE URL: " + myInfo.getNovaEndpoint());
    System.out.println("Keystone URL: " + myInfo.getKeystoneEndpoint());
    System.out.println("Provider Type: " + myInfo.getCloudType());
    System.out.println("Is Monitored? " + myInfo.getIsMonitored());
    System.out.println("Is Beta? " + myInfo.getIsBeta());
    System.out.println("Is Production? " + myInfo.getIsProduction());

    /*
     * ArrayList<CloudProviderInfo> myList = myClient.getFeasibleProvidersInfo();
     * System.out.println("Number of providers: " + myList.size()); Iterator<CloudProviderInfo>
     * myIter = myList.iterator(); while (myIter.hasNext()) { System.out.println("Provider: " +
     * myIter.next().getProviderId()); }
     */
  }
}