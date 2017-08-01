package org.indigo.openstackprobe.openstack;

/**
 * This class holds the information related to the monitoring of a Cloud Provider when doing the
 * Create VM Operation.
 *
 */
public class VmResultCreation {
  private int createVmAvailability;
  private int createVmResult;
  private long createVmResponseTime;
  private String vmId;

  /**
   * This method is the constructor of the class, assigning the received values to the private
   * variables.
   * 
   * @param availability It denotes whether the endpoint was available
   * @param result It represents the HTTP response code received
   * @param responseTime It indicates how much time it took to run the operation
   * @param id It is the VM id received from the operation
   */
  public VmResultCreation(int availability, int result, long responseTime, String id) {
    createVmAvailability = availability;
    createVmResult = result;
    createVmResponseTime = responseTime;
    vmId = id;
  }

  /**
   * It retrieves the availability result of the Create VM operation.
   * 
   * @return An integer indicating available (1) or unavailable (0).
   */
  public int getCreateVmAvailability() {
    return createVmAvailability;
  }

  /**
   * It retrieves the HTTP response received in the operation.
   * 
   * @return Integer representing the HTTP response code.
   */
  public int getCreateVmResult() {
    return createVmResult;
  }

  /**
   * It retrieves the response time of the Create VM operation.
   * 
   * @return Response time in milliseconds.
   */
  public long getCreateVmResponseTime() {
    return createVmResponseTime;
  }

  /**
   * It retrieves the identifier of the created VM.
   * 
   * @return String representing the identifier.
   */
  public String getVmId() {
    return vmId;
  }
}
