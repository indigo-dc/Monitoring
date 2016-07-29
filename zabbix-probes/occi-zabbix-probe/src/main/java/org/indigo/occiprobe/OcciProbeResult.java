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

/**
 * This class represents the whole result of a monitoring action which has 
 * followed the full VM lifecycle by using the OCCI API exposed by a 
 * Cloud provider. It keeps information about each operation (Create, Inspect 
 * and Delete VM) and about a global evaluation.
 * @author ATOS
 *
 */
public class OcciProbeResult {
  private int globalAvailability;
  private int globalResult;
  private long globalResponseTime;
  
  private CreateVmResult createVmElement;
  private InspectVmResult inspectVmElement;
  private DeleteVmResult deleteVmElement;
  
  /**
   * This is an empty constructor for the class.
   */
  public OcciProbeResult(){
  }
  
  /**
   * This is a constructor that takes as input the global values for the VMs
   * lifecycle operation (create, inspect and delete VM).
   * @param availability 1 if all the operations were available, 0 if not
   * @param result It is a global HTTP response code, the most problematic one.
   * @param responseTime Sum of the response time of all calls, in milliseconds.
   */
  public OcciProbeResult(int availability, int result, long responseTime) {
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
  public void addGlobalInfo(int availability, int result, long responseTime) {
    globalAvailability = availability;
    globalResult = result;
    globalResponseTime = responseTime;
  }
  
  /**
   * This method sets the monitoring information related to the operation for
   * creating a VM.
   * @param result It represents availability, HTTP response code and response time for the
   *     create VM operation.
   */
  public void addCreateVmInfo(CreateVmResult result) {
    createVmElement = result;
  }
  
  /**
   * This method sets the monitoring information related to the operation for
   * inspecting a VM.
   * @param result It represents availability, HTTP response code and response time for the
   *     Inspect VM operation.
   */
  public void addInspectVmInfo(InspectVmResult result) {
    inspectVmElement = result;
  }
  
  /**
   * This method sets the monitoring information related to the operation for
   * deleting a VM.
   * @param result It represents availability, HTTP response code and response time for the
   *     Delete VM operation.
   */
  public void addDeleteVmInfo(DeleteVmResult result) {
    deleteVmElement = result;
  }
  
  /**
   * It retrieves the availability result of the global monitoring.
   * @return An integer indicating all operations available (1) or some operation unavailable (0)
   */
  public int getGlobalAvailability() {
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
  
  /**
   * It retrieves the monitoring result for the operation Create VM.
   * @return Object with availability, HTTP code and response time.
   */
  public CreateVmResult getCreateVmElement() {
    return createVmElement;
  }
  
  /**
   * It retrieves the monitoring result for the operation Inspect VM.
   * @return Object with availability, HTTP code and response time.
   */
  public InspectVmResult getInspectVmElement() {
    return inspectVmElement;
  }
  
  /**
   * It retrieves the monitoring result for the operation Delete VM.
   * @return Object with availability, HTTP code and response time.
   */
  public DeleteVmResult getDeleteVmElement() {
    return deleteVmElement;
  }
}
