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

import org.apache.commons.lang3.BooleanUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the whole result of a monitoring action which has 
 * followed the full VM lifecycle by using the OCCI API exposed by a 
 * Cloud provider. It keeps information about each operation (Create, Inspect 
 * and Delete VM) and about a global evaluation.
 * @author ATOS
 *
 */
public class OcciProbeResult {
  private boolean globalAvailability;
  private int globalResult;
  private long globalResponseTime;


  public void setGlobalAvailability(boolean globalAvailability) {
    this.globalAvailability = globalAvailability;
  }

  public void setGlobalResult(int globalResult) {
    this.globalResult = globalResult;
  }

  public void setGlobalResponseTime(long globalResponseTime) {
    this.globalResponseTime = globalResponseTime;
  }

  /**
   * This is an empty constructor for the class.
   */
  
  /**
   * This is a constructor that takes as input the global values for the VMs
   * lifecycle operation (create, inspect and delete VM).
   * @param availability 1 if all the operations were available, 0 if not
   * @param result It is a global HTTP response code, the most problematic one.
   * @param responseTime Sum of the response time of all calls, in milliseconds.
   */
  public OcciProbeResult(boolean availability, int result, long responseTime) {
    globalAvailability = availability;
    globalResult = result;
    globalResponseTime = responseTime;
  }
  
  /**
   * This method sets the global values of the monitoring operation, as it
   * represents a kind of aggregation of monitored values for each operation.
   * @param availability 1 if all the operations were available, 0 if not
   * @param result It is a global HTTP response code, the most problematic one.
   * @param responseTime Sum of the response time of all calls, in milliseconds.
   */
  public void addGlobalInfo(boolean availability, int result, long responseTime) {
    globalAvailability = availability;
    globalResult = result;
    globalResponseTime = responseTime;
  }
  
  /**
   * It retrieves the availability result of the global monitoring.
   * @return A boolean indicating all operations available or some operation unavailable
   */
  public boolean getGlobalAvailability() {
    return globalAvailability;
  }
  
  /**
   * It retrieves the HTTP response set for the global group of operations.
   * @return Integer representing the HTTP response code
   */
  public int getGlobalResult() {
    return globalResult;
  }
  
  /**
   * It retrieves the response time as result of summing up all the operations
   * performed for the monitoring.
   * @return Response time in milliseconds
   */
  public long getGlobalResponseTime() {
    return globalResponseTime;
  }


  public Map<String, String> getMetrics() {

    Map<String, String> values = new HashMap<>();

    values.put("occi.global[availability]", BooleanUtils
        .toIntegerObject(globalAvailability).toString());
    values.put("occi.global[result]", Integer.toString(globalResult));
    values.put("occi.global[responseTime]", Long.toString(globalResponseTime));

    return values;
  }
}
