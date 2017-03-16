package org.indigo.openstackprobe.openstack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.exceptions.ClientResponseException;
import org.openstack4j.api.exceptions.ResponseException;

/**
 * 
 * @author Reply Santer. Collects and initializes the cloud providers.
 */
public class ProviderSearch {

	private static CmdbClient cmdbClient = new CmdbClient();
	private static final Logger log = LogManager.getLogger(OpenStackClient.class);
	private static List<CloudProviderInfo> providersList = new ArrayList<>();
	private static String providerId;
	private static String keystoneUrl;
	private static OpenstackCollector collector;
	private OSClient osclient;
	private static final String ERROR_MESSAGE= "Unable to retrieve information about the provider ";

	/**
	 * Constructor used for testing purposes.
	 * 
	 * @param providers
	 * @param collectorMocked
	 * @param cmdbMocked
	 * @param providerMocked
	 * @param osclientMocked
	 */
	protected ProviderSearch(List<CloudProviderInfo> providers, OpenstackCollector collectorMocked,
			CmdbClient cmdbMocked, CloudProviderInfo providerMocked, OSClient osclientMocked) {
		providersList = providers;
		collector = collectorMocked;
		cmdbClient = cmdbMocked;
		CloudProviderInfo provider = providerMocked;
		osclient = osclientMocked;
	}

	/**
	 * For each of the providers returned from prefiltering in @see
	 * getFeasibleProvidersInfo, initializes and adds the providers in the
	 * array.
	 * 
	 * @return List of OpenstackCollectors
	 */
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
			CloudProviderInfo provider = providersIterator.next();
			if (provider.getKeystoneEndpoint() != null) {
				providerId = provider.getProviderId();
				keystoneUrl = provider.getKeystoneEndpoint();
				providers.add(provider);
			}
			log.info("Task scheduled for the provider: " + providerId + " whose identity endpoint is " + keystoneUrl);
			try {
				collector = new OpenstackCollector(providerId, keystoneUrl);
				tasks.add(collector);
			} catch (ResponseException | IllegalArgumentException iae) {
				log.debug(ERROR_MESSAGE + providerId + " " + iae.getMessage());
			}
		}
		return tasks;
	}

}
