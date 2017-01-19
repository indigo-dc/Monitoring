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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.client.Client;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.client.IOSClientBuilder.V2;
import org.openstack4j.model.compute.ActionResponse;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.network.Network;
import org.openstack4j.openstack.OSFactory;

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
  private String baseNovaUrl = "";
  private String baseKeystoneUrl = "";
  private String openStackUser = "";
  private String openStackPwd = "";
  private String currentToken = "";
  private String providerId;
  private V2 myKeystoneClient = null;
  private static final String POWER_STATE_ACTIVE = "1";

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
  public OpenStackClient(Client mockClient, V2 mockKeystone) {
    client = mockClient;
    myKeystoneClient = mockKeystone;
  }

  private String getToken() {
    /*
     * // Build JSON input TokenRequest tRequest= new TokenRequest("INDIGO", openStackUser,
     * openStackPwd); Gson gson = new Gson(); String input = gson.toJson(tRequest);
     * System.out.println (input);
     * 
     * // Call to Keystone WebTarget target = client.target(baseKeystoneUrl + "/tokens");
     * System.out.println (target.toString()); Response response =
     * target.request(MediaType.APPLICATION_JSON) .post(Entity.entity(input,
     * MediaType.APPLICATION_JSON), Response.class);
     * 
     * //String result = r.accept(MediaType.APPLICATION_JSON_TYPE).get(String.class);
     * 
     * //JsonElement jelement = new JsonParser().parse(response.);
     * //System.out.println(jelement.toString());
     *
     */

    // Authenticate
    OSClient os = myKeystoneClient.endpoint(baseKeystoneUrl)
        .credentials(openStackUser, openStackPwd).tenantName("INDIGO_DEMO").authenticate();

    // System.out.println(os.getToken().toString());

    return os.getToken().getId();
  }

  private OSClient getOSAuth() {
    // More general Authentication
    OSClient os = myKeystoneClient.endpoint(baseKeystoneUrl)
        .credentials(openStackUser, openStackPwd).tenantName("INDIGO_DEMO").authenticate();

    System.out.println(os.getToken().toString());

    return os;
  }

  private CreateVmResult createVm() throws InterruptedException, TimeoutException {

    // Create the instance by using openstack4J rather than plain APIs
    String instanceName = "vMOpenstackZabbixProbe";
    ServerCreate sc = Builders.server().name(instanceName).flavor("7")
        .image("867bdfd7-7a97-4ef5-bfa8-9f7d05958239").build();

    // Boot the Server
    long startTime = System.currentTimeMillis();
    // Create the machine
    getOSAuth().compute().servers().boot(sc);

    // Wait for the process to be done
    Server serverCreation = poller(/* server, */ instanceName);
    long creationTime = serverCreation.getCreated().getTime();
    long responseTime = startTime - creationTime;

    System.out.println("Total elapsed http request/response time in milliseconds: " + responseTime);

    Map<String, Integer> resultMap = manageResponseStatus(serverCreation);

    String vmId = serverCreation.getId();
    System.out.println("Created VM: " + vmId);

    // Feed monitoring info
    CreateVmResult monitoredInfo = new CreateVmResult(resultMap.get("availability"),
        resultMap.get("httpCode"), responseTime, vmId);

    return monitoredInfo;
  }

  /**
   * Manage the Response status of server in case of creation.
   * 
   * @param server @see Server
   * @return the map for managing the values 
   */
  private Map<String, Integer> manageResponseStatus(Server server) {
    boolean resultServerStatus;
    if (getState(server).equals(Server.Status.ACTIVE)) {
      resultServerStatus = true;
    } else {
      resultServerStatus = false;
    }
    return manageResult(resultServerStatus);
  }

  /**
   * It polls the result instance Server until is really returned. 
   * @param instanceName name of instance to be created
   * @return Server instance from openstak4j api @see Server
   * @throws TimeoutException 
   * @throws InterruptedException
   */
  private Server poller(/* Server server, */ String instanceName)
      throws TimeoutException, InterruptedException {

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.SECOND, 30 * 10);
    long stopPollingTime = calendar.getTimeInMillis();

    boolean created = false;
    String vmId = null;
    Server server = null;
    List<? extends Server> servers = getOSAuth().compute().servers().list();
    for (Server serverInstance : servers) {
      if (serverInstance.getName().equalsIgnoreCase(instanceName)) {
        while (System.currentTimeMillis() < stopPollingTime) {
          Thread.sleep(3000);
          Server serverDiagnosed = getOSAuth().compute().servers().get(serverInstance.getId());
          // polling
          System.out.println("Polling..");
          if (serverDiagnosed.getPowerState().equals(POWER_STATE_ACTIVE)) {
            System.out.println("created..");
            vmId = serverDiagnosed.getUuid();
            server = serverDiagnosed;
            created = true;
            return server;
          }
        }
        Iterator<? extends Server> itr = servers.iterator();
        if (created || !itr.hasNext()) {
          break;
        }
      }
    }
    if (!created) {
      server = getOSAuth().compute().servers().get(vmId);
      getState(server);
    }
    return server;
  }

  private Server.Status getState(Server server) {
    return server.getStatus();
  }

  /**
   * Method used for testing openstack4J API
   */
  private void getVmsList() {

    // List all Servers
    List<? extends Server> servers = getOSAuth().compute().servers().list();

    // Measure response time and wait
    long startTime = System.currentTimeMillis();
    while (servers.isEmpty()) {
      try {
        servers.wait(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    // Response response = invocationBuilder.get();
    long elapsedTime = System.currentTimeMillis() - startTime;
    System.out.println("Total elapsed http request/response time in milliseconds: " + elapsedTime);

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
      wait(3000);
    }

    // Response response = invocationBuilder.get();
    long responseTime = System.currentTimeMillis() - startTime;
    System.out.println("Total elapsed http request/response time in milliseconds: " + responseTime);

    Map<String, Integer> resultMap = manageResponseStatus(server);

    // Feed monitoring info
    InspectVmResult monitoredInfo =
        new InspectVmResult(resultMap.get("availability"), resultMap.get("httpCode"), responseTime);

    return monitoredInfo;
  }

  /**
   * Delete the the instance in Openstack by using Openstack4j.
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
    // Response response = invocationBuilder.delete();

    ActionResponse result = getOSAuth().compute().servers().delete(vmId);
    while (!result.isSuccess()) {
      result.wait(3000);
    }
    if (!result.isSuccess()) {
      result.getCode();
    }
    long responseTime = System.currentTimeMillis() - startTime;
    System.out.println("Total elapsed http request/response time in milliseconds: " + responseTime);

    Map<String, Integer> resultMap = manageDeleteResult(result);

    // Feed monitoring info
    DeleteVmResult monitoredInfo =
        new DeleteVmResult(resultMap.get("availability"), resultMap.get("httpCode"), responseTime);

    return monitoredInfo;
  }

  /**
   * Manage the result in case a Vm is created or returned using openstack4j. Cannot be used the
   * classic http jersey client results (wrapped in the library).
   * 
   * @param result
   *          boolean (true in case of success)
   * @return Map for accessing to values of results
   */
  private Map<String, Integer> manageResult(boolean result) {

    Map<String, Integer> resultMap = new HashMap<>();

    if (result) {
      resultMap.put("httpCode", 200);
      resultMap.put("availability", 1);
    } else {
      Random r = new Random();
      int httpCodeRandom = r.nextInt(400 - 600) + 400;

      resultMap.put("httpCode", httpCodeRandom);
      resultMap.put("availability", 0);
    }
    return resultMap;
  }

  // Manage the response coming from delete openstack4j classes
  private Map<String, Integer> manageDeleteResult(ActionResponse code) {

    Map<String, Integer> resultMap = new HashMap<>();

    if (code.isSuccess()) {
      resultMap.put("httpCode", 200);
      resultMap.put("availability", 1);
    } else {

      resultMap.put("httpCode", code.getCode());
      resultMap.put("availability", 0);
    }
    return resultMap;
  }

  /**
   * It retrieves the list of networks available.
   */
  public void getNetworksList() {

    List<? extends Network> networks = getOSAuth().networking().network().list();
    System.out.println(networks.toString());
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
    currentToken = getToken();
    if (currentToken == null) {
      return null;
    }

    // getVmsList();

    System.out.println("Token is valid. Continue monitoring operation...");

    CreateVmResult createVmInfo = createVm();
    if (createVmInfo.getCreateVmAvailability() == 0) {
      // Send failure result, since we cannot go on with the process
      OpenstackProbeResult failureResult = new OpenstackProbeResult(providerId);
      failureResult.addCreateVmInfo(createVmInfo);
      failureResult.addGlobalInfo(0, createVmInfo.getCreateVmResult(), -1);
      return failureResult;

    }

    InspectVmResult inspectVmInfo = inspectVm(createVmInfo.getVmId());
    DeleteVmResult deleteVmInfo = deleteVm(createVmInfo.getVmId());

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

    return finalResult;
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
