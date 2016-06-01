package org.indigo.occiprobe.openstack;

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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CMDBClient 
{
	private Client client = null;
	private String CMDBUrl;
	
	public CMDBClient()
	{
		// Retrieve properties
        PropertiesManager myProp = new PropertiesManager();
		CMDBUrl = myProp.getProperty(PropertiesManager.CMDB_URL);
		
		// Create the Client
		ClientConfig cc = new ClientConfig();		
		client = JerseyClientBuilder.newClient(cc);
	}
	
	public String[] getProvidersList()
	{
		// Call to CMDB API
		WebTarget target = client.target(CMDBUrl + "/provider/list");
		Invocation.Builder invocationBuilder = target.request();
		Response response = invocationBuilder.get();
		String message = response.readEntity(String.class);
		
		// Retrieve the providers list
		JsonElement jelement = new JsonParser().parse(message);
		JsonObject parsedRes = jelement.getAsJsonObject();
		JsonArray listArray = parsedRes.getAsJsonArray("rows");
		
		ArrayList<String> providersList = new ArrayList<String> ();
		Iterator <JsonElement> myIter = listArray.iterator();
        while (myIter.hasNext())
        {            	           	
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
	
	public void getProviderData (String providerId)
	{
		// Call to CMDB API
		WebTarget target = client.target(CMDBUrl + "/provider/" + providerId);
		Invocation.Builder invocationBuilder = target.request();
		Response response = invocationBuilder.get();
		String message = response.readEntity(String.class);
		
		// Retrieve the providers list
		JsonElement jelement = new JsonParser().parse(message);
		JsonObject parsedRes = jelement.getAsJsonObject();
	}
	
	public static void main(String[] args)
	{
		CMDBClient myClient = new CMDBClient();
		myClient.getProvidersList();
	}
}
