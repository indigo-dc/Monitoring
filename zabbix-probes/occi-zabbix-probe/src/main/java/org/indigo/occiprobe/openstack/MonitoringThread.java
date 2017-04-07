package org.indigo.occiprobe.openstack;

import io.github.hengyunabc.zabbix.sender.SenderResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
public class MonitoringThread extends com.indigo.zabbix.utils.ProbeThread<OpenStackOcciClient> {

  public static final Log logger = LogFactory.getLog(MonitoringThread.class);

  private String accessToken;
  private String provider;
  private String providerUrl;
  private String keystoneUrl;
  
  /**
   * This is the main constructor of the class, in order to retrieve the
   * required information for carrying out the monitoring activities.
   * @param acessToken String with the access token provided by the IAM
   * @param providerId String with the identifier of the provider evaluated
   * @param providerUrl String representing the OCCI API URL
   * @param keystoneUrl String representing the Keystone API URL
   */
  protected MonitoringThread(String acessToken, String providerId, String providerUrl,
                             String keystoneUrl) {
    super("IaaS", "Cloud_Providers", "OCCI");

    this.accessToken = acessToken;
    provider = providerId;
    this.providerUrl = providerUrl;
    this.keystoneUrl = keystoneUrl;
    
    System.out.println("OCCI Endpoint: " + providerUrl);
    System.out.println("Keystone Endpoint: " + keystoneUrl);
  }

  @Override
  protected OpenStackOcciClient createCollector() {
    return new OpenStackOcciClient(accessToken, keystoneUrl, providerUrl, provider);
  }

  /**
   * This is a constructor for testing purposes, which uses mocks.
   * @param senderMock Mock for the Zabbix sender
   * @param occiMock Mock for the OCCI client
   * @param providerId Provider identifier
   */
  /*public MonitoringThread(ZabbixSender senderMock, OpenStackOcciClient occiMock,
      String providerId) {
    mySender = senderMock;
    myClient = occiMock;
    provider = providerId;
  }*/
  
  /**
   * This method executes the actions required for the monitoring process (perform
   * the expected operations on the OCCI API and send the metrics gathered to 
   * Zabbix).
   */
  public SenderResult run() {

      return run(null);

      /*System.out.println("Retrieving monitoring information about " + provider + "...");
      // Run the OCCI monitoring process and retrieve the result      
      OcciProbeResult result = myClient.getOcciMonitoringInfo();
      
      // Send the metrics to Zabbix collector 
      System.out.println("Sending the metrics to Zabbix Sender...");
      mySender.addMetricToQueue(result);*/
  }
}
