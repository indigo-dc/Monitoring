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
 * This class holds the information related to the monitorization of a pod.
 * Basically, it contains the monitored information, so it can be used and
 * processed by other classes.
 * @author ATOS
 *
 */
public class PodMetrics {

  String podName;
  String namespaceName;
  int networkTxErrors;
  float networkTxErrorsRate;
  int networkRxErrors;
  float networkRxErrorsRate;
  float memoryMajorPageFaultsRate;
  float memoryPageFaultsRate;
  int uptime;

  /**
   * This is the main constructor of the class, which gathers all the information which 
   * can be monitored in a pod.
   * @param name Name of the pod as registered in the Kubernetes cluster
   * @param space Name of the namespace where the pod belongs to
   * @param txErrors Integer with the transmission errors (network)
   * @param txErrorsRate Percentage of transmission errors (network)
   * @param rxErrors Number of errors in messages reception (network)
   * @param rxErrorsRate Percentage of error in messages reception (network)
   * @param majorPageFaults Percentage of major page faults in memory
   * @param pageFaults Percentage of page faults in memory
   * @param theUptime Number of seconds the pod has been available so far
   */
  public PodMetrics(String name, String space, int txErrors, float txErrorsRate, int rxErrors,
      float rxErrorsRate, float majorPageFaults, float pageFaults, int theUptime) {
    podName = name;
    namespaceName = space;
    networkTxErrors = txErrors;
    networkTxErrorsRate = txErrorsRate;
    networkRxErrors = rxErrors;
    networkRxErrorsRate = rxErrorsRate;
    memoryMajorPageFaultsRate = majorPageFaults;
    memoryPageFaultsRate = pageFaults;
    uptime = theUptime;
  }

  /**
   * It provides the name of the monitored pod.
   * @return string with the pod name, as it is in the Kubernetes cluster
   */
  public String getPodName() {
    return podName;
  }

  /**
   * It provides the number of network transmission errors detected.
   * @return Number of errors detected
   */
  public int getNetworkTxErrors() {
    return networkTxErrors;
  }

  /**
   * It provides the network transmission errors rate detected.
   * @return Percentage of errors detected, with respect to all messages sent
   */
  public float getNetworkTxErrorsRate() {
    return networkTxErrorsRate;
  }

  /**
   * It provides the number of network reception errors detected.
   * @return Number of errors detected
   */
  public int getNetworkRxErrors() {
    return networkRxErrors;
  }

  /**
   * It provides the network reception errors rate detected.
   * @return Percentage of errors detected, with respect to all messages received
   */
  public float getNetworkRxErrorsRate() {
    return networkRxErrorsRate;
  }

  /**
   * It provides the rate of major memory page faults.
   * @return Percentage of major page faults
   */
  public float getMemoryMajorPageFaultsRate() {
    return memoryMajorPageFaultsRate;
  }

  /**
   * It provides the rate of memory page faults.
   * @return Percentage of page faults
   */
  public float getMemoryPageFaultsRate() {
    return memoryPageFaultsRate;
  }

  /**
   * It provides how much time the pod has been available in the cluster.
   * @return Number of seconds the pod has been available.
   */
  public int getUptime() {
    return uptime;
  }
}
