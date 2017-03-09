package org.indigo.openstackprobe.openstack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProviderSearch {

	private static CmdbClient cmdbClient = new CmdbClient();
	private static final Logger log = LogManager.getLogger(OpenStackClient.class);
	private static List<CloudProviderInfo> providersList = new ArrayList<>();
	private static String providerId;
	private static String keystoneUrl;
	private OpenstackCollector collector;
	private static CloudProviderInfo provider;

	protected ProviderSearch(List<CloudProviderInfo> providers, OpenstackCollector collectorMocked,
			CmdbClient cmdbMocked, CloudProviderInfo providerMocked) {
		providersList = providers;
		collector = collectorMocked;
		cmdbClient = cmdbMocked;
		provider = providerMocked;
	}

	public static List<OpenstackCollector> getCollectorResults() {
		// Retrieve the list of providers with their info
		log.info("Looking for providers and their info...");
		providersList = cmdbClient.getFeasibleProvidersInfo();

		// Prepare the list of monitoring threads
		log.info("Done! Now starting monitoring tasks...");
		List<OpenstackCollector> tasks = new ArrayList<>();
		Iterator<CloudProviderInfo> providersIterator = providersList.iterator();
		List<CloudProviderInfo> providers = new ArrayList<>();

		while (providersIterator.hasNext()) {
			provider = providersIterator.next();
			if (provider.getKeystoneEndpoint() != null) {
				providerId = provider.getProviderId();
				keystoneUrl = provider.getKeystoneEndpoint();
				providers.add(provider);
			}
			log.info("Task scheduled for the provider: " + providerId + " whose identity endpoint is " + keystoneUrl);
			tasks.add(new OpenstackCollector(providerId, keystoneUrl));
		}
		return tasks;
	}

}
