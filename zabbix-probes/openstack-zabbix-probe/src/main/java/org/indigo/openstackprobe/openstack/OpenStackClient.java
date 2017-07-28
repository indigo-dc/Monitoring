/**
 * Copyright 2017 Reply Italy Licensed under the Apache License, Version 2.0 (the License); you may
 * not use this file except in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * </p>
 **/

package org.indigo.openstackprobe.openstack;

import com.indigo.zabbix.utils.KeystoneClient;
import com.indigo.zabbix.utils.ProbesTags;
import com.indigo.zabbix.utils.PropertiesManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.client.IOSClientBuilder.V2;
import org.openstack4j.api.client.IOSClientBuilder.V3;
import org.openstack4j.api.compute.ComputeService;
import org.openstack4j.api.compute.FlavorService;
import org.openstack4j.api.compute.ServerService;
import org.openstack4j.api.image.ImageService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.model.image.Image;
import org.openstack4j.model.network.Network;
import org.openstack4j.openstack.OSFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;



/**
 * It takes care of the interactions to be performed with Cloud Providers whose base platform is
 * OpenStack. It requires additional interactions with the Keystone component, in order to get
 * authorization tokens. This class is able to perform basic operations on VMs: create, inspect and
 * delete.
 * 
 * @author Reply
 *
 */
public class OpenStackClient {
  private Client client = null;
  private String baseKeystoneUrl;
  private String openStackUser;
  private String openStackPwd;
  private String providerId;

  private static final String POWER_STATE_ACTIVE = "1";
  private String flavorId;
  private String imageId;
  private Flavor flavor;
  private Image image;
  private List<? extends Image> images = new ArrayList<>();
  private List<? extends Flavor> flavors = new ArrayList<>();
  private List<? extends Server> servers = new ArrayList<>();
  private static final String INSTANCE_NAME = OpenstackProbeTags.INSTANCE_NAME;
  private OSClientV3 osClientV3;
  private OSClient osClientV2;
  private Object osClient;
  private V2 myKeystoneClientV2;
  private V3 keystoneclientV3;
  private Token token;
  private ComputeService computeService;
  private FlavorService flavorService;
  private ImageService imageService;
  private String tenantName;
  private Map<String, Integer> resultMap;
  private Map<Server.Status, Server> serverCreation;
  private Server server;
  private ServerService serverService;
  private OpenstackConfiguration osconfig;
  String vmId;
  OpenstackComponent component;
  String tokenId;
  private CheckForProviderDifference checkForProviderDifference = new CheckForProviderDifference();

  private static final String HTTP_RESPONSE_CODE = "httpCode";
  private static final String AVAILABILITY_STATUS = "availability";
  private static final String TIME_MS = " time in milliseconds: ";
  private static final String TOTAL_CREATION_TIME =
      "Total elapsed http request/response for creating the instance: ";

  private static final Logger log = LogManager.getLogger(OpenStackClient.class);

  /**
   * Main constructor of the OpenStackOcciClient class. It retrieves some information from the
   * properties files in order to create and configure the client which will connect to the remote
   * OCCI API of a cloud provider.
   * @param keystoneLocation String with the full location of Keystone [IP:Port]
   * @param providerName String with the identifier of the Cloud Provider
   */
  public OpenStackClient(String accessToken, String keystoneLocation, String providerName) {

    if (!Boolean
        .parseBoolean(PropertiesManager.getProperty(OpenstackProbeTags.IAM_AUTHENTICATION))) {
      log.info("getting openstack credentials from property file for provider " + providerName);
      try {
        PropertiesManager.loadProperties(OpenstackProbeTags.CONFIG_FILE);
      } catch (IOException e) {
        log.debug("Unable to load the file: " + OpenstackProbeTags.CONFIG_FILE);
      }

      checkCredentials(providerName);
      myKeystoneClientV2 = OSFactory.builderV2();
      buildClientV2(myKeystoneClientV2);

    } else {
      // use the Client IAM to authenticate to openstack instance
      // Retrieve properties
      String project = checkForProviderDifference.checkForOpenstackProject(providerName);

      String identityProvider = PropertiesManager.getProperty(ProbesTags.IDENTITY_PROVIDER);

      String openstackToken = new KeystoneClient(keystoneLocation).getScopedToken(accessToken,
          project, identityProvider, PropertiesManager.getProperty(ProbesTags.IAM_PROTOCOL));

      if (openstackToken == null && Boolean
          .parseBoolean(PropertiesManager.getProperty(OpenstackProbeTags.PROVIDERS_EXCEPTIONS))) {
        openstackToken = new KeystoneClient(keystoneLocation).getScopedToken(accessToken, project,
            identityProvider, "iam" + PropertiesManager.getProperty(ProbesTags.IAM_PROTOCOL));
      }

      baseKeystoneUrl = keystoneLocation;
      tokenId = openstackToken;
      keystoneclientV3 = OSFactory.builderV3();
      buildClientV3(keystoneclientV3, project);
    }

    providerId = providerName;
  }

  /**
   * Build the client V3.
   * @param keystoneclient kesytsone client
   * @param project project
   */
  private void buildClientV3(V3 keystoneclient, String project) {
    setCertificate();
    // Create the Clients
    ClientConfig cc = new ClientConfig();
    client = JerseyClientBuilder.newClient(cc);
    osClient = getOsAuthIam(keystoneclient, project);
  }

  /**
   * Build ClientV2.
   * @param keystoneclient keystone cleint os4j
   */
  private void buildClientV2(V2 keystoneclient) {
    setCertificate();
    // Create the Clients
    ClientConfig cc = new ClientConfig();
    client = JerseyClientBuilder.newClient(cc);
    osClient = getOsAuth(myKeystoneClientV2);
  }

  /**
   * Set the the certification path and properties.
   */
  private void setCertificate() {
    System.setProperty("jsse.enableSNIExtension", "false");
    String certificatesTrustStorePath =
        PropertiesManager.getProperty(OpenstackProbeTags.JAVA_KEYSTORE);
    System.setProperty("javax.net.ssl.trustStore", certificatesTrustStorePath);
    System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
  }

  /**
   * Constructor to be used for automatic testing purposes only.
   * @param component component class
   */
  public OpenStackClient(OpenstackComponent component) {

    client = component.getMockClient();
    myKeystoneClientV2 = component.getMockKeystone();
    flavor = component.getMockFlavor();
    image = component.getMockImage();
    osClient = component.getOsClientMocked();
    // token = component.getTokenId();
    baseKeystoneUrl = component.getBaseKeystoneUrlMocked();
    openStackUser = component.getOpenStackUserMocked();
    openStackPwd = component.getOpenStackPwdMocked();
    tenantName = component.getTenantNameMoked();
    images = component.getImagesMocked();
    flavors = component.getFlavorsMocked();
    computeService = component.getComputeserviceMocked();
    flavorService = component.getFlavorServiceMocked();
    imageService = component.getImageServiceMocked();
    serverCreation = component.getServerCreationMocked();
    resultMap = component.getResultMapMocked();
    serverService = component.getServerServiceMocked();
    server = component.getServerMocked();
    servers = component.getServersMocks();
    vmId = component.getServerMocked().getId();
    // tokenId = component.getTokenId();
  }

  /**
   * It asks the token to Openstack.
   * @param keystoneclient keystoneclient os4j
   * @param project os tenant
   * @return OSClientV3
   */
  private OSClientV3 getOsAuthIam(V3 keystoneclient, String project) {

    osClientV3 = keystoneclient.endpoint(baseKeystoneUrl).token(tokenId)
        .scopeToProject(Identifier.byName(project), Identifier.byName("default")).authenticate();
    return osClientV3;
  }

  /**
   * To maintain back compatibility just make sure to authentuicate with legacy method.
   * @param keystoneclient keystoneclient os4j
   * @return OSClient osclient
   */
  private OSClient getOsAuth(V2 keystoneclient) {

    osClientV2 = myKeystoneClientV2.endpoint(baseKeystoneUrl)
        .credentials(openStackUser, openStackPwd).tenantName(tenantName).authenticate();
    return osClientV2;
  }

  /**
   * Checks whether there are credentials of openstack.
   * @param providerName provider cloud
   */
  protected void checkCredentials(String providerName) {

    if (OpenstackConfiguration.zone != null && OpenstackConfiguration.zone.contains("test")) {
      osconfig = new OpenstackConfiguration("testoszone.yml");
    } else {
      osconfig = new OpenstackConfiguration();
    }
    for (CloudProvidersZone zone : osconfig.getMonitoringZones().getCloudProvidersZones()) {
      if (zone.getName().equalsIgnoreCase(providerName) && (zone.getPassword() != null
          || zone.getTenant() != null || zone.getUsername() != null)) {
        openStackUser = zone.getUsername();
        openStackPwd = zone.getPassword();
        tenantName = zone.getTenant();
      }
    }

    if (openStackPwd == null || tenantName == null || openStackUser == null) {
      throw new IllegalArgumentException("Unable to schedule task for the provider " + providerName
          + " because of errors or missing data into properties file");
    }
  }

  /**
   * Constructor to be used for automatic testing purposes only. 
   * @param mockClient mock
   * @param mockKeystone mock
   * @param mockFlavorId mock
   * @param mockImageId mock for test
   */
  public OpenStackClient(Client mockClient, V2 mockKeystone, String mockFlavorId,
      String mockImageId) {
    client = mockClient;
    myKeystoneClientV2 = mockKeystone;
    flavorId = mockFlavorId;
    imageId = mockImageId;
  }

  protected List<? extends Flavor> getInternalFlavor(Object osClient) {

    return (osClient.equals(osClientV3)) ? osClientV3.compute().flavors().list()
        : osClientV2.compute().flavors().list();

  }

  /**
   * Retrieves the flavors from the providers. It checks whether there is any flavor called small,
   * otherwise gets the first.
   * @return the Id of the flavor
   */
  public String getFlavor() {
    if (flavors.isEmpty()) {
      flavors = getInternalFlavor(osClient);
    }
    for (Flavor flavor : flavors) {
      if (flavor.getName().toLowerCase().contains("small")) {
        return flavor.getId();
      }
    }
    if (!flavors.isEmpty()) {
      return flavors.get(0).getId();
    }
    throw new NotFoundException("Unable to find any flavor in Openstack");
  }

  protected List<? extends Image> getInternalOsImage(Object osClient) {

    List<? extends Image> images =
        (osClient.equals(osClientV3)) ? osClientV3.images().list() : osClientV2.images().list();

    return images;
  }

  /**
   * Checks inside CMDB for all the images IDs and checks whether it belongs to the examined
   * provider or not in order to use the correct one.
   * @return id of the Image used for creating the instance.
   */
  public String getOsImage() {

    if (images.isEmpty()) {
      images = getInternalOsImage(osClient);
    }
    for (Image image : images) {
      for (String imageIdCmdb : getImagesFromCmdb()) {
        if (image.getId().equals(imageIdCmdb)) {
          return image.getId();
        }
      }
    }
    if (!images.isEmpty()) {
      return images.get(0).getId();
    }
    throw new NotFoundException("Unable to find any image in Openstack");
  }

  protected List<String> getImagesFromCmdb() {
    CmdbClientForOpenstack cmdbClient = new CmdbClientForOpenstack();
    return Arrays.asList(cmdbClient.getImageList());
  }

  protected ServerCreate createOsServer(String instanceName) {
    // Create the instance by using openstack4J rather than plain APIs
    try {
      flavorId = getFlavor();

      imageId = getOsImage();

    } catch (Exception ex) {
      log.error(
          "Impossible to get data about the instance: " + instanceName + " " + ex.getMessage());
    }
    return Builders.server().name(instanceName).flavor(flavorId).image(imageId).build();
  }

  protected void bootOsServer(ServerCreate sc, Object osClient) {
    if (osClient.equals(osClientV3)) {
      osClientV3.compute().servers().boot(sc);
    } else {
      osClientV2.compute().servers().boot(sc);
    }
  }

  /**
   * Create the virtual machine in Openstack by polling the result coming from openstack4j.
   * @return CreateVmResult
   * @throws InterruptedException exec
   * @throws TimeoutException timeout
   */
  public CreateVmResult createVm(Object osClient) {

    String instanceName = INSTANCE_NAME + new BigInteger(37, new SecureRandom()).toString(16);

    ServerCreate sc = createOsServer(instanceName);

    // Boot the Server
    long startTime = System.currentTimeMillis();

    // Create the machine
    bootOsServer(sc, osClient);

    // resultMap = new HashMap<>();
    long responseTime;

    // Wait for the process to be done or just get the immediate result
    // coming from Openstack API
    try {
      if (Boolean
          .parseBoolean(PropertiesManager.getProperty(OpenstackProbeTags.WAIT_FOR_CREATION))) {
        log.info("Calculating real creation server time by polling the status to be active");
        serverCreation = poller(instanceName);
      } else {
        log.info("Calculating http creation server immediate response time");
        serverCreation = pollGetImmediateResult(instanceName);
      }
    } catch (TimeoutException | InterruptedException ie) {
      log.debug("Timeout or interrupted operation when waiting for the creation of an instance: "
          + ie.getMessage());
    }

    if (serverCreation.get(Server.Status.ACTIVE) == null) {
      for (Entry<Server.Status, Server> entry : serverCreation.entrySet()) {
        vmId = serverCreation.get(entry.getKey()).getId() != null
            ? serverCreation.get(entry.getKey()).getId() : "";
        resultMap = manageResponseStatus(entry.getKey());
      }
      responseTime = System.currentTimeMillis();
    } else {
      long creationTime = serverCreation.get(Server.Status.ACTIVE).getCreated().getTime();

      responseTime = creationTime - startTime;
      log.info(TOTAL_CREATION_TIME + serverCreation.get(Server.Status.ACTIVE).getId() + TIME_MS
          + responseTime);

      resultMap = manageResponseStatus(Server.Status.ACTIVE);
      vmId = serverCreation.get(Server.Status.ACTIVE).getId();
      log.info("Created VM id: " + vmId + " name: "
          + serverCreation.get(Server.Status.ACTIVE).getName());
    }

    // Feed monitoring info
    return new CreateVmResult(resultMap.get(AVAILABILITY_STATUS), resultMap.get(HTTP_RESPONSE_CODE),
        responseTime, vmId);
  }

  /**
   * Manage the Response status of server in case of creation.
   * @param server serverStatus
   * @see Server serverStatus
   * @return the map for managing the values
   */
  protected Map<String, Integer> manageResponseStatus(Server.Status serverStatus) {
    boolean resultServerStatus;
    if (serverStatus.equals(Server.Status.ACTIVE)) {
      resultServerStatus = true;
    } else {
      resultServerStatus = false;
    }
    return manageResult(resultServerStatus);
  }

  protected List<? extends Server> getServerOsList(Object osClient) {
    return (osClient.equals(osClientV3)) ? osClientV3.compute().servers().list()
        : osClientV2.compute().servers().list();
  }

  /**
   * It polls the result instance Server until is really returned.
   * @param instanceName name of instance to be created
   * @return Server instance from openstak4j api @see Server
   * @throws TimeoutException timeout
   * @throws InterruptedException interrupt
   */
  private Map<Server.Status, Server> poller(/* Server server, */ String instanceName)
      throws TimeoutException, InterruptedException {

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.SECOND, 9 * 10); // 7minutes
    long stopPollingTime = calendar.getTimeInMillis();

    boolean created = false;
    Map<Server.Status, Server> serverResultMap = new HashMap<>();
    servers = getServerOsList(osClient);
    for (Server serverInstance : servers) {
      if (serverInstance.getName().equalsIgnoreCase(instanceName)) {
        while (System.currentTimeMillis() < stopPollingTime
            && !serverInstance.getStatus().equals(Server.Status.ERROR)
            && !serverInstance.getStatus().equals(Server.Status.ACTIVE)) {
          if (getDiagnosedServer(serverInstance, osClient)) {
            serverResultMap.put(Server.Status.ACTIVE, serverInstance);
            created = true;
            break;
          } else {
            serverResultMap.put(serverInstance.getStatus(), serverInstance);
          }
        }
        // If it's still creating (build status), is in error or polling
        // time is expired, meaning
        // the machine has not been succesffully created
        if (serverInstance.getStatus().equals(Server.Status.ERROR)
            || serverInstance.getStatus().equals(Server.Status.BUILD)
                && System.currentTimeMillis() > stopPollingTime
                && !getDiagnosedServer(serverInstance, osClient)) {
          serverResultMap.put(serverInstance.getStatus(), serverInstance);
          return serverResultMap;
        }
        Iterator<? extends Server> itr = servers.iterator();
        if (created || !itr.hasNext()) {
          break;
        }
      }
    }
    if (!created) {
      for (Server serverNotCreated : servers) {
        if (serverNotCreated.getName().equalsIgnoreCase(instanceName)) {
          serverResultMap.put(serverNotCreated.getStatus(), serverNotCreated);
          return serverResultMap;
        }
      }
    }
    return serverResultMap;
  }

  /**
   * Poll the PI for deleting the instance.
   * @param instanceName instance server
   * @throws TimeoutException timeout
   * @throws InterruptedException interrupt
   */
  private void pollerDelete(String instanceName) throws TimeoutException, InterruptedException {

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.SECOND, 9 * 10); // 7minutes
    long stopPollingTime = calendar.getTimeInMillis();

    boolean deleted = false;
    int poll = 1;
    servers = getServerOsList(osClient);
    for (Server serverInstance : servers) {
      if (!serverInstance.getId().equalsIgnoreCase(vmId) && poll++ == servers.size()) {
        while (System.currentTimeMillis() < stopPollingTime) {
          if ((poll++) - 2 == servers.size()) {
            log.info("instance " + instanceName + " succesfully deleted");
            deleted = true;
            break;
          }
          // If it's still deleting (build status), is in error or
          // polling
          // time is expired, meaning
          // the machine has not been succesffully created
          if (System.currentTimeMillis() > stopPollingTime) {
            log.debug("Unable to delete the instance " + instanceName);
            deleted = false;
          }
        }
      }
    }
  }

  /**
   * It polls the result instance Server until is really returned.
   * @param instanceName name of instance to be created
   * @return Server instance from openstak4j api @see Server
   * @throws TimeoutException timeout
   * @throws InterruptedException interrupt
   */
  private Map<Server.Status, Server> pollGetImmediateResult(
      /* Server server, */ String instanceName) throws TimeoutException, InterruptedException {

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.SECOND, 9 * 10); // 7minutes

    boolean created = false;
    Map<Server.Status, Server> serverResultMap = new HashMap<>();
    // List<? extends Server>
    servers = getServerOsList(osClient);
    for (Server serverInstance : servers) {
      if (serverInstance.getName().equalsIgnoreCase(instanceName)) {
        if ((!serverInstance.getStatus().equals(Server.Status.ERROR)
            && !serverInstance.getStatus().equals(Server.Status.ACTIVE))
            || serverInstance.getVmState().equals("building")) {
          serverResultMap.put(Server.Status.ACTIVE, serverInstance);
          created = true;
          break;
        } else {
          serverResultMap.put(serverInstance.getStatus(), serverInstance);
        }
      }
      Iterator<? extends Server> itr = servers.iterator();
      if (created || !itr.hasNext()) {
        break;
      }
    }
    return serverResultMap;
  }

  /**
   * Manages the result when polling.
   * @param serverInstance instance
   * @return a flag
   * @throws InterruptedException interrupt
   */
  protected boolean getDiagnosedServer(Server serverInstance, Object osClient)
      throws InterruptedException {

    Thread.sleep(3000);
    boolean isInstanceCreated = false;

    Server serverDiagnosed =
        osClient.equals(osClientV3) ? osClientV3.compute().servers().get(serverInstance.getId())
            : osClientV2.compute().servers().get(serverInstance.getId());
    // poll until the result is mappable...
    log.info("Polling for an instance to be created...");
    Map<Server.Status, Server> serverResultMap = new HashMap<>();
    if (serverDiagnosed.getPowerState().equals(POWER_STATE_ACTIVE)) {
      log.info("created..");
      isInstanceCreated = true;
      serverResultMap.put(Server.Status.ACTIVE, serverInstance);

      return isInstanceCreated;
    } else {
      return isInstanceCreated;
    }
  }

  protected Server getInstanceInfo(Server serverInstance, Object osClient) {
    return (osClient.equals(osClientV3))
        ? osClientV3.compute().servers().get(serverInstance.getId())
        : osClientV2.compute().servers().get(serverInstance.getId());
  }

  /**
   * Inspect the instance in Openstack after passing the proper uuid. In case is not created
   * openstack4j returns null date from (server.getCreated()).
   * @param vmId instance uuid
   * @return @see InspectVmResult
   * @throws TimeoutException timeout
   * @throws InterruptedException interrupt
   */
  private InspectVmResult inspectVm(String vmId, Object osClient)
      throws TimeoutException, InterruptedException {
    long startTime = System.currentTimeMillis();
    server = (osClient.equals(osClientV3)) ? osClientV3.compute().servers().get(vmId)
        : osClientV2.compute().servers().get(vmId);

    // Measure response time
    while (server.getCreated() == null) {
      synchronized (server) {
        wait(3000);
      }
    }
    long responseTime = System.currentTimeMillis() - startTime;
    log.info("Total elapsed http request/response time after inspection of instance: " + vmId
        + TIME_MS + responseTime);

    Map<String, Integer> resultMap = manageResponseStatus(server.getStatus());

    // Feed monitoring info
    return new InspectVmResult(resultMap.get(AVAILABILITY_STATUS),
        resultMap.get(HTTP_RESPONSE_CODE), responseTime);
  }

  /**
   * Delete the instance in Openstack by using Openstack4j.
   * @param vmI VM uuid
   * @return the managed result of @see DeleteVmResult
   * @throws TimeoutException timeout
   * @throws InterruptedException interrupt
   */
  protected DeleteVmResult deleteVm(String vmId, Object osClient)
      throws TimeoutException, InterruptedException {

    // Measure response time
    long startTime = System.currentTimeMillis();

    ActionResponse result = osClient.equals(osClientV3)
        ? osClientV3.compute().servers().delete(vmId) : osClientV2.compute().servers().delete(vmId);
    if (result.isSuccess()) {
      // Just make sure the machine is really deleted
      pollerDelete(vmId);
    }
    long responseTime = System.currentTimeMillis() - startTime;
    log.info("Total elapsed http request/response for deleting the instance: " + vmId + TIME_MS
        + responseTime);

    Map<String, Integer> resultMap = manageDeleteResult(result);
    // Feed monitoring info
    return new DeleteVmResult(resultMap.get(AVAILABILITY_STATUS), resultMap.get(HTTP_RESPONSE_CODE),
        responseTime);
  }

  /**
   * Manage the result in case a Vm is created or returned using openstack4j. Cannot be used the
   * classic http jersey client results (wrapped in the library).
   * @param result boolean (true in case of successful result in terms of availability status)
   * @return Map for accessing to values of results
   */
  private Map<String, Integer> manageResult(boolean result) {

    Map<String, Integer> resultMap = new HashMap<>();

    if (result) {
      resultMap.put(HTTP_RESPONSE_CODE, 200);
      resultMap.put(AVAILABILITY_STATUS, 1);
    } else {
      int httpCodeRandom = getRandomCode((400 - 600) + 400);

      resultMap.put(HTTP_RESPONSE_CODE, httpCodeRandom);
      resultMap.put(AVAILABILITY_STATUS, 0);
    }
    return resultMap;
  }

  private Integer getRandomCode(int randCode) {
    Random rand = new Random();
    return rand.nextInt(randCode);
  }

  // Manage the response coming from delete openstack4j classes
  private Map<String, Integer> manageDeleteResult(ActionResponse code) {

    Map<String, Integer> resultMap = new HashMap<>();

    if (code.isSuccess()) {
      resultMap.put(HTTP_RESPONSE_CODE, 200);
      resultMap.put(AVAILABILITY_STATUS, 1);
    } else {

      resultMap.put(HTTP_RESPONSE_CODE, code.getCode());
      resultMap.put(AVAILABILITY_STATUS, 0);
    }
    return resultMap;
  }

  /**
   * It retrieves the list of networks available.
   */
  public void getNetworksList(Object osClient) {

    List<? extends Network> networks = (osClient.equals(osClientV3))
        ? osClientV3.networking().network().list() : osClientV2.networking().network().list();
    log.info(networks.toString());
  }

  /**
   * This method goes through the whole lifecycle of a VM (create, inspect and delete VM) in order
   * to retrieve monitoring metrics: availability, HTTP response code and response time.
   * @return Object representing results for each operation and a global aggregation.
   * @throws TimeoutException timeout
   * @throws InterruptedException interrupt
   */
  public OpenstackProbeResult getOpenstackMonitoringInfo(String project)
      throws TimeoutException, InterruptedException {
    // Follow the full lifecycle for a VM
    // Retrieve the operation token

    osClient = osClient.equals(osClientV3) ? getOsAuthIam(keystoneclientV3, project)
        : getOsAuth(myKeystoneClientV2);

    log.info("Token obtained from " + providerId + " is valid. Continue monitoring operation...");

    CreateVmResult createVmInfo = createVm(osClient);
    // Map<String, Object> resultTryMap = new HashMap<>();
    if (createVmInfo.getCreateVmAvailability() == 0) {
      // Send failure result, since we cannot go on with the process
      OpenstackProbeResult failureResult = new OpenstackProbeResult(providerId);
      failureResult.addCreateVmInfo(createVmInfo);
      failureResult.addGlobalInfo(0, createVmInfo.getCreateVmResult(), -1);
      log.info("Instantiation failed with time: " + failureResult.getGlobalResponseTime());

      tryToDeleteInstance(createVmInfo);
      return failureResult;
    }

    InspectVmResult inspectVmInfo = inspectVm(createVmInfo.getVmId(), osClient);
    DeleteVmResult deleteVmInfo = deleteVm(createVmInfo.getVmId(), osClient);
    // Determine Global Availability
    int globalAvailability = 1;
    if (createVmInfo.getCreateVmAvailability() == 0 || inspectVmInfo.getInspectVmAvailability() == 0
        || deleteVmInfo.getDeleteVmAvailability() == 0) {
      globalAvailability = 0;
    }

    // Determine Global Result
    int globalResult = 200;
    int createVmResult = createVmInfo.getCreateVmResult();
    int inspectVmResult = inspectVmInfo.getInspectVmResult();
    int deleteVmResult = deleteVmInfo.getDeleteVmResult();
    if (createVmResult > inspectVmResult) {
      if (createVmResult > deleteVmResult) {
        globalResult = createVmResult;
      } else {
        globalResult = deleteVmResult;
      }
    } else {
      if (inspectVmResult > deleteVmResult) {
        globalResult = inspectVmResult;
      } else {
        globalResult = deleteVmResult;
      }
    }
    // Determine Global ResponseTime
    long globalResponseTime = createVmInfo.getCreateVmResponseTime()
        + inspectVmInfo.getInspectVmResponseTime() + deleteVmInfo.getDeleteVmResponseTime();

    // Construct the result
    OpenstackProbeResult finalResult = new OpenstackProbeResult(providerId);
    finalResult.addCreateVmInfo(createVmInfo);
    finalResult.addInspectVmInfo(inspectVmInfo);
    finalResult.addDeleteVmInfo(deleteVmInfo);
    finalResult.setOsInstanceList(getServerOsList(osClient));
    finalResult.addGlobalInfo(globalAvailability, globalResult, globalResponseTime);

    log.info(finalResult);

    return finalResult;
  }

  private void tryToDeleteInstance(CreateVmResult createVmInfo) {

    try {
      if (createVmInfo.getVmId() != null) {
        deleteVm(createVmInfo.getVmId(), osClient);
      }

    } catch (TimeoutException | InterruptedException e) {
      log.debug("Timeout or interrupted operation when deleting the instance: " + e.getMessage());
    }
  }
}
