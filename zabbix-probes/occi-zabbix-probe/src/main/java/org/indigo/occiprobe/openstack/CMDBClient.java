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
	
	public ProviderInfo getProviderData (String providerId)
	{
		// Call to CMDB API
		WebTarget target = client.target(CMDBUrl + "/provider/id/" + providerId);
		Invocation.Builder invocationBuilder = target.request();
		Response response = invocationBuilder.get();
		String message = response.readEntity(String.class);
		
		System.out.println (message);
		
		// Retrieve the providers list
		JsonElement jelement = new JsonParser().parse(message);
		JsonObject parsedRes = jelement.getAsJsonObject();
		
		return null;
	}
	
	public static void main(String[] args)
	{
		CMDBClient myClient = new CMDBClient();
		String[] providers = myClient.getProvidersList();
		myClient.getProviderData(providers[0]);
	}
}
