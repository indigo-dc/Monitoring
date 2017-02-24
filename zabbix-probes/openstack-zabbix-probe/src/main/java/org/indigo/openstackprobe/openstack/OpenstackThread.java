package org.indigo.openstackprobe.openstack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
	private static OpenstackThread _instance = null;
	private long interval = 200;
	private int numThreads = 2;
	private long initialDelay = 10;

	private OpenstackCollector openstackTaskCollector;

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
		Set<OpenstackCollector> tasks = new HashSet<>();
		Iterator<CloudProviderInfo> providersIterator = providersList.iterator();
		List<CloudProviderInfo> providers = new ArrayList<>();
		while (providersIterator.hasNext()) {
			CloudProviderInfo currentProvider = providersIterator.next();
			if (currentProvider.getNovaEndpoint() != null && currentProvider.getKeystoneEndpoint() != null) {
				providerId = currentProvider.getProviderId();
				providerUrl = currentProvider.getNovaEndpoint();
				keystoneUrl = currentProvider.getKeystoneEndpoint();
				providers.add(currentProvider);
			}

			// for (CloudProviderInfo provider : providers) {

			// openstackTaskCollector = new OpenstackCollector(providerId,
			// providerUrl, keystoneUrl);

			// myTaskList.add(openstackTaskCollector);
			log.info("Task scheduled for the provider: " + providerId + " whose compute endpoint is " + providerUrl
					+ " and identity endpoint is " + keystoneUrl);
			tasks.add(new OpenstackCollector(providerId, providerUrl, keystoneUrl));
			// }
		}

		// Create threads and run the monitoring actions
		// Iterator<OpenstackCollector> myTaskIterator = myTaskList.iterator();
		Iterator<OpenstackCollector> taskIterator = tasks.iterator();
		int schedulingFailures = 0;
		while (taskIterator.hasNext()) {
			// for(OpenstackCollector task: tasks){
			OpenstackCollector currentTask = taskIterator.next();
//			currentTask.run();
			
			return currentTask;
//			try {
//				scheduler.schedule(currentTask/* currentTask */, initialDelay, TimeUnit.SECONDS);
//			} catch (RejectedExecutionException ex) {
//				log.info("Issue detected when scheduling a new thread!" + ex.getMessage());
//				schedulingFailures++;
//			}
		}
		return null;

		// Terminate thread manager
//		boolean result = true;
//		if (schedulingFailures > 0) {
//			result = false;
//		} else {
//			// Check scheduled tasks finalization
//		}
//		scheduler.shutdown();
//		try {
//			boolean termination = scheduler.awaitTermination(30, TimeUnit.MINUTES);
//		} catch (InterruptedException ex) {
//			log.info("The scheduler was interrupted because of unexpected reasons!");
//			result = false;
//		}
		

		// Send all the metrics
		// result = mySender.sendMetrics();
		// if (!result){
		// log.info("Some metrics could not be sent correctly!");
		// }
		//
		// log.info("Openstack Monitoring Probe finished!");
		// return result;
	}
}
