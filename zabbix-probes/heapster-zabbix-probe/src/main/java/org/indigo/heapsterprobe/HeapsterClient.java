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

package org.indigo.heapsterprobe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * This class is in charge of the interactions with Heapster by using its REST API.
 * The client retrieves the list of pods and the list of containers per pod, requesting
 * also access to the metrics of both pods and containers.
 * @author ATOS
 *
 */
public class HeapsterClient {

  private Client client = null;
  private String baseHeapsterUrl;
  
  /**
   * This is the main constructor of the class. It retrieves the properties indicating
   * the Heapster endpoint and it creates the base client for the REST queries.
   */
  public HeapsterClient() {
    // Retrieve properties
    PropertiesManager myProp = new PropertiesManager();
    String heapsterUrl = myProp.getProperty(PropertiesManager.HEAPSTER_LOCATION);
    String heapsterPort = myProp.getProperty(PropertiesManager.HEAPSTER_PORT);

    // Prepare access URLs
    //baseHeapsterUrl = "http://" + heapsterUrl + ":" + heapsterPort + "/heapster/api/v1";
    baseHeapsterUrl = heapsterUrl + "/heapster/api/v1";
    
    // Disable issue with SSL Handshake in Java 7 and indicate certificates keystore
    System.setProperty("jsse.enableSNIExtension", "false");
    String certificatesTrustStorePath = myProp.getProperty(PropertiesManager.JAVA_KEYSTORE);
    System.setProperty("javax.net.ssl.trustStore", certificatesTrustStorePath);

    // Create the Client
    ClientConfig cc = new ClientConfig();
    client = JerseyClientBuilder.newClient(cc);
  }

  /**
   * This method access the Heapster REST API in order to obtain the list of pods 
   * available in the Kubernetes cluster.
   * @return List of pod identifiers
   */
  public String[] getPodsList() {
    // Call to Heapster API
    WebTarget target = client.target(baseHeapsterUrl + "/model/namespaces/");
    Invocation.Builder invocationBuilder = target.request();
    Response response = invocationBuilder.get();
    String message = response.readEntity(String.class);

    // Retrieve the namespaces list
    JsonElement jelement = new JsonParser().parse(message);
    JsonArray listArray = jelement.getAsJsonArray();
    System.out.println("Number of namespaces: " + listArray.size());
    ArrayList<String> namespacesList = new ArrayList<String>();
    Iterator<JsonElement> myIter = listArray.iterator();
    while (myIter.hasNext()) {
      String currentResource = myIter.next().getAsString();
      System.out.println("Namespace: " + currentResource);
      if (!currentResource.equalsIgnoreCase("default")) {
        namespacesList.add(currentResource);
      }
    }

    // Retrieve the pods per 'not default' namespace
    ArrayList<String> podsList = new ArrayList<String>();
    for (int i = 0; i < namespacesList.size(); i++) {
      // Perform the request
      String namespaceName = namespacesList.get(i);
      target = client.target(baseHeapsterUrl + "/model/namespaces/" + namespaceName + "/pods/");
      invocationBuilder = target.request();
      response = invocationBuilder.get();
      message = response.readEntity(String.class);

      // Parse the result
      jelement = new JsonParser().parse(message);
      listArray = jelement.getAsJsonArray();
      System.out.println("Number of pods: " + listArray.size());
      myIter = listArray.iterator();
      while (myIter.hasNext()) {
        String currentPod = myIter.next().getAsString();
        podsList.add(namespaceName + "/" + currentPod);
        System.out.println("Pod: " + currentPod);
      }
    }

    // Provide result
    podsList.trimToSize();
    String[] resultList = new String[podsList.size()];
    podsList.toArray(resultList);
    return resultList;
  }

  /**
   * This method access the Heapster REST API in order to list the physical
   * nodes which are part of the Kubernetes cluster.
   * @return List of nodes identifiers.
   */
  public String[] getNodesList() {
    // Call to Heapster API
    WebTarget target = client.target(baseHeapsterUrl + "/model/nodes/");
    Invocation.Builder invocationBuilder = target.request();
    Response response = invocationBuilder.get();
    String message = response.readEntity(String.class);

    // Retrieve the nodes
    JsonElement jelement = new JsonParser().parse(message);
    JsonArray listArray = jelement.getAsJsonArray();
    System.out.println("Number of nodes: " + listArray.size());
    String[] resultList = new String[listArray.size()];
    int pointer = 0;
    Iterator<JsonElement> myIter = listArray.iterator();
    while (myIter.hasNext()) {
      String currentResource = myIter.next().getAsString();
      System.out.println("Node: " + currentResource);
      resultList[pointer] = currentResource;
      pointer++;
    }

    // Provide result
    return resultList;
  }
  
  /**
   * This method accesses each metric available for a pod, in order to
   * monitor its current status.
   * @param podName String with the pod identifier.
   * @return Object with all the gathered metrics.
   */
  public PodMetrics getPodMetrics(String podName) {    
    // Separate Pod and Namespace
    String[] podSplit = podName.split("/");
    String namespace = podSplit[0];
    String pod = podSplit[1];

    // List the metrics available for the pod
    String podUrl = baseHeapsterUrl + "/model/namespaces/" + namespace + "/pods/" + pod + "/";
    System.out.println(podUrl + "metrics/");
    WebTarget target = client.target(podUrl + "metrics/");
    Invocation.Builder invocationBuilder = target.request();
    Response response = invocationBuilder.get();
    String message = response.readEntity(String.class);
    System.out.println(message);
    
    // Prepare the result variables
    int txErrors = -1;
    float txErrorsRate = -1.0f;
    int rxErrors = -1;
    float rxErrorsRate = -1.0f;
    float majorPageFaults = -1.0f;
    float pageFaults = -1.0f;
    int uptime = 0;
    
    // Get the metrics list checking all are available      
    JsonElement jelement = new JsonParser().parse(message);   
    JsonArray listArray = jelement.getAsJsonArray();
    System.out.println("Number of metrics: " + listArray.size());
    
    if (listArray.size() == 0) {
      return null;
    }
    
    HashMap<String, String> metricsList = new HashMap<String, String>();    
    Iterator<JsonElement> myIter = listArray.iterator();
    while (myIter.hasNext()) {
      String currentMetric = myIter.next().getAsString();
      System.out.println("Metric: " + currentMetric);
      String metricUrl = podUrl + "metrics/" + currentMetric + "/";
      metricsList.put(currentMetric, metricUrl);      
    }
    
    // Retrieve metrics
    // Retrieve Tx Errors
    if (metricsList.containsKey("network/tx_errors")) {
      JsonObject measurementObject = retrieveMetric(metricsList.get("network/tx_errors"));
      txErrors = measurementObject.get("value").getAsInt();
      System.out.println("Tx Err Value: " + txErrors);       
    }    
    
    // Retrieve Tx Errors Rate
    if (metricsList.containsKey("network/tx_errors_rate")) {
      JsonObject measurementObject = retrieveMetric(metricsList.get("network/tx_errors_rate"));
      txErrorsRate = measurementObject.get("value").getAsFloat();
      System.out.println("Tx Err Rate Value: " + txErrorsRate);       
    }
    
    // Retrieve Rx Errors
    if (metricsList.containsKey("network/rx_errors")) {
      JsonObject measurementObject = retrieveMetric(metricsList.get("network/rx_errors"));
      rxErrors = measurementObject.get("value").getAsInt();
      System.out.println("Rx Err Value: " + rxErrors);       
    }
    
    // Retrieve Rx Errors Rate
    if (metricsList.containsKey("network/rx_errors_rate")) {
      JsonObject measurementObject = retrieveMetric(metricsList.get("network/rx_errors_rate"));
      rxErrorsRate = measurementObject.get("value").getAsFloat();
      System.out.println("Rx Err Rate Value: " + rxErrorsRate);       
    }
    
    // Retrieve Memory Major Fault Rate
    if (metricsList.containsKey("memory/major_page_faults_rate")) {
      JsonObject measurementObject = retrieveMetric(
          metricsList.get("memory/major_page_faults_rate"));
      majorPageFaults = measurementObject.get("value").getAsFloat();
      System.out.println("Major Page Faults Rate Value: " + majorPageFaults);       
    }
    
    // Retrieve Memory Fault Rate
    if (metricsList.containsKey("memory/page_faults_rate")) {
      JsonObject measurementObject = retrieveMetric(metricsList.get("memory/page_faults_rate"));
      pageFaults = measurementObject.get("value").getAsFloat();
      System.out.println("Memory Page Faults Rate Value: " + pageFaults);       
    }
    
    // Retrieve Uptime
    if (metricsList.containsKey("uptime")) {
      JsonObject measurementObject = retrieveMetric(metricsList.get("uptime"));
      uptime = measurementObject.get("value").getAsInt();
      System.out.println("Uptime Value: " + uptime);       
    }
    
    // Build result
    PodMetrics podResult = new PodMetrics(pod, namespace, txErrors, txErrorsRate, rxErrors, 
        rxErrorsRate, majorPageFaults, pageFaults, uptime);
    return podResult;
  }
  
  private JsonObject retrieveMetric(String metricUrl) {
    // Invoke the Heapster API with the metric absolute URL
    WebTarget metricTarget = client.target(metricUrl);
    Invocation.Builder metricInvocationBuilder = metricTarget.request();
    Response metricResponse = metricInvocationBuilder.get();
    String metricMessage = metricResponse.readEntity(String.class);
      
    // Extract the last metric given by Heapster
    JsonElement jsonMetrics = new JsonParser().parse(metricMessage);
    JsonObject parsedMetrics = jsonMetrics.getAsJsonObject();
    JsonArray metricsArray = parsedMetrics.getAsJsonArray("metrics");
    int numMeasurements = metricsArray.size();
    JsonElement lastMeasurement = metricsArray.get(numMeasurements - 1);
    return lastMeasurement.getAsJsonObject();      
  }

  /**
   * Typical main method for simple testing purposes.
   * @param args Typical input arguments
   */
  public static void main(String[] args) {
    HeapsterClient myClient = new HeapsterClient();
    myClient.getPodsList();
    myClient.getPodMetrics("kube-system/monitoring-influxdb-grafana-v3-fpd4q");
  }

}
