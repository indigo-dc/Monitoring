package org.indigo.openstackprobe.openstack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.openstack4j.api.OSClient.OSClientV3;

import com.indigo.zabbix.utils.IamClient;

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
	private OSClientV3 osclient;
	private static final String ERROR_MESSAGE = "Unable to retrieve information about the provider ";
	// private static OpenStackClient openstack;

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
			CmdbClient cmdbMocked, CloudProviderInfo providerMocked, OSClientV3 osclientMocked) {
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
		log.info("Done with looking for candidate providers! They are: " + providersList.size()
				+ ". Now starting to collect the providers with successfull authentication to Openstack...");
		List<OpenstackCollector> tasks = new ArrayList<>();
		Iterator<CloudProviderInfo> providersIterator = providersList.iterator();
		List<CloudProviderInfo> providers = new ArrayList<>();

		OAuthJSONAccessTokenResponse response = IamClient.getAccessToken();
		String accessToken = response.getAccessToken();

		while (providersIterator.hasNext()) {
			CloudProviderInfo provider = providersIterator.next();
			if (provider.getKeystoneEndpoint() != null) {
				providerId = provider.getProviderId();
				keystoneUrl = validateIdentityEndpoint(provider.getKeystoneEndpoint());
				providers.add(provider);
				try {
					if (!providers.isEmpty()) {
						log.info("Task scheduled for the provider: " + providerId + " whose identity endpoint is "
								+ keystoneUrl);
						collector = new OpenstackCollector(accessToken, providerId, keystoneUrl);
						//If authentication to Openstack was successful add it to tasks array
						tasks.add(collector);
					}
				} catch (Exception iae) {
					log.debug(ERROR_MESSAGE + providerId + " " + iae.getMessage());
				}
			}
		}
		return tasks;
	}

	private static String validateIdentityEndpoint(String endpoint) {
		endpoint = endpoint.contains("v2.0") ? endpoint.replace("v2.0", "v3") : endpoint;
		endpoint = endpoint.contains("https") ? endpoint : endpoint.replace("http", "https");
		return endpoint;
	}

}
