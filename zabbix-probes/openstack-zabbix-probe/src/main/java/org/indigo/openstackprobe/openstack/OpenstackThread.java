package org.indigo.openstackprobe.openstack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.indigo.zabbix.utils.ProbeThread;

public class OpenstackThread extends ProbeThread<OpenstackCollector> {

  private CmdbClient cmdbClient = new CmdbClient();
  private static final Logger log = LogManager.getLogger(OpenStackClient.class);
  String providerId;
  String providerUrl;
  String keystoneUrl;
  private final ScheduledExecutorService scheduler;
  private int numThreads = 2;
  private long initialDelay = 10;
  

  /**
   * Constructor.
   */
  protected OpenstackThread() {
    super("IaaS", "Cloud_Providers", "TemplateOpenstack");
    scheduler = Executors.newScheduledThreadPool(numThreads); 
  }

  public static void main(String[] args) {
    new OpenstackThread().run(OpenstackProbeTags.CONFIG_FILE);
  }

  @Override
  protected OpenstackCollector createCollector() {

    // Retrieve the list of providers with their info
    log.info("Looking for providers and their info...");
    List<CloudProviderInfo> providersList = cmdbClient.getFeasibleProvidersInfo();

    // Prepare the list of monitoring threads
    log.info("Done! Now starting monitoring tasks...");
    List<OpenstackCollector> myTaskList = new ArrayList<>();
    Iterator<CloudProviderInfo> providersIterator = providersList.iterator();
    while (providersIterator.hasNext()) {
      CloudProviderInfo currentProvider = providersIterator.next();
      if (currentProvider.getNovaEndpoint() != null
          && currentProvider.getKeystoneEndpoint() != null) {
        providerId = currentProvider.getProviderId();
        providerUrl = currentProvider.getNovaEndpoint();
        keystoneUrl = currentProvider.getKeystoneEndpoint();
      }
    }
    
    OpenstackCollector openstackTaskCollector = new OpenstackCollector(providerId, providerUrl, keystoneUrl);

    myTaskList.add(openstackTaskCollector);

    // Create threads and run the monitoring actions
    Iterator<OpenstackCollector> myTaskIterator = myTaskList.iterator();
    int schedulingFailures = 0;
    while (myTaskIterator.hasNext())

    {
      OpenstackCollector currentTask = myTaskIterator.next();
      try {
        scheduler.schedule((Runnable) currentTask, initialDelay, TimeUnit.SECONDS);
      } catch (RejectedExecutionException ex) {
        log.info("Issue detected when scheduling a new thread!" + ex.getMessage());
        schedulingFailures++;
      }
    }

    // Terminate thread manager
    boolean result;
    if (schedulingFailures > 0){
      result = false;
    } else{
      // Check scheduled tasks finalization
    }
    scheduler.shutdown();
    try {
      boolean termination = scheduler.awaitTermination(30, TimeUnit.MINUTES);
    } catch (InterruptedException ex){
      log.info("The scheduler was interrupted because of unexpected reasons!");
      result = false;
    }
    return openstackTaskCollector;

    // Send all the metrics
//    result = mySender.sendMetrics();
//    if (!result){
//      log.info("Some metrics could not be sent correctly!");
//    }
//
//    log.info("Openstack Monitoring Probe finished!");
//    return result;
  }
}
