///**
//
//Licensed under the Apache License, Version 2.0 (the License);
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//
//**/
//
//package org.indigo.openstackprobe.openstack;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.concurrent.Executors;
//import java.util.concurrent.RejectedExecutionException;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.logging.log4j.LogManager;
//
//import io.github.hengyunabc.zabbix.sender.ZabbixSender;
//
//
///**
// * This class aims at implementing the management of the threads to be used for 
// * the monitoring operations. It follows a singleton pattern, so only one instance
// * of the class will exist.
// * @author ATOS
// *
// */
//public class ProbeThread {
//  // A handle to the unique Singleton instance.
//  private final ScheduledExecutorService scheduler;
//  private static ProbeThread _instance;
//  private long initialDelay = 10;
//  private int numThreads = 2;
//  private CmdbClient myClient;
//  private ZabbixSender mySender;
//  
//  private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ProbeThread.class);
//  
//  private ProbeThread() {
//    // Build element for thread and tasks scheduling
//    scheduler = Executors.newScheduledThreadPool(numThreads);
//    myClient = new CmdbClient();
//    mySender = ZabbixSender.instance();
//  }
//  
//  private ProbeThread(ScheduledExecutorService mock, ZabbixSender senderMock, CmdbClient cmdbMock) {
//    // Build element for thread and tasks scheduling
//    scheduler = mock;
//    mySender = senderMock;
//    myClient = cmdbMock;
//  }
//  
//  /**
//   * It provides the current ProbeThread instance and, in case it does not exist,
//   * it creates a new instance and gives it as the result.
//   * @return The current single instance of the ProbeThread 
//   */
//  public static synchronized ProbeThread instance() {
//    if (null == _instance) {
//      _instance = new ProbeThread();
//      log.info("A new instance of the Monitoring Probe thread was created!");
//    }
//    return _instance;
//  }
//  
//  /**
//   * It provides the current ProbeThread instance and, in case it does not exist,
//   * it creates a new instance and gives it as the result. This method is for
//   * unit testing purposes.
//   * @return The current single instance of the ProbeThread 
//   */
//  public static synchronized ProbeThread instance(ScheduledExecutorService mock,
//      ZabbixSender senderMock, CmdbClient cmdbMock) {
//    _instance = new ProbeThread(mock, senderMock, cmdbMock);
//    log.info("A new instance of the testing Monitoring Probe thread was created!");
//    
//    return _instance;
//  }
//  
//  /**
//   * It manages the process of retrieving Cloud providers' information, preparing 
//   * the treads pool and launching monitoring operations per each provider.
//   */
//  public boolean startMonitoringProcess() {
//    // Retrieve the list of providers with their info   
//    log.info("Looking for providers and their info...");
//    List<CloudProviderInfo> providersList = myClient.getFeasibleProvidersInfo();
//    
//    // Prepare the list of monitoring threads
//    log.info("Done! Now starting monitoring tasks...");
//    List<OpenstackThread> myTaskList = new ArrayList<>();
//    Iterator<CloudProviderInfo> providersIterator = providersList.iterator();
//    while (providersIterator.hasNext()) {
//      CloudProviderInfo currentProvider = providersIterator.next();
//      if(currentProvider.getNovaEndpoint()!=null && currentProvider.getKeystoneEndpoint()!=null){
//      String providerId = currentProvider.getProviderId();
//      String openstackEndpoint = currentProvider.getNovaEndpoint();
//      String keystoneEndpoint = currentProvider.getKeystoneEndpoint();
//      
//      OpenstackThread myTask = new OpenstackThread(providerId, openstackEndpoint, keystoneEndpoint);
////      OpenstackThread myTask = new OpenstackThread();
//      myTaskList.add(myTask);
//      }
//    }
//    /*
//    MonitoringThread myTask1 = new MonitoringThread("provider-RECAS-BARI", "http://cloud.recas.ba.infn.it:8787", "https://cloud.recas.ba.infn.it:5000");
//    MonitoringThread myTask2 = new MonitoringThread("provider-UPV-GRyCAP", "http://cloud.recas.ba.infn.it:8787", "https://cloud.recas.ba.infn.it:5000");
//    MonitoringThread myTask3 = new MonitoringThread("provider-RECAS-BARI", "http://cloud.recas.ba.infn.it:8787", "https://cloud.recas.ba.infn.it:5000");
//    MonitoringThread myTask4 = new MonitoringThread("provider-UPV-GRyCAP", "http://cloud.recas.ba.infn.it:8787", "https://cloud.recas.ba.infn.it:5000");
//    myTaskList.add(myTask1);
//    myTaskList.add(myTask2);
//    myTaskList.add(myTask3);
//    myTaskList.add(myTask4);
//    */
//    
//    // Create threads and run the monitoring actions
//    Iterator<OpenstackThread> myTaskIterator = myTaskList.iterator();
//    int schedulingFailures = 0;
//    while (myTaskIterator.hasNext()) {
//      MonitoringThread currentTask = myTaskIterator.next();
//      try {
//        scheduler.schedule(currentTask, initialDelay, TimeUnit.SECONDS);
//      } catch (RejectedExecutionException ex) {
//        log.info("Issue detected when scheduling a new thread!" + ex.getMessage());
//        schedulingFailures ++;
//      }      
//    }
//            
//    // Terminate thread manager
//    boolean result;
//    if (schedulingFailures > 0) {
//      result = false;
//    } else {
//      // Check scheduled tasks finalization
//      
//    }
//    scheduler.shutdown();
//    try {
//      boolean termination = scheduler.awaitTermination(30, TimeUnit.MINUTES);
//    } catch (InterruptedException ex) {
//      log.info("The scheduler was interrupted because of unexpected reasons!");
//      result = false;
//    }
//    
//    // Send all the metrics    
//    result = mySender.sendMetrics();
//    if (!result) {
//      log.info("Some metrics could not be sent correctly!");
//    }
//    
//    log.info("Openstack Monitoring Probe finished!");
//    return result;
//  }
//  
//  /**
//   * Stops and cleans all the required elements once the monitoring operations
//   * are finished.
//   */
//  public void stopThread() {
//    log.info("Destroying the monitoring thread...");
//    scheduler.shutdownNow();
//  }
//  
//  /**
//   * Typical main method for testing.
//   * @param args Typical args
//   */
//  public static void main(String[] args) {
//    // Retrieve input arguments
//     
//    // Start the monitoring process
//    ProbeThread probeManager = ProbeThread.instance();
//    boolean result = probeManager.startMonitoringProcess();
//    log.info("Result: " + result);
//  }
//}