package org.indigo.openstackprobe.openstack;

/**
 * This class holds the information related to the monitoring of a Cloud Provider when doing the
 * Inspect VM Operation.
 */
public class InspectVmResult {
  private int inspectVmAvailability;
  private int inspectVmResult;
  private long inspectVmResponseTime;

  /**
   * This method is the constructor of the class, assigning the received values to the private
   * variables.
   * 
   * @param availability It denotes whether the endpoint was available
   * @param result It represents the HTTP response code received
   * @param responseTime It indicates how much time it took to run the operation
   */
  public InspectVmResult(int availability, int result, long responseTime) {
    inspectVmAvailability = availability;
    inspectVmResult = result;
    inspectVmResponseTime = responseTime;
  }

  /**
   * It retrieves the availability result of the Inspect VM operation.
   * 
   * @return An integer indicating available (1) or unavailable (0)
   */
  public int getInspectVmAvailability() {
    return inspectVmAvailability;
  }

  /**
   * It retrieves the HTTP response received in the operation.
   * 
   * @return Integer representing the HTTP response code
   */
  public int getInspectVmResult() {
    return inspectVmResult;
  }

  /**
   * It retrieves the response time of the Inspect VM operation.
   * 
   * @return Response time in milliseconds
   */
  public long getInspectVmResponseTime() {
    return inspectVmResponseTime;
  }
}
