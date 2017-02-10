package org.indigo.openstackprobe.openstack;

import org.apache.logging.log4j.LogManager;

/**
 * This class is in charge of carrying out the monitoring process for the Openstack
 * API of a concrete provider. It covers the whole lifecycle (create, inspect and
 * delete VM) and it sends the corresponding results to the Zabbix sender. * 
 * Since it extends the Thread class, it is expected to have several instances
 * running in parallel.
 * 
 * @author Reply
 *
 */
public class MonitoringThread extends Thread {
  private String provider;
  private ZabbixSender mySender;
  private OpenStackClient myClient;
  private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(MonitoringThread.class);
  
  /**
   * This is the main constructor of the class, in order to retrieve the
   * required information for carrying out the monitoring activities.
   * @param providerId String with the identifier of the provider evaluated
   * @param providerURL String representing the Openstack API URL
   * @param keystoneURL String representing the Keystone API URL
   */
  protected MonitoringThread(String providerId, String providerUrl, String keystoneUrl) {
    provider = providerId;
    mySender = ZabbixSender.instance();  
    myClient = new OpenStackClient(keystoneUrl, providerUrl, providerId);
    
    log.info("Nova Endpoint: " + providerUrl);
    log.info("Keystone Endpoint: " + keystoneUrl);
  }
  
  /**
   * This is a constructor for testing purposes, which uses mocks.
   * @param senderMock Mock for the Zabbix sender
   * @param OpenstackMock Mock for the Openstack client
   * @param providerId Provider identifier
   */
  public MonitoringThread(ZabbixSender senderMock, OpenStackClient openstackMock, 
      String providerId) {
    mySender = senderMock;
    myClient = openstackMock;
    provider = providerId;
  }
  
  /**
   * This method executes the actions required for the monitoring process (perform
   * the expected operations on the Openstack API and send the metrics gathered to 
   * Zabbix).
   */
  @Override
  public void run() {
    try {
      log.info("Retrieving monitoring information about " + provider + "...");      
      // Run the Openstack monitoring process and retrieve the result
      OpenstackProbeResult result = myClient.getOpenstackMonitoringInfo();
      
      // Send the metrics to Zabbix collector 
      log.info("Sending the metrics to Zabbix Sender...");
      mySender.addMetricToQueue(result);
    } catch (Exception ex) {
      log.info("Failure when monitoring the provider" + provider + "!");
      log.info(ex.getMessage());
    }
    
    log.info("Monitoring thread for provider " + provider + " finished!");
  }
}