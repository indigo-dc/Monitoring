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

package org.indigo.occiprobe.openstack;

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
  public ProviderInfo getProviderData(String providerId) {
    // Call to CMDB API
    WebTarget target = client.target(cmdbUrl + "/provider/id/" + providerId);
    Invocation.Builder invocationBuilder = target.request();
    Response response = invocationBuilder.get();
    String message = response.readEntity(String.class);
    
    System.out.println(message);
    
    // Retrieve the providers list
    JsonElement jelement = new JsonParser().parse(message);
    JsonObject parsedRes = jelement.getAsJsonObject();
    
    return null;
  }
  
  /**
   * Temporary main for testing.
   * @param args Typical arguments
   */
  public static void main(String[] args) {
    CmdbClient myClient = new CmdbClient();
    String[] providers = myClient.getProvidersList();
    myClient.getProviderData(providers[0]);
  }
}
