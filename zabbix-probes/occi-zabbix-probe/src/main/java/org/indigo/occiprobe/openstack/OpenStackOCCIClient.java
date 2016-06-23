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

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;

import org.openstack4j.api.OSClient;
import org.openstack4j.openstack.OSFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class OpenStackOCCIClient 
{
	private Client client = null;
	private String baseOCCIURL;
	private String baseKeystoneURL;	
	private String openStackUser = "";
	private String openStackPwd = "";
	private String currentToken = "";
		
	public OpenStackOCCIClient (String keystoneLocation, String occiLocation)
	{
		// Retrieve properties
        PropertiesManager myProp = new PropertiesManager();
		//String keystoneUrl = myProp.getProperty(PropertiesManager.KEYSTONE_LOCATION);
		//String keystonePort = myProp.getProperty(PropertiesManager.KEYSTONE_PORT);
		//String occiUrl = myProp.getProperty(PropertiesManager.OCCI_LOCATION);
		//String occiPort = myProp.getProperty(PropertiesManager.OCCI_PORT);
		openStackUser = myProp.getProperty(PropertiesManager.OPENSTACK_USER);
		openStackPwd = myProp.getProperty(PropertiesManager.OPENSTACK_PASSWORD);
        
		// Disable issue with SSL Handshake in Java 7 and indicate certificates keystore
		System.setProperty("jsse.enableSNIExtension", "false");
		String certificatesTrustStorePath = myProp.getProperty(PropertiesManager.JAVA_KEYSTORE);
		System.setProperty("javax.net.ssl.trustStore", certificatesTrustStorePath);
		
		// Create the Client
		ClientConfig cc = new ClientConfig();
		//cc.getClasses().add(JsonObject.class);
		client = JerseyClientBuilder.newClient(cc);
		
		// Prepare access URLs		
		//baseKeystoneURL = keystoneUrl + ":" + keystonePort + "/v2.0";
        //baseOCCIURL = occiUrl + ":" + occiPort;
		baseKeystoneURL = keystoneLocation + "/v2.0";
        baseOCCIURL = occiLocation;
		
        // Retrieve the operation token
        currentToken = getToken();
                       
	}
	
	public OpenStackOCCIClient (Client mockClient)
	{
		client = mockClient;
	}
	
	private String getToken()
	{
		/*
		// Build JSON input
		TokenRequest tRequest= new TokenRequest("INDIGO", openStackUser, openStackPwd);
		Gson gson = new Gson();
		String input = gson.toJson(tRequest);
		System.out.println (input);
		
		// Call to Keystone
		WebTarget target = client.target(baseKeystoneURL + "/tokens");
		System.out.println (target.toString());
		Response response =  target.request(MediaType.APPLICATION_JSON).post(Entity.entity(input, MediaType.APPLICATION_JSON), Response.class); 
		
		//String result = r.accept(MediaType.APPLICATION_JSON_TYPE).get(String.class);
		
		//JsonElement jelement = new JsonParser().parse(response.);
		//System.out.println(jelement.toString());
		 * 
		 */
		
		// Authenticate
		/*
		OSClient os = OSFactory.builder()
		                  .endpoint(baseKeystoneURL)
		                  .credentials(openStackUser,openStackPwd)
		                  .tenantName("INDIGO")
		                  .authenticate();
		*/
		
		OSClient os = OSFactory.builder()
                .endpoint(baseKeystoneURL)
                .credentials(openStackUser,openStackPwd)
                .tenantName("INDIGO_DEMO")
                .authenticate();
		
		System.out.println(os.getToken().toString());
		
		return os.getToken().getId();
	}
	
	private CreateVMResult createVM ()
	{		
		// Call to OCCI API
		WebTarget target = client.target(baseOCCIURL + "/occi/compute/");
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
	 		
	 	if (httpCode>=400 && httpCode <= 600)
	 	{
	 		availability = 0;
	 	}
	 		 	 		 		
	 	// Get response message
	 	String message = response.readEntity(String.class);
	 	System.out.println (message);
		
	 	String[] vmUrlParts = message.split("/");
	 	String vmId = vmUrlParts[vmUrlParts.length-1];
	 	System.out.println ("Created VM: " + vmId);
	 	
	 	// Feed monitoring info
	 	CreateVMResult monitoredInfo = new CreateVMResult (availability, httpCode, responseTime, vmId);
	 	
		return monitoredInfo;
	}
	
	private String getVMsList ()
	{		
		// Build the OCCI call
		WebTarget target = client.target(baseOCCIURL + "/occi/compute/");
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
		
		System.out.println (message);
		
		return "";
	}
	
	private InspectVMResult inspectVM (String vmId)
	{
		// Build the OCCI call
		WebTarget target = client.target(baseOCCIURL + "/occi/compute/" + vmId);
		Invocation.Builder invocationBuilder = target.request();
		invocationBuilder.header("Content-Type", "text/occi");
		invocationBuilder.header("X-Auth-Token", currentToken);		
		
		//System.out.println(invocationBuilder.toString());
		
		// Invoke the OCCI service and measure response time
		long startTime = System.currentTimeMillis();
		Response response = invocationBuilder.get();
		long responseTime = System.currentTimeMillis() - startTime;
	    System.out.println("Total elapsed http request/response time in milliseconds: " + responseTime);
	    
		// Get response information		
		int httpCode = response.getStatus();
		int availability = 1;
		
		if (httpCode>=400 && httpCode <= 600)
		{
			availability = 0;
		}
		
		// Feed monitoring info
		InspectVMResult monitoredInfo = new InspectVMResult (availability, httpCode, responseTime);
				
		// Get response message
		String message = response.readEntity(String.class);
		System.out.println (message);
				
		return monitoredInfo;
	}
	
	private DeleteVMResult deleteVM (String vmId)
	{
		// Build the OCCI call
		WebTarget target = client.target(baseOCCIURL + "/occi/compute/" + vmId);
		Invocation.Builder invocationBuilder = target.request();
		invocationBuilder.header("Content-Type", "text/occi");
		invocationBuilder.header("X-Auth-Token", currentToken);
				
		// System.out.println(invocationBuilder.toString());
		
		// Invoke the OCCI service and measure response time
		long startTime = System.currentTimeMillis();
		Response response = invocationBuilder.delete();
		long responseTime = System.currentTimeMillis() - startTime;
	    System.out.println("Total elapsed http request/response time in milliseconds: " + responseTime);
		
	    // Get response information
	 	System.out.println(response.toString());
	 	int httpCode = response.getStatus();
	 	int availability = 1;
	 		
	 	if (httpCode>=400 && httpCode <= 600)
	 	{
	 		availability = 0;
	 	}
	 		
	 	// Feed monitoring info
	 	DeleteVMResult monitoredInfo = new DeleteVMResult (availability, httpCode, responseTime);
	 		 		
	 	// Get response message
	 	String message = response.readEntity(String.class);
	 	System.out.println (message);
		
		return monitoredInfo;
	}
	
	public void getNetworksList()
	{
		// Build the OCCI call
		WebTarget target = client.target(baseOCCIURL + "/occi/network/");
		Invocation.Builder invocationBuilder = target.request();
		invocationBuilder.header("Content-Type", "text/occi");
		invocationBuilder.header("X-Auth-Token", currentToken);
						
		System.out.println(invocationBuilder.toString());
		Response response = invocationBuilder.get();
		// Get response message
		String message = response.readEntity(String.class);
		System.out.println (message);
		
	}
	
	public OCCIProbeResult getOCCIMonitoringInfo()
	{
		// Follow the full lifecycle for a VM
		//getVMsList();
		CreateVMResult createVMInfo = createVM();
		if (createVMInfo.getCreateVMAvailability()==0)
		{
			// Send failure result, since we cannot go on with the process
			OCCIProbeResult failureResult = new OCCIProbeResult();
			failureResult.addCreateVMInfo(createVMInfo);			
			failureResult.addGlobalInfo(0, createVMInfo.getCreateVMResult(), -1);
			return failureResult;
		}
		
		InspectVMResult inspectVMInfo = inspectVM(createVMInfo.getVmId());
		DeleteVMResult deleteVMInfo = deleteVM(createVMInfo.getVmId());
		
		// Determine Global Availability
		int globalAvailability = 1;
		if (createVMInfo.getCreateVMAvailability() == 0 || inspectVMInfo.getInspectVMAvailability() == 0 || deleteVMInfo.getDeleteVMAvailability() == 0)
		{
			globalAvailability = 0;
		}
		
		// Determine Global Result
		int globalResult = 200;
		int createVMResult = createVMInfo.getCreateVMResult();
		int inspectVMResult = inspectVMInfo.getInspectVMResult();
		int deleteVMResult = deleteVMInfo.getDeleteVMResult();
		if (createVMResult > inspectVMResult)
		{
			if (createVMResult > deleteVMResult)
			{
				globalResult = createVMResult;
			}
			else
			{
				globalResult = deleteVMResult;
			}
		}
		else
		{
			if (inspectVMResult > deleteVMResult)
			{
				globalResult = inspectVMResult;
			}
			else
			{
				globalResult = deleteVMResult;
			}
		}
		
		// Determine Global ResponseTime
		long globalResponseTime = createVMInfo.getCreateVMResponseTime() + inspectVMInfo.getInspectVMResponseTime() + deleteVMInfo.getDeleteVMResponseTime();
		
		// Construct the result
		OCCIProbeResult finalResult = new OCCIProbeResult();
		finalResult.addCreateVMInfo(createVMInfo);
		finalResult.addInspectVMInfo(inspectVMInfo);
		finalResult.addDeleteVMInfo(deleteVMInfo);
		finalResult.addGlobalInfo(globalAvailability, globalResult, globalResponseTime);
		
		return finalResult;
	}	
	
	
	public static void main(String[] args)
	{
		// Run the OCCI monitoring process and retrieve the result
		OpenStackOCCIClient myClient = new OpenStackOCCIClient("https://cloud.recas.ba.infn.it:5000", "http://cloud.recas.ba.infn.it:8787");
		myClient.getNetworksList();
		
		myClient.getOCCIMonitoringInfo();
	}
	
}
