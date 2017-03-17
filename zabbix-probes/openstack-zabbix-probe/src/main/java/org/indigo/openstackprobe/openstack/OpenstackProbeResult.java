package org.indigo.openstackprobe.openstack;

import java.util.List;

import org.openstack4j.model.compute.Server;

/**
 * This class represents the whole result of a monitoring action which has 
 * followed the full VM lifecycle by using the OCCI API exposed by a 
 * Cloud provider. It keeps information about each operation (Create, Inspect 
 * and Delete VM) and about a global evaluation.
 *
 */
public class OpenstackProbeResult {
  private String providerName;
  private int globalAvailability;
  private int globalResult;
  private long globalResponseTime;
  
  private CreateVmResult createVmElement;
  private InspectVmResult inspectVmElement;
  private DeleteVmResult deleteVmElement;
  private List<? extends Server> instancesList;
  
  /**
   * This is an empty constructor for the class.
   */
  public OpenstackProbeResult(String provider) {
    providerName = provider;
  }
  
  /**
   * This is a constructor that takes as input the global values for the VMs
   * lifecycle operation (create, inspect and delete VM).
   * @param availability 1 if all the operations were available, 0 if not
   * @param result It is a global HTTP response code, the most problematic one.
   * @param responseTime Sum of the response time of all calls, in milliseconds.
   */
  public OpenstackProbeResult(int availability, int result, long responseTime, String provider) {
    globalAvailability = availability;
    globalResult = result;
    globalResponseTime = responseTime;
    providerName = provider;
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
   * It retrieves the name of the provider the metrics belong to.
   * @return String with the provider identifier
   */
  public String getProviderName() {
    return providerName;
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
  
  public void setOsInstanceList(List<? extends Server> instances){
	  this.instancesList= instances;
  }
  
  public List<? extends Server> getOsInstanceList(){
	  return instancesList;
  }
}