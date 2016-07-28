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
 * This class holds the information related to the monitorization of a container.
 * Basically, it contains the monitored information, so it can be used and
 * processed by other classes.
 * @author ATOS
 *
 */
public class ContainerMetrics {

  private String containerName;  
  private float cpuUsageRate;
  private int cpuRequest;
  private long cpuUsage;
  private long memoryRequest;
  private int memoryLimit;
  private int majorPageFaults;
  private int pageFaults;
  private float majorPageFaultsRate;
  private float pageFaultsRate;
  private int memoryUsage;
  private int workingSet;
  private int uptime;  
  
  /**
   * This is the class constructor, which gathers all the monitored information and
   * assigns it to the corresponding private variables.
   * @param name Name of the container as in the Kubernetes cluster
   * @param cpuRate Percentage of requested CPU used
   * @param cpuReq CPU amount required
   * @param cpuUsed CPU under use
   * @param memReq Amount of memory required
   * @param memLimit Limit set to memory use
   * @param majorMemFaults Number of major memory page faults detected in the container
   * @param memFaults Number of memory page faults detected in the container
   * @param majorMemFaultsRate Percentage of major memory page faults
   * @param pageMemFaultsRate Percentage of memory page faults
   * @param memUsed Amount of memory under use
   * @param workSet Working set
   * @param time Amount of seconds the container has been available so far
   */
  public ContainerMetrics(String name, float cpuRate, int cpuReq, long cpuUsed, long memReq, 
      int memLimit, int majorMemFaults, int memFaults, float majorMemFaultsRate, 
      float pageMemFaultsRate, int memUsed, int workSet, int time) {
    
    containerName = name;
    cpuUsageRate = cpuRate;
    cpuRequest = cpuReq;
    cpuUsage = cpuUsed;
    memoryRequest = memReq;
    memoryLimit = memLimit;
    majorPageFaults = majorMemFaults;
    pageFaults = memFaults;
    majorPageFaultsRate = majorMemFaultsRate;
    pageFaultsRate = pageMemFaultsRate;
    memoryUsage = memUsed;
    workingSet = workSet;
    uptime = time;
  }
  
  /**
   * This is the class constructor, which gathers all the monitored information and
   * assigns it to the corresponding private variables.
   * @param name Name of the container as in the Kubernetes cluster
   * @param cpuRate Percentage of requested CPU used   
   * @param cpuUsed CPU under use  
   * @param majorMemFaults Number of major memory page faults detected in the container
   * @param memFaults Number of memory page faults detected in the container
   * @param majorMemFaultsRate Percentage of major memory page faults
   * @param pageMemFaultsRate Percentage of memory page faults
   * @param memUsed Amount of memory under use
   * @param workSet Working set
   * @param time Amount of seconds the container has been available so far
   */
  public ContainerMetrics(String name, float cpuRate, long cpuUsed,  
      int majorMemFaults, int memFaults, float majorMemFaultsRate, 
      float pageMemFaultsRate, int memUsed, int workSet, int time) {
    
    containerName = name;
    cpuUsageRate = cpuRate;    
    cpuUsage = cpuUsed;    
    majorPageFaults = majorMemFaults;
    pageFaults = memFaults;
    majorPageFaultsRate = majorMemFaultsRate;
    pageFaultsRate = pageMemFaultsRate;
    memoryUsage = memUsed;
    workingSet = workSet;
    uptime = time;
  }
  
  /**
   * It provides the name of the container which produced the information.
   * @return Name of the container as registered in the Kubernetes cluster
   */
  public String getContainerName() {
    return containerName;    
  }
  
  /**
   * It provides the usage rate of the requested CPU.
   * @return CPU usage on all cores in millicores
   */
  public float getCpuUsageRate() {
    return cpuUsageRate;
  }
  
  /**
   * It provides the amount representing the requested CPU.
   * @return Amount of CPU requested in millicores
   */
  public int getCpuRequest() {
    return cpuRequest;
  }
  
  /**
   * It provides the amount of CPU in use.
   * @return CPU usage in millicores
   */
  public long getCpuUsage() {
    return cpuUsage;
  }
  
  /**
   * It provides the amount of memory requested.
   * @return Memory requested in bytes
   */
  public long getMemoryRequest() {
    return memoryRequest;
  }
  
  /**
   * It provides the memory hard limit set.
   * @return Memory limit in bytes
   */
  public int getMemoryLimit() {
    return memoryLimit;
  }
  
  /**
   * It provides the number of major memory page faults.
   * @return Number of major page faults
   */
  public int getMajorPageFaults() {
    return majorPageFaults;
  }
  
  /**
   * It provides the number of memory page faults.
   * @return Number of page faults
   */
  public int getPageFaults() {
    return pageFaults;
  }
  
  /**
   * It provides the rate of major page faults.
   * @return Number of major page faults per second
   */
  public float getMajorPageFaultsRate() {
    return majorPageFaultsRate;
  }
  
  /**
   * It provides the rate of page faults.
   * @return Number of page faults per second
   */
  public float getPageFaultsRate() {
    return pageFaultsRate;
  }
  
  /**
   * It provides the memory usage.
   * @return Total memory usage in bytes
   */
  public int getMemoryUsage() {
    return memoryUsage;
  }
  
  /**
   * It provides the working set usage. Heapster defines working set as the 
   * "memory being used and not easily dropped by the kernel".
   * @return Working set in bytes
   */
  public int getWorkingset() {
    return workingSet;
  }
  
  /**
   * It provides information about how much time the container has been available.
   * @return Number of milliseconds since the container was started
   */
  public int getUptime() {
    return uptime;
  }  
}
