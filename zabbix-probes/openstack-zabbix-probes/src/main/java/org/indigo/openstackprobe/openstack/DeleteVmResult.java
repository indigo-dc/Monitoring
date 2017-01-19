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

package org.indigo.openstackprobe.openstack;

/**
 * This class holds the information related to the monitoring of a Cloud
 * Provider when doing the Delete VM Operation.
 * @author ATOS
 *
 */
public class DeleteVmResult {
  private int deleteVmAvailability;
  private int deleteVmResult;
  private long deleteVmResponseTime;
  
  /**
   * This method is the constructor of the class, assigning the received values to
   * the private variables.
   * @param availability It denotes whether the endpoint was available
   * @param result It represents the HTTP response code received
   * @param responseTime It indicates how much time it took to run the operation
   */
  public DeleteVmResult(int availability, int result, long responseTime) {
    deleteVmAvailability = availability;
    deleteVmResult = result;
    deleteVmResponseTime = responseTime;
  }
  
  /**
   * It retrieves the availability result of the Delete VM operation.
   * @return An integer indicating available (1) or unavailable (0)
   */
  public int getDeleteVmAvailability() {
    return deleteVmAvailability;
  }
  
  /**
   * It retrieves the HTTP response received in the operation.
   * @return Integer representing the HTTP response code
   */
  public int getDeleteVmResult() {
    return deleteVmResult;
  }
  
  /**
   * It retrieves the response time of the Delete VM operation.
   * @return Response time in milliseconds
   */
  public long getDeleteVmResponseTime() {
    return deleteVmResponseTime;
  }  
}
