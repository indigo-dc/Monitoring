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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class aims at implementing the management of the threads to be used for 
 * the monitoring operations. It follows a singleton pattern, so only one instance
 * of the class will exist. In this version, only one thread is used.
 * @author ATOS
 *
 */
public class ProbeThread {
  // A handle to the unique Singleton instance.  
  private static ProbeThread _instance = null;
  private HeapsterClient myClient = null;
  
  private ProbeThread() {
    myClient = new HeapsterClient();    
  }
  
  /**
   * It provides the current ProbeThread instance and, in case it does not exist,
   * it creates a new instance and gives it as the result.
   * @return The current single instance of the ProbeThread 
   */
  public static synchronized ProbeThread instance() {
    if (null == _instance) {
      _instance = new ProbeThread();
      System.out.println("A new instance of the Monitoring Probe thread was created!");
    }
    return _instance;
  }
  
  /**
   * It manages the process of retrieving the information from pods and containers,  
   * sending the metrics to Zabbix at the end of the process.
   */
  public void startMonitoringProcess() {
    ArrayList<PodMetrics> podMetricsList = new ArrayList<PodMetrics>();
    ArrayList<ContainerMetrics> containerMetricsList = new ArrayList<ContainerMetrics>();
    
    // 1 - Retrieve the list of pods
    String[] podsList = myClient.getPodsList();
    if (podsList == null) {
      System.out.println("No pods can be monitored! Finishing...");
      return;
    }
  
    // 2 - Retrieve pods metrics and containers metrics
    for (int i = 0; i < podsList.length; i ++) {
      // Get pod metrics
      PodMetrics currentPod = myClient.getPodMetrics(podsList[i]);
      podMetricsList.add(currentPod);
      
      // List containers in the pod
      String podName = currentPod.getPodName();
      String namespace = currentPod.getNamespaceName();
      String[] containersList = myClient.getContainersList(podName, namespace);
    
      // Retrieve Containers' metrics
      if (containersList != null) {
        for (int j = 0; j < containersList.length; j ++) {
          ContainerMetrics currentContainer = myClient.getContainerMetrics(podName, namespace, 
              containersList[j]);
          containerMetricsList.add(currentContainer);
        }
      }      
    }
    
    // 3 - Send metrics to Zabbix
    ZabbixSender mySender = new ZabbixSender("Indigo");
    
    // Send pods metrics
    int podFailures = 0;
    Iterator<PodMetrics> myPodIterator = podMetricsList.iterator();
    while (myPodIterator.hasNext()) {
      PodMetrics currentPod = myPodIterator.next();
      boolean success = mySender.sendPodMetrics(currentPod);
      if (!success) { 
        podFailures ++; 
      }    
    }
    System.out.println("Number of pods failed: " + podFailures);
    
    // Send containers metrics
    int containerFailures = 0;
    Iterator<ContainerMetrics> myContainerIterator = containerMetricsList.iterator();
    while (myContainerIterator.hasNext()) {
      ContainerMetrics currentContainer = myContainerIterator.next();
      boolean success = mySender.sendContainerMetrics(currentContainer);
      if (!success) {
        containerFailures ++;    
      }
    }
    System.out.println("Number of container failed: " + containerFailures);
  }  
  
  /**
   * Entry point for the probe. It will launch all the monitoring process.
   * @param args Typical args
   */
  public static void main(String[] args) {
    // Retrieve input arguments
     
    // Start the monitoring process
    ProbeThread probeManager = ProbeThread.instance();
    probeManager.startMonitoringProcess();
  }
}
