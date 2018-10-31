package com.indigo.zabbix.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.indigo.zabbix.utils.beans.CmdbResponse;
import com.indigo.zabbix.utils.beans.DocDataType;
import com.indigo.zabbix.utils.beans.ServiceInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The CmdbClient class is in charge of the interactions between the probe and the CMDB component.
 * Such component provides information about the available providers, such as their name, location,
 * list of services provided, etc...
 *
 * @author Atos
 */
public class CmdbClient {
  public static final String OPENSTACK_TYPE = "eu.egi.cloud.vm-management.openstack";
  // private Client client = null;

  protected CmdbFeignClient cmdbClient = null;

  protected String cmdbUrl;

  /**
   * It constructs an object of the CmdbClient type, retrieving certain properties and initializing
   * a Jersey client.
   */
  public CmdbClient() {
    // Retrieve properties
    this(PropertiesManager.getProperty(ProbesTags.CMDB_URL));
  }

  public CmdbClient(String cmdbUrl) {
    this.cmdbUrl = cmdbUrl;
    cmdbClient = ProbeClientFactory.getClient(CmdbFeignClient.class, cmdbUrl);
  }

  /**
   * This is a constructor for unit testing purposes.
   *
   * @param mock Mock of the Jersey Client class
   */
  public CmdbClient(CmdbFeignClient mock) {
    this.cmdbClient = mock;
  }

  protected CloudProviderInfo deserializeProviderInfo(String providerId, JsonElement jelement) {
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

          protocol = oidcConfig.get("protocol").getAsString();
        }

        JsonElement monitoringInfoElement = currentData.get("occi_monitoring");
        if (monitoringInfoElement != null) {
          JsonObject monitoringInfo = monitoringInfoElement.getAsJsonObject();

          imageId = monitoringInfo.get("image_id").getAsString();
          osFlavour = monitoringInfo.get("os_flavour").getAsString();
          networkId = monitoringInfo.get("network_id").getAsString();
        }

      } else if (currentServiceType.equalsIgnoreCase(OPENSTACK_TYPE)) {
        keystoneEndpoint = currentEndpoint;
        type = CloudProviderInfo.OPENSTACK;
      }
    }

    CloudProviderInfo myProvider =
        new CloudProviderInfo(
            providerId,
            occiEndpoint,
            keystoneEndpoint,
            type,
            isMonitored,
            isBeta,
            isProduction,
            identityProvider,
            protocol,
            imageId,
            osFlavour,
            networkId);
    return myProvider;
  }

  /**
   * This method access the CMDB service in order to retrieve the available data from a Cloud
   * Provider (i.e. its location, provided services, etc.)
   *
   * @param providerId Represents the identifier of the Cloud provider
   * @return An object with all the information retrieved
   */
  public CloudProviderInfo getProviderData(String providerId) {
    // Call to CMDB API
    // Retrieve the services list
    return deserializeProviderInfo(providerId, cmdbClient.providerInfo(providerId));
  }

  /**
   * Get a list of OCCI providers.
   *
   * @return List of providers with and OCCI endpoint.
   */
  public List<CloudProviderInfo> getFeasibleProvidersInfo() {

    CmdbResponse response = cmdbClient.services();
    if (response != null && response.getRows() != null) {

      List<ServiceInfo> services = response.getRows();

      return services
          .stream()

          // First filter only services which are of type OpenStack
          .filter(
              service ->
                  service.getDoc() != null
                      && service.getDoc().getData() != null
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

  /**
   * Returns a list of service of given type from the CMDB.
   *
   * @param type The type of the service to filter.
   * @return A list of service information filtered by type.
   */
  public List<ServiceInfo> getServiceList(DocDataType.ServiceType type) {
    List<ServiceInfo> services = cmdbClient.services().getRows();
    if (type != null) {
      services =
          services
              .stream()
              .filter(
                  service ->
                      service.getDoc() != null
                          && service.getDoc().getData() != null
                          && type.equals(service.getDoc().getData().getServiceType()))
              .collect(Collectors.toList());
    }
    return services;
  }
}
