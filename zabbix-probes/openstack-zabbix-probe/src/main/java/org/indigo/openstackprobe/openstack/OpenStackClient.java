/**
Copyright 2017 Reply Italy
Licensed under the Apache License, Version 2.0 (the License);
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

**/

package org.indigo.openstackprobe.openstack;

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
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;

import org.apache.commons.logging.impl.Log4JLogger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.client.IOSClientBuilder.V2;
import org.openstack4j.model.compute.ActionResponse;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.image.Image;
import org.openstack4j.model.network.Network;
import org.openstack4j.openstack.OSFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

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
  private String baseNovaUrl;
  private String baseKeystoneUrl;
  private String openStackUser;
  private String openStackPwd;
  private String currentToken;
  private String providerId;
  private V2 myKeystoneClient = null;
  private static final String POWER_STATE_ACTIVE = "1";
  private String flavorId;
  private String imageId;
  private final String INSTANCE_NAME = "vMOpenstackZabbixProbe_";
  private String tenantName;
  private static final String HTTP_RESPONSE_CODE = "httpCode";
  private static final String AVAILABILITY_STATUS = "availability";
  private static final String TIME_MS = " time in milliseconds: ";
  private static final String TOTAL_CREATION_TIME =
      "Total elapsed http request/response for creating the instance: ";
  private static final String DELETE_OPTION = "delete";
  private static final String INSPECT_OPTION = "inspect";

  private static final Logger log = LogManager.getLogger(OpenStackClient.class);

  /**
   * Main constructor of the OpenStackOcciClient class. It retrieves some information from the
   * properties files in order to create and configure the client which will connect to the remote
   * OCCI API of a cloud provider.
   * 
   * @param keystoneLocation
   *          String with the full location of Keystone [IP:Port]
   * @param novaLocation
   *          String with the root location of the OCCI API [IP:Port]
   * @param providerName
   *          String with the identifier of the Cloud Provider
   */
  public OpenStackClient(String keystoneLocation, String providerUrl, String providerName) {

    // Retrieve properties
    PropertiesManager myProp = new PropertiesManager();
    openStackUser = myProp.getProperty(PropertiesManager.OPENSTACK_USER);
    openStackPwd = myProp.getProperty(PropertiesManager.OPENSTACK_PASSWORD);
    tenantName = myProp.getProperty(PropertiesManager.TENANT_NAME);
    providerId = providerName;

    // Disable issue with SSL Handshake in Java 7 and indicate certificates keystore
    System.setProperty("jsse.enableSNIExtension", "false");
    String certificatesTrustStorePath = myProp.getProperty(PropertiesManager.JAVA_KEYSTORE);
    System.setProperty("javax.net.ssl.trustStore", certificatesTrustStorePath);

    // Create the Clients
    ClientConfig cc = new ClientConfig();
    client = JerseyClientBuilder.newClient(cc);
    myKeystoneClient = OSFactory.builder();

    // Prepare access URLs
    baseKeystoneUrl = keystoneLocation.contains("https") ? keystoneLocation
        : keystoneLocation.replace("http", "https");
    baseNovaUrl = providerUrl;
  }

  /**
   * Constructor to be used for automatic testing purposes only.
   * 
   * @param mockClient
   *          Mock of the Jersey Client class, for simulating.
   */
  public OpenStackClient(Client mockClient, V2 mockKeystone, String mockFlavor, String mockImage) {
    client = mockClient;
    myKeystoneClient = mockKeystone;
    flavorId = mockFlavor;
    imageId = mockImage;
  }

  private String getTokenId() {
    // Authenticate
    OSClient os = myKeystoneClient.endpoint(baseKeystoneUrl)
        .credentials(openStackUser, openStackPwd).tenantName(tenantName).authenticate();
    return os.getToken().getId();
  }

  private OSClient getOSAuth() {
    // More general Authentication
    return myKeystoneClient.endpoint(baseKeystoneUrl).credentials(openStackUser, openStackPwd)
        .tenantName(tenantName).authenticate();

  }

  protected List<? extends Flavor> getInternalFlavor() {
    return getOSAuth().compute().flavors().list();
  }

  public String getFlavor() {
    List<? extends Flavor> flavors = getInternalFlavor();

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

  protected List<? extends Image> getInternalOsImage() {
    return getOSAuth().images().list();
  }

  public String getOsImage() {
    List<? extends Image> images = getInternalOsImage();
    for (Image image : images) {
      for (String imageIdCmdb : getImagesFromCmdb()) {
        if (image.getId().equals(imageIdCmdb)) {
          return image.getId();
        }
      }
    }
    while (!images.isEmpty()) {
      return images.get(0).getId();
    }
    throw new NotFoundException("Unable to find any image in Openstack");
  }

  protected List<String> getImagesFromCmdb() {
    CmdbClient cmdbClient = new CmdbClient();
    return Arrays.asList(cmdbClient.getImageList());
  }

  protected ServerCreate createOsServer(String instanceName) {
    // Create the instance by using openstack4J rather than plain APIs
    flavorId = getFlavor();
    imageId = getOsImage();
    return Builders.server().name(instanceName).flavor(flavorId).image(imageId).build();
  }

  protected void bootOsServer(ServerCreate sc) {
    getOSAuth().compute().servers().boot(sc);
  }

  /**
   * Create the virtual machine in Openstack by polling the result coming from openstack4j.
   * 
   * @return
   * @throws InterruptedException
   * @throws TimeoutException
   */
  public CreateVmResult createVm() {

    String instanceName = INSTANCE_NAME + new BigInteger(37, new SecureRandom()).toString(16);

    ServerCreate sc = createOsServer(instanceName);

    // Boot the Server
    long startTime = System.currentTimeMillis();

    // Create the machine
    bootOsServer(sc);

    Map<String, Integer> resultMap = new HashMap<>();
    long responseTime = 0;

    // Wait for the process to be done
    Map<Server.Status, Server> serverCreation = new HashMap<>();
    String vmId = null;

    try {
      serverCreation = poller(instanceName);
    } catch (TimeoutException | InterruptedException e) {

      log.debug(e.getMessage());
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
      responseTime = startTime - creationTime;
      log.info(TOTAL_CREATION_TIME + serverCreation.get(Server.Status.ACTIVE).getId() + TIME_MS
          + responseTime);
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
   * 
   * @param server
   * @see Server
   * @return the map for managing the values
   */
  private Map<String, Integer> manageResponseStatus(Server.Status serverStatus) {
    boolean resultServerStatus;
    if (serverStatus.equals(Server.Status.ACTIVE)) {
      resultServerStatus = true;
    } else {
      resultServerStatus = false;
    }
    return manageResult(resultServerStatus);
  }

  protected List<? extends Server> getServerOsList() {
    return getOSAuth().compute().servers().list();
  }

  /**
   * It polls the result instance Server until is really returned.
   * 
   * @param instanceName
   *          name of instance to be created
   * @return Server instance from openstak4j api @see Server
   * @throws TimeoutException
   * @throws InterruptedException
   */
  private Map<Server.Status, Server> poller(/* Server server, */ String instanceName)
      throws TimeoutException, InterruptedException {

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.SECOND, 1); // * 10); // 42 --> 7minutes
    long stopPollingTime = calendar.getTimeInMillis();

    boolean created = false;
    Map<Server.Status, Server> serverResultMap = new HashMap<>();
    List<? extends Server> servers = getServerOsList();
    for (Server serverInstance : servers) {
      if (serverInstance.getName().equalsIgnoreCase(instanceName)) {
        while (System.currentTimeMillis() < stopPollingTime
            && !serverInstance.getStatus().equals(Server.Status.ERROR)) {
          getDiagnosedServer(serverInstance);

        }
        if (serverInstance.getStatus().equals(Server.Status.ERROR)
            || serverInstance.getStatus().equals(Server.Status.BUILD)) {
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

  protected Map<Server.Status, Server> getDiagnosedServer(Server serverInstance)
      throws InterruptedException {

    Thread.sleep(3000);
    Server serverDiagnosed = getOSAuth().compute().servers().get(serverInstance.getId());
    // poll until the result is mappable...
    log.info("Polling..");
    Map<Server.Status, Server> serverResultMap = new HashMap<>();
    if (serverDiagnosed.getPowerState().equals(POWER_STATE_ACTIVE)) {
      log.info("created..");
      Server server = serverDiagnosed;
      serverResultMap.put(Server.Status.ACTIVE, server);
    }
    return serverResultMap;
  }

  /**
   * Method used for testing openstack4J API.
   */
  private void getVmsList() {

    // List all Servers
    List<? extends Server> servers = getOSAuth().compute().servers().list();

    // Measure response time and wait
    long startTime = System.currentTimeMillis();
    while (servers.isEmpty()) {
      try {
        synchronized (servers) {
          servers.wait(3000);
        }

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    long elapsedTime = System.currentTimeMillis() - startTime;
    log.info("Total elapsed http request/response time in milliseconds: " + elapsedTime);

  }

  /**
   * Inspect the instance in Openstack after passing the proper uuid. In case is not created
   * openstack4j returns null date from (server.getCreated()).
   * 
   * @param vmId
   *          instance uuid
   * @return @see InspectVmResult
   * @throws TimeoutException
   * @throws InterruptedException
   */
  private InspectVmResult inspectVm(String vmId) throws TimeoutException, InterruptedException {
    long startTime = System.currentTimeMillis();
    Server server = getOSAuth().compute().servers().get(vmId);

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
   * 
   * @param vmI
   *          VM uuid
   * @return the managed result of @see DeleteVmResult
   * @throws TimeoutException
   * @throws InterruptedException
   */
  private DeleteVmResult deleteVm(String vmId) throws TimeoutException, InterruptedException {

    // Measure response time
    long startTime = System.currentTimeMillis();

    ActionResponse result = getOSAuth().compute().servers().delete(vmId);
    while (!result.isSuccess()) {
      result.wait(3000);
    }
    if (!result.isSuccess()) {
      result.getCode();
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
   * 
   * @param result
   *          boolean (true in case of successful result in terms of availability status)
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

  private Integer getRandomCode(int i) {
    Random r = new Random();
    return r.nextInt(i);
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
  public void getNetworksList() {

    List<? extends Network> networks = getOSAuth().networking().network().list();
    log.info(networks.toString());
  }

  /**
   * This method goes through the whole lifecycle of a VM (create, inspect and delete VM) in order
   * to retrieve monitoring metrics: availability, HTTP response code and response time.
   * 
   * @return Object representing results for each operation and a global aggregation.
   * @throws TimeoutException
   * @throws InterruptedException
   */
  public OpenstackProbeResult getOpenstackMonitoringInfo()
      throws TimeoutException, InterruptedException {
    // Follow the full lifecycle for a VM
    // Retrieve the operation token
    currentToken = getTokenId();
    if (currentToken == null) {
      return null;
    }

    log.info("Token is valid. Continue monitoring operation...");

    CreateVmResult createVmInfo = createVm();
    Map<String, Object> resultTryMap = new HashMap<>();
    if (createVmInfo.getCreateVmAvailability() == 0) {
      // Send failure result, since we cannot go on with the process
      OpenstackProbeResult failureResult = new OpenstackProbeResult(providerId);
      failureResult.addCreateVmInfo(createVmInfo);
      failureResult.addGlobalInfo(0, createVmInfo.getCreateVmResult(), -1);
      log.info("Instantiation failed with time: " + failureResult.getGlobalResponseTime());

      tryToDeleteInstance(createVmInfo);
      return failureResult;
    }

    InspectVmResult inspectVmInfo =
        (InspectVmResult) InspectAndDeleteInstance(createVmInfo).get(INSPECT_OPTION);
    DeleteVmResult deleteVmInfo =
        (DeleteVmResult) InspectAndDeleteInstance(createVmInfo).get(DELETE_OPTION);
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
    finalResult.addGlobalInfo(globalAvailability, globalResult, globalResponseTime);
    log.info(finalResult);

    return finalResult;
  }

  private Map<String, ?> InspectAndDeleteInstance(CreateVmResult createVmInfo) {

    Map<String, Object> resultTryMap = new HashMap<>();
    try {
      DeleteVmResult deleteVmInfo = deleteVm(createVmInfo.getVmId());
      InspectVmResult inspectVmInfo = inspectVm(createVmInfo.getVmId());

      resultTryMap.put(DELETE_OPTION, deleteVmInfo);
      resultTryMap.put(INSPECT_OPTION, inspectVmInfo);

    } catch (TimeoutException | InterruptedException e) {
      log.debug(e.getMessage());
    }
    return resultTryMap;
  }

  private void tryToDeleteInstance(CreateVmResult createVmInfo) {

    try {
      if(createVmInfo.getVmId()!=null)
      deleteVm(createVmInfo.getVmId());

    } catch (TimeoutException | InterruptedException e) {
      log.debug(e.getMessage());
    }
  }

  /**
   * Typical main for testing.
   * 
   * @param args
   *          Typical args
   * @throws InterruptedException
   * @throws TimeoutException
   */
  public static void main(String[] args) throws TimeoutException, InterruptedException {
    // Run the OCCI monitoring process and retrieve the result
    OpenStackClient myClient = new OpenStackClient("http://cloud.recas.ba.infn.it:5000/v2.0",
        "http://cloud.recas.ba.infn.it:8774", "provider-RECAS-BARI");

    myClient.getNetworksList();

    myClient.getOpenstackMonitoringInfo();
  }
}