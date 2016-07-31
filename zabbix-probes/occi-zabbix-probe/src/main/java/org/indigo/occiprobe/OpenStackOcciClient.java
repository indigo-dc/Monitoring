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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.client.IOSClientBuilder.V2;
import org.openstack4j.openstack.OSFactory;

/**
 * It takes care of the interactions to be performed with Cloud Providers whose base platform
 * is OpenStack, since it requires additional interactions with the Keystone component, in order
 * to get authorization tokens.
 * This class is able to perform basic operations on VMs: create, inspect and delete.
 * @author ATOS
 *
 */
public class OpenStackOcciClient {
  private Client client = null;
  private String baseOcciUrl = "";
  private String baseKeystoneUrl = "";
  private String openStackUser = "";
  private String openStackPwd = "";
  private String currentToken = "";
  private String providerId;
  private V2 myKeystoneClient = null;
  
  /**
   * Main constructor of the OpenStackOcciClient class. It retrieves some information
   * from the properties files in order to create and configure the client which 
   * will connect to the remote OCCI API of a cloud provider.
   * @param keystoneLocation String with the full location of Keystone [IP:Port]
   * @param occiLocation String with the root location of the OCCI API [IP:Port]
   * @param providerName String with the identifier of the Cloud Provider
   */
  public OpenStackOcciClient(String keystoneLocation, String occiLocation, String providerName) {
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
    baseKeystoneUrl = keystoneLocation.replace("http", "https");
    baseOcciUrl = occiLocation;                     
  }
  
  /**
   * Constructor to be used for automatic testing purposes only.
   * @param mockClient Mock of the Jersey Client class, for simulating.
   */
  public OpenStackOcciClient(Client mockClient, V2 mockKeystone) {
    client = mockClient;
    myKeystoneClient = mockKeystone;
  }
  
  private String getToken() {
    /*
    // Build JSON input
    TokenRequest tRequest= new TokenRequest("INDIGO", openStackUser, openStackPwd);
    Gson gson = new Gson();
    String input = gson.toJson(tRequest);
    System.out.println (input);
    
    // Call to Keystone
    WebTarget target = client.target(baseKeystoneUrl + "/tokens");
    System.out.println (target.toString());
    Response response =  target.request(MediaType.APPLICATION_JSON)
      .post(Entity.entity(input, MediaType.APPLICATION_JSON), Response.class); 
    
    //String result = r.accept(MediaType.APPLICATION_JSON_TYPE).get(String.class);
    
    //JsonElement jelement = new JsonParser().parse(response.);
    //System.out.println(jelement.toString());
    *
    */
    
    
    // Authenticate 
    OSClient os = myKeystoneClient
                .endpoint(baseKeystoneUrl)
                .credentials(openStackUser,openStackPwd)
                .tenantName("INDIGO_DEMO")
                .authenticate();
    
    //System.out.println(os.getToken().toString());
    
    return os.getToken().getId();
  }
  
  private CreateVmResult createVm() {
    // Call to OCCI API
    WebTarget target = client.target(baseOcciUrl + "/compute/");
    Invocation.Builder invocationBuilder = target.request();
    invocationBuilder.header("Content-Type", "text/occi");
    invocationBuilder.header("X-Auth-Token", currentToken);
    invocationBuilder.header("Category", "compute; scheme=\"http://schemas.ogf.org/occi/infrastructure#\"; class=\"kind\"");
    invocationBuilder.header("Category", "7; scheme=\"http://schemas.openstack.org/template/resource#\"; class=\"mixin\"");
    invocationBuilder.header("Category", "303d8324-69a7-4372-be24-1d68703affd7; scheme=\"http://schemas.openstack.org/template/os#\"; class=\"mixin\"");
    
    /*
    String linkedNetwork = "<http://cloud.recas.ba.infn.it:8787/occi/network/ac063601-5af8-4b80-9972-07908e764a9c>; "
        + "rel=\"http://schemas.ogf.org/occi/infrastructure#network\"; "
        + "category=\"http://schemas.ogf.org/occi/infrastructure#networkinterface\"; "
        + "occi.networkinterface.interface=\"eth0\"; "
        + "occi.networkinterface.mac=\"00:11:22:33:44:55\";";
    
    invocationBuilder.header("Link", linkedNetwork);
    */
    
    // Invoke the OCCI service and measure response time
    long startTime = System.currentTimeMillis();
    Response response = invocationBuilder.post(null);
    long responseTime = System.currentTimeMillis() - startTime;
    System.out.println("Total elapsed http request/response time in milliseconds: " + responseTime);
    
    // Get response information
    //System.out.println(response.toString());
    int httpCode = response.getStatus();
    int availability = 1;
    
    if (httpCode >= 400 && httpCode <= 600) {
      availability = 0;
    }
    
    // Get response message
    String message = response.readEntity(String.class);
    System.out.println(message);
    
    String[] vmUrlParts = message.split("/");
    String vmId = vmUrlParts[vmUrlParts.length - 1];
    System.out.println("Created VM: " + vmId);
     
    // Feed monitoring info
    CreateVmResult monitoredInfo = new CreateVmResult(availability, httpCode, responseTime, vmId);
    
    return monitoredInfo;
  }
  
  private String getVmsList() {
    // Build the OCCI call
    WebTarget target = client.target(baseOcciUrl + "/compute/");
    Invocation.Builder invocationBuilder = target.request();
    invocationBuilder.header("Content-Type", "text/occi");
    invocationBuilder.header("X-Auth-Token", currentToken);
    
    // Invoke the OCCI service and measure response time
    long startTime = System.currentTimeMillis();
    Response response = invocationBuilder.get();
    long elapsedTime = System.currentTimeMillis() - startTime;
    System.out.println("Total elapsed http request/response time in milliseconds: " + elapsedTime);
    
    // Get response data
    System.out.println(response.toString());
    String message = response.readEntity(String.class);
    
    System.out.println(message);
    
    return "";
  }
  
  private InspectVmResult inspectVm(String vmId) {
    // Build the OCCI call
    WebTarget target = client.target(baseOcciUrl + "/compute/" + vmId);
    Invocation.Builder invocationBuilder = target.request();
    invocationBuilder.header("Content-Type", "text/occi");
    invocationBuilder.header("X-Auth-Token", currentToken);
    
    // Invoke the OCCI service and measure response time
    long startTime = System.currentTimeMillis();
    Response response = invocationBuilder.get();
    long responseTime = System.currentTimeMillis() - startTime;
    System.out.println("Total elapsed http request/response time in milliseconds: " + responseTime);
    
    // Get response information
    int httpCode = response.getStatus();
    int availability = 1;
    
    if (httpCode >= 400 && httpCode <= 600) {
      availability = 0;
    }
    
    // Feed monitoring info
    InspectVmResult monitoredInfo = new InspectVmResult(availability, httpCode, responseTime);
    
    // Get response message
    String message = response.readEntity(String.class);
    System.out.println(message);
    
    return monitoredInfo;
  }
  
  private DeleteVmResult deleteVm(String vmId) {
    // Build the OCCI call
    WebTarget target = client.target(baseOcciUrl + "/compute/" + vmId);
    Invocation.Builder invocationBuilder = target.request();
    invocationBuilder.header("Content-Type", "text/occi");
    invocationBuilder.header("X-Auth-Token", currentToken);
    
    // Invoke the OCCI service and measure response time
    long startTime = System.currentTimeMillis();
    Response response = invocationBuilder.delete();
    long responseTime = System.currentTimeMillis() - startTime;
    System.out.println("Total elapsed http request/response time in milliseconds: " + responseTime);
    
    // Get response information
    System.out.println(response.toString());
    int httpCode = response.getStatus();
    int availability = 1;
    
    if (httpCode >= 400 && httpCode <= 600) {
      availability = 0;
    }
    
    // Feed monitoring info
    DeleteVmResult monitoredInfo = new DeleteVmResult(availability, httpCode, responseTime);
    
    // Get response message
    String message = response.readEntity(String.class);
    System.out.println(message);
    
    return monitoredInfo;
  }
  
  /**
   * It retrieves the list of networks available. 
   */
  public void getNetworksList() {
    // Build the OCCI call
    WebTarget target = client.target(baseOcciUrl + "/network/");
    Invocation.Builder invocationBuilder = target.request();
    invocationBuilder.header("Content-Type", "text/occi");
    invocationBuilder.header("X-Auth-Token", currentToken);
    
    System.out.println(invocationBuilder.toString());
    Response response = invocationBuilder.get();
    // Get response message
    String message = response.readEntity(String.class);
    System.out.println(message);
  }
  
  /**
   * This method goes through the whole lifecycle of a VM (create, inspect 
   * and delete VM) in order to retrieve monitoring metrics: availability, 
   * HTTP response code and response time. 
   * @return Object representing results for each operation and a global 
   *     aggregation.
   */
  public OcciProbeResult getOcciMonitoringInfo() {
    // Follow the full lifecycle for a VM
    // Retrieve the operation token
    currentToken = getToken();
    if (currentToken == null) {
      return null;
    }
    //getVMsList();

    System.out.println("Token is valid. Continue monitoring operation...");
    CreateVmResult createVmInfo = createVm();
    if (createVmInfo.getCreateVmAvailability() == 0) {
      // Send failure result, since we cannot go on with the process
      OcciProbeResult failureResult = new OcciProbeResult(providerId);
      failureResult.addCreateVmInfo(createVmInfo);
      failureResult.addGlobalInfo(0, createVmInfo.getCreateVmResult(), -1);
      return failureResult;
    }
    
    InspectVmResult inspectVmInfo = inspectVm(createVmInfo.getVmId());
    DeleteVmResult deleteVmInfo = deleteVm(createVmInfo.getVmId());
    
    // Determine Global Availability
    int globalAvailability = 1;
    if (createVmInfo.getCreateVmAvailability() == 0 
        || inspectVmInfo.getInspectVmAvailability() == 0 
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
        + inspectVmInfo.getInspectVmResponseTime() 
        + deleteVmInfo.getDeleteVmResponseTime();
    
    // Construct the result
    OcciProbeResult finalResult = new OcciProbeResult(providerId);
    finalResult.addCreateVmInfo(createVmInfo);
    finalResult.addInspectVmInfo(inspectVmInfo);
    finalResult.addDeleteVmInfo(deleteVmInfo);
    finalResult.addGlobalInfo(globalAvailability, globalResult, globalResponseTime);
    
    return finalResult;
  }
  
  /**
   * Typical main for testing.
   * @param args Typical args
   */
  public static void main(String[] args) {
    // Run the OCCI monitoring process and retrieve the result
    OpenStackOcciClient myClient = new OpenStackOcciClient("https://cloud.recas.ba.infn.it:5000", "http://cloud.recas.ba.infn.it:8787", "provider-RECAS-BARI");
    myClient.getNetworksList();
    
    myClient.getOcciMonitoringInfo();
  }
}
