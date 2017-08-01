package org.indigo.occiprobe.openstack;

import com.indigo.zabbix.utils.CloudProviderInfo;

import io.github.hengyunabc.zabbix.sender.SenderResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is in charge of carrying out the monitoring process for the OCCI
 * API of a concrete provider. It covers the whole lifecycle (create, inspect and
 * delete VM) and it sends the corresponding results to the Zabbix sender.
 * 
 * @author ATOS
 *
 */
public class MonitoringThread extends com.indigo.zabbix.utils.ProbeThread<OpenStackOcciClient> {

  public static final Log logger = LogFactory.getLog(MonitoringThread.class);
  
  private String accessToken;
  private CloudProviderInfo configuration;
  
  /**
   * This is the main constructor of the class, in order to retrieve the
   * required information for carrying out the monitoring activities.
   * @param acessToken String with the access token provided by the IAM
   * @param configuration Monitoring configuration
   */
  protected MonitoringThread(String acessToken, CloudProviderInfo configuration) {
    super("IaaS", "Cloud_Providers", "OCCI");

    this.accessToken = acessToken;
    this.configuration = configuration;
    
    System.out.println("OCCI Endpoint: " + configuration.getOcciEndpoint());
    System.out.println("Keystone Endpoint: " + configuration.getKeystoneEndpoint());
  }

  @Override
  protected OpenStackOcciClient createCollector() {
    return new OpenStackOcciClient(accessToken, configuration);
  }

  
  /**
   * This method executes the actions required for the monitoring process (perform
   * the expected operations on the OCCI API and send the metrics gathered to 
   * Zabbix).
   */
  public SenderResult run() {
    return run(null);
  }
}
