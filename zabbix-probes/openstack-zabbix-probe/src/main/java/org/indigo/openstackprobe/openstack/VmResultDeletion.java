package org.indigo.openstackprobe.openstack;

/**
 * This class holds the information related to the monitoring of a Cloud Provider when doing the
 * Delete VM Operation.
 *
 */
public class VmResultDeletion {
  private int deleteVmAvailability;
  private int deleteVmResult;
  private long deleteVmResponseTime;

  /**
   * This method is the constructor of the class, assigning the received values to the private
   * variables.
   * 
   * @param availability It denotes whether the endpoint was available
   * @param result It represents the HTTP response code received
   * @param responseTime It indicates how much time it took to run the operation
   */
  public VmResultDeletion(int availability, int result, long responseTime) {
    deleteVmAvailability = availability;
    deleteVmResult = result;
    deleteVmResponseTime = responseTime;
  }

  /**
   * It retrieves the availability result of the Delete VM operation.
   * 
   * @return An integer indicating available (1) or unavailable (0)
   */
  public int getDeleteVmAvailability() {
    return deleteVmAvailability;
  }

  /**
   * It retrieves the HTTP response received in the operation.
   * 
   * @return Integer representing the HTTP response code
   */
  public int getDeleteVmResult() {
    return deleteVmResult;
  }

  /**
   * It retrieves the response time of the Delete VM operation.
   * 
   * @return Response time in milliseconds
   */
  public long getDeleteVmResponseTime() {
    return deleteVmResponseTime;
  }
}
