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
  
  
  private ProbeThread() {
    // Build element for thread and tasks scheduling
    
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
    // Retrieve the list of providers with their info
    
   
    
    
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
