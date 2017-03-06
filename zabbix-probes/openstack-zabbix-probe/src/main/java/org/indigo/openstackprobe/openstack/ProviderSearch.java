package org.indigo.openstackprobe.openstack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public  class ProviderSearch {

	private static CmdbClient cmdbClient = new CmdbClient();
	private static final Logger log = LogManager.getLogger(OpenStackClient.class);
	public static String providerId;
	public static String providerUrl;
	public static String keystoneUrl;
//	private static OpenstackThread _instance = null;
//	private long interval = 200;
//	private int numThreads = 4;
//	private long initialDelay = 20;
//	private boolean termination = true;	
	private static OpenstackCollector openstackTaskCollector;
	
	public static List<OpenstackCollector> getCollectorResults(){
		// Retrieve the list of providers with their info
				log.info("Looking for providers and their info...");
				List<CloudProviderInfo> providersList = cmdbClient.getFeasibleProvidersInfo();

				// Prepare the list of monitoring threads
				log.info("Done! Now starting monitoring tasks...");
				List<OpenstackCollector> tasks = new ArrayList<>();
				Iterator<CloudProviderInfo> providersIterator = providersList.iterator();
				List<CloudProviderInfo> providers = new ArrayList<>();

				while (providersIterator.hasNext()) {
					CloudProviderInfo currentProvider = providersIterator.next();
					if (currentProvider.getKeystoneEndpoint() != null) {
						providerId = currentProvider.getProviderId();
						keystoneUrl = currentProvider.getKeystoneEndpoint();
						providers.add(currentProvider);
					}

					log.info("Task scheduled for the provider: " + providerId + " whose identity endpoint is " + keystoneUrl);
					openstackTaskCollector = new OpenstackCollector(providerId, keystoneUrl);
					tasks.add(openstackTaskCollector);
				}
				return tasks;
	}

}
