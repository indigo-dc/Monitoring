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

import com.indigo.zabbix.utils.ProbeClientFactory;
import com.indigo.zabbix.utils.PropertiesManager;

import org.indigo.occiprobe.openstack.beans.CmdbResponse;
import org.indigo.occiprobe.openstack.beans.ServiceType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The CmdbClient class is in charge of the interactions between the probe and
 * the CMDB component. Such component provides information about the available
 * providers, such as their name, location, list of services provided, etc...
 * 
 * @author Atos
 *
 */
public class CmdbClient {
  public static final String OPENSTACK_TYPE = "eu.egi.cloud.vm-management.openstack";
  //private Client client = null;

  private CmdbFeignClient cmdbClient = null;

  private String cmdbUrl;
  
  /**
   * It constructs an object of the CmdbClient type, retrieving certain properties
   * and initializing a Jersey client.
   */
  public CmdbClient() {
    // Retrieve properties
    cmdbUrl = PropertiesManager.getProperty(OcciProbeTags.CMDB_URL);
    cmdbClient = ProbeClientFactory.getClient(CmdbFeignClient.class, cmdbUrl);
  }
  
  /**
   * This is a constructor for unit testing purposes.
   * @param mock Mock of the Jersey Client class
   */
  public CmdbClient(CmdbFeignClient mock) {
    this.cmdbClient = mock;
  }
  
  /**
   * This method access the CMDB service in order to retrieve the available 
   * data from a Cloud Provider (i.e. its location, provided services, etc.)
   * @param providerId Represents the identifier of the Cloud provider
   * @return An object with all the information retrieved
   */
  public CloudProviderInfo getProviderData(String providerId) {
    // Call to CMDB API
    
    //System.out.println(message);
    
    // Retrieve the services list
    JsonElement jelement = cmdbClient.providerInfo(providerId);
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
  
    String identityProvider = "indigo-dc";
    String protocol = "oidc";
    String imageId = null;
    String osFlavour = null;
    String networkId = null;
    
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
        
        JsonElement oidcElement = currentData.get("oidc_config");
        if (oidcElement != null) {
          JsonObject oidcConfig = oidcElement.getAsJsonObject();
          
          identityProvider = oidcConfig.get("provider_id").getAsString();
          
        }
        
      } else if (currentServiceType.equalsIgnoreCase(OPENSTACK_TYPE)) {
        keystoneEndpoint = currentEndpoint;
        type = CloudProviderInfo.OPENSTACK;
      }
    }
    
    CloudProviderInfo myProvider = new CloudProviderInfo(providerId, occiEndpoint,
        keystoneEndpoint, type, isMonitored, isBeta, isProduction, identityProvider, protocol,
                                                            imageId, osFlavour, networkId);
    
    return myProvider;
  }

  /**
   * Get a list of OCCI providers.
   * @return List of providers with and OCCI endpoint.
   */
  public List<CloudProviderInfo> getFeasibleProvidersInfo() {

    CmdbResponse response = cmdbClient.services();
    if (response != null && response.getRows() != null) {

      List<ServiceType> services = response.getRows();

      return services.stream()

          // First filter only services which are of type OpenStack
          .filter(service ->
         service.getDoc() != null && service.getDoc().getData() != null
            && OPENSTACK_TYPE.equals(service.getDoc().getData().getServiceType()))

          // For any of such services, get the full information of the associated provider
          .map(service -> getProviderData(service.getDoc().getData().getProviderId()))

          // Get only the ones with OCCI information
          .filter(serviceInfo -> serviceInfo.getOcciEndpoint() != null)

          // Collect results
          .collect(Collectors.toList());

    } else {
      return new ArrayList<>();
    }

  }


}
