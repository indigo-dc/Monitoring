package org.indigo.openstackprobe.openstack;

/**
 * This class is in charge of carrying out the monitoring process for the OCCI
 * API of a concrete provider. It covers the whole lifecycle (create, inspect and
 * delete VM) and it sends the corresponding results to the Zabbix sender. * 
 * Since it extends the Thread class, it is expected to have several instances
 * running in parallel.
 * 
 * @author ATOS
 *
 */
public class MonitoringThread extends Thread {
  private String provider;
  private ZabbixSender mySender;
  private OpenStackOcciClient myClient;
  
  /**
   * This is the main constructor of the class, in order to retrieve the
   * required information for carrying out the monitoring activities.
   * @param providerId String with the identifier of the provider evaluated
   * @param providerURL String representing the OCCI API URL
   * @param keystoneURL String representing the Keystone API URL
   */
  protected MonitoringThread(String providerId, String providerUrl, String keystoneUrl) {
    provider = providerId;
    mySender = ZabbixSender.instance();  
    myClient = new OpenStackOcciClient(keystoneUrl, providerUrl, providerId);
    
    System.out.println("OCCI Endpoint: " + providerUrl);
    System.out.println("Keystone Endpoint: " + keystoneUrl);
  }
  
  /**
   * This is a constructor for testing purposes, which uses mocks.
   * @param senderMock Mock for the Zabbix sender
   * @param occiMock Mock for the OCCI client
   * @param providerId Provider identifier
   */
  public MonitoringThread(ZabbixSender senderMock, OpenStackOcciClient occiMock, 
      String providerId) {
    mySender = senderMock;
    myClient = occiMock;
    provider = providerId;
  }
  
  /**
   * This method executes the actions required for the monitoring process (perform
   * the expected operations on the OCCI API and send the metrics gathered to 
   * Zabbix).
   */
  public void run() {
    try {
      System.out.println("Retrieving monitoring information about " + provider + "...");      
      // Run the OCCI monitoring process and retrieve the result      
      OcciProbeResult result = myClient.getOcciMonitoringInfo();
      
      // Send the metrics to Zabbix collector 
      System.out.println("Sending the metrics to Zabbix Sender...");
      mySender.addMetricToQueue(result);
    } catch (Exception ex) {
      System.out.println("Failure when monitoring the provider" + provider + "!");
      System.out.println(ex.getMessage());
    }
    
    System.out.println("Monitoring thread for provider " + provider + " finished!");
  }
}
