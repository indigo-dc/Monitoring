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
 * This class holds the information related to the monitoring of a Cloud
 * Provider when doing the Create VM Operation.
 * @author ATOS
 *
 */
public class CreateVmResult {
  private int createVmAvailability;
  private int createVmResult;
  private long createVmResponseTime;
  private String vmId;
  
  /**
   * This method is the constructor of the class, assigning the received values to
   * the private variables.
   * @param availability It denotes whether the endpoint was available
   * @param result It represents the HTTP response code received
   * @param responseTime It indicates how much time it took to run the operation
   * @param id It is the VM id received from the operation
   */
  public CreateVmResult(int availability, int result, long responseTime, String id) {
    createVmAvailability = availability;
    createVmResult = result;
    createVmResponseTime = responseTime;
    vmId = id;
  }
  
  /**
   * It retrieves the availability result of the Create VM operation.
   * @return An integer indicating available (1) or unavailable (0).
   */
  public int getCreateVmAvailability() {
    return createVmAvailability;
  }
  
  /**
   * It retrieves the HTTP response received in the operation.
   * @return Integer representing the HTTP response code.
   */
  public int getCreateVmResult() {
    return createVmResult;
  }
  
  /**
   * It retrieves the response time of the Create VM operation.
   * @return Response time in milliseconds.
   */
  public long getCreateVmResponseTime() {
    return createVmResponseTime;
  }
  
  /**
   * It retrieves the identifier of the created VM.
   * @return String representing the identifier.
   */
  public String getVmId() {
    return vmId;
  }
}
