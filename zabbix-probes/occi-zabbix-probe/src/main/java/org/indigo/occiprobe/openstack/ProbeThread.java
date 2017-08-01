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

import com.indigo.zabbix.utils.CloudProviderInfo;
import com.indigo.zabbix.utils.CmdbClient;
import com.indigo.zabbix.utils.IamClient;
import com.indigo.zabbix.utils.PropertiesManager;

import io.github.hengyunabc.zabbix.sender.SenderResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class aims at implementing the management of the threads to be used for 
 * the monitoring operations. It follows a singleton pattern, so only one instance
 * of the class will exist.
 * @author ATOS
 *
 */
public class ProbeThread {

  private static final Log logger = LogFactory.getLog(ProbeThread.class);

  // A handle to the unique Singleton instance.
  private static ProbeThread _instance = null;
  private long interval = 200;
  private long initialDelay = 10;
  private int numThreads = 2;
  private boolean termination = true;
  private CmdbClient myClient;
  
  private ProbeThread() {
    myClient = new CmdbClient();
  }
  
  private ProbeThread(CmdbClient cmdbMock) {
    myClient = cmdbMock;
  }
  
  /**
   * It provides the current ProbeThread instance and, in case it does not exist,
   * it creates a new instance and gives it as the result.
   * @return The current single instance of the ProbeThread 
   */
  public static synchronized ProbeThread instance() {
    if (null == _instance) {
      _instance = new ProbeThread();
      System.out.println("A new instance of the Monitoring Probe thread was created!");
    }
    return _instance;
  }
  
  /**
   * It provides the current ProbeThread instance and, in case it does not exist,
   * it creates a new instance and gives it as the result. This method is for
   * unit testing purposes.
   * @return The current single instance of the ProbeThread 
   */
  public static synchronized ProbeThread instance(CmdbClient cmdbMock) {
    _instance = new ProbeThread(cmdbMock);
    System.out.println("A new instance of the testing Monitoring Probe thread was created!");
    
    return _instance;
  }
  
  /**
   * It manages the process of retrieving Cloud providers' information, preparing 
   * the treads pool and launching monitoring operations per each provider.
   */
  public void startMonitoringProcess() {
  
    try {
      PropertiesManager.loadProperties("occiprobe.properties");
    
      //Get access token from IAM
      OAuthJSONAccessTokenResponse response = IamClient.getAccessToken();
    
      String accessToken = response.getAccessToken();
    
      String cmdb = PropertiesManager.getProperty(OcciProbeTags.CMDB_URL);
      if (cmdb != null) {
        
        // Retrieve the list of providers with their info
        System.out.println("Looking for providers and their info...");
        List<CloudProviderInfo> providersList = myClient.getFeasibleProvidersInfo();
    
        // Prepare the list of monitoring threads
        System.out.println("Done! Now starting monitoring tasks...");
        ArrayList<MonitoringThread> myTaskList = new ArrayList<MonitoringThread>();
        Iterator<CloudProviderInfo> providersIterator = providersList.iterator();
        while (providersIterator.hasNext()) {
          CloudProviderInfo currentProvider = providersIterator.next();
      
          MonitoringThread myTask = new MonitoringThread(accessToken, currentProvider);
          try {
            SenderResult result = myTask.run();
            if (result.success()) {
              System.out.println("Successfully sent metrics for host "
                                     + currentProvider.getProviderId());
            } else {
              System.out.println("Error sending metrics for host "
                                     + currentProvider.getProviderId());
            }
          } catch (Exception e) {
            System.out.println("Error while getting OCCI metrics from provider");
            e.printStackTrace();
          }
      
      
          //myTaskList.add(myTask);
        }
    
        System.out.println("OCCI Monitoring Probe finished!");
      } else {
        
        String provider = PropertiesManager.getProperty(OcciProbeTags.PROVIDER);
        String occi = PropertiesManager.getProperty(OcciProbeTags.OCCI_ENDPOINT);
        String keystone = PropertiesManager.getProperty(OcciProbeTags.KEYSTONE_ENDPOINT);
        String identityProvider = PropertiesManager.getProperty(OcciProbeTags.IDENTITY_PROVIDER);
        String protocol = PropertiesManager.getProperty(OcciProbeTags.PROTOCOL);
        String imageId = PropertiesManager.getProperty(OcciProbeTags.IMAGE_ID);
        String osFlavour = PropertiesManager.getProperty(OcciProbeTags.OS_FLAVOUR);
        String networkId = PropertiesManager.getProperty(OcciProbeTags.NETWORK_ID);
    
        if (provider != null && occi != null && keystone != null && identityProvider != null
                && protocol != null && imageId != null && osFlavour != null) {
          new MonitoringThread(accessToken, new CloudProviderInfo(provider, occi, keystone, 0, true,
                                                                     false, true, identityProvider,
                                                                     protocol, imageId, osFlavour,
                                                                     networkId)).run();
        } else {
          System.out.println("OCCI probe configuration incomplete. "
                                 + "Please provide a CMDB URL or provide all mandatory elements");
        }
    
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Typical main method for testing.
   * @param args Typical args
   */
  public static void main(String[] args) {
    new ProbeThread().startMonitoringProcess();
  }
}
