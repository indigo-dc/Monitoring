package org.indigo.heapsterprobe;

import java.util.ArrayList;
import java.util.Iterator;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class HeapsterClient 
{
	private Client client = null;
	private String baseHeapsterURL;
	
	public HeapsterClient ()
	{
		// Retrieve properties
        PropertiesManager myProp = new PropertiesManager();
		String heapsterUrl = myProp.getProperty(PropertiesManager.HEAPSTER_LOCATION);
		String heapsterPort = myProp.getProperty(PropertiesManager.HEAPSTER_PORT);
		
		// Disable issue with SSL Handshake in Java 7 and indicate certificates keystore
		System.setProperty("jsse.enableSNIExtension", "false");
		String certificatesTrustStorePath = myProp.getProperty(PropertiesManager.JAVA_KEYSTORE);
		System.setProperty("javax.net.ssl.trustStore", certificatesTrustStorePath);
		
		// Create the Client
		ClientConfig cc = new ClientConfig();		
		client = JerseyClientBuilder.newClient(cc);
		
		// Prepare access URLs		
		baseHeapsterURL = "http://" + heapsterUrl + ":" + heapsterPort + "/heapster/api/v1";
	}
	
	public String[] getPodsList ()
	{
		// Call to Heapster API
		WebTarget target = client.target(baseHeapsterURL + "/model/namespaces/");
		Invocation.Builder invocationBuilder = target.request();
		Response response = invocationBuilder.get();
		String message = response.readEntity(String.class);
		
		// Retrieve the namespaces list
		JsonElement jelement = new JsonParser().parse(message);
		JsonArray listArray = jelement.getAsJsonArray();
		System.out.println ("Number of namespaces: " + listArray.size());
		ArrayList<String> namespacesList = new ArrayList<String> ();		
		Iterator <JsonElement> myIter = listArray.iterator();
	    while (myIter.hasNext())
	    {            	           	
	    	String currentResource = myIter.next().getAsString();
	    	System.out.println ("Namespace: " + currentResource);
	    	if (!currentResource.equalsIgnoreCase("default"))
	    	{
	    		namespacesList.add(currentResource);		    	
	    	}	    	
	    }
		
		// Retrieve the pods per 'not default' namespace
		ArrayList<String> podsList = new ArrayList<String> ();
	    for (int i=0; i<namespacesList.size(); i++)
	    {
	    	// Perform the request
	    	String namespaceName = namespacesList.get(i);
	    	target = client.target(baseHeapsterURL + "/model/namespaces/" + namespaceName + "/pods/");
	    	invocationBuilder = target.request();
			response = invocationBuilder.get();
			message = response.readEntity(String.class);
			
			// Parse the result
			jelement = new JsonParser().parse(message);
			listArray = jelement.getAsJsonArray();
			System.out.println ("Number of pods: " + listArray.size());			
			myIter = listArray.iterator();
		    while (myIter.hasNext())
		    {            	           	
		    	String currentPod = myIter.next().getAsString();
		    	podsList.add(currentPod);    
		    	System.out.println("Pod: " + currentPod);
		    }			
	    }
	    
		// Provide result
		podsList.trimToSize();
		String[] resultList = new String[podsList.size()];
		podsList.toArray(resultList);
		return resultList;
	}
	
	public String[] getNodesList ()
	{
		// Call to Heapster API
		WebTarget target = client.target(baseHeapsterURL + "/model/nodes/");
		Invocation.Builder invocationBuilder = target.request();
		Response response = invocationBuilder.get();
		String message = response.readEntity(String.class);
		
		// Retrieve the nodes
		JsonElement jelement = new JsonParser().parse(message);
		JsonArray listArray = jelement.getAsJsonArray();
		System.out.println ("Number of nodes: " + listArray.size());
		String[] resultList = new String[listArray.size()];
		int pointer = 0;
		Iterator <JsonElement> myIter = listArray.iterator();
	    while (myIter.hasNext())
	    {            	           	
	    	String currentResource = myIter.next().getAsString();
	    	System.out.println ("Node: " + currentResource);
	    	resultList[pointer] = currentResource;
	    	pointer++;
	    }		 
		
		// Provide result
		return resultList;
	}
	
	public PodMetrics getPodMetrics (String podName)
	{
		return null;
	}
	
	public static void main(String[] args)
	{
		HeapsterClient myClient = new HeapsterClient ();	
		myClient.getPodsList();
	}

}
