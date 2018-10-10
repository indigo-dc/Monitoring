package org.indigo.openstackprobe.openstack;

import com.indigo.zabbix.utils.CloudProviderInfo;
import com.indigo.zabbix.utils.IamClient;
import com.nimbusds.openid.connect.sdk.token.OIDCTokens;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openstack4j.api.OSClient.OSClientV3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Reply Santer. Collects and initializes the cloud providers.
 */
public class ProvidersSearch {

  private static CmdbClientForOpenStack cmdbClient = new CmdbClientForOpenStack();
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
   * @param providers provideres
   * @param collectorMocked collectors
   * @param cmdbMocked cmdbMocked
   * @param providerMocked providerMocked
   * @param osclientMocked osclientMocked
   */
  protected ProvidersSearch(List<CloudProviderInfo> providers, OpenstackCollector collectorMocked,
      CmdbClientForOpenStack cmdbMocked, CloudProviderInfo providerMocked,
      OSClientV3 osclientMocked) {
    providersList = providers;
    collector = collectorMocked;
    cmdbClient = cmdbMocked;
    CloudProviderInfo provider = providerMocked;
    osclient = osclientMocked;
  }

  /**
   * For each of the providers returned from prefiltering in @see getFeasibleProvidersInfo,
   * initializes and adds the providers in the array.
   * 
   * @return List of OpenstackCollectors
   */
  public static List<OpenstackCollector> getCollectorResults() {
    // Retrieve the list of providers with their info
    log.info("Looking for providers and their info...");
    providersList = cmdbClient.getFeasibleProvidersInfo();

    // Prepare the list of monitoring threads
    log.info("Done with looking for candidate providers! They are: " + providersList.size()
        + ". Now starting to collect the providers with successfull "
        + "authentication to Openstack...");
    List<OpenstackCollector> tasks = new ArrayList<>();
    Iterator<CloudProviderInfo> providersIterator = providersList.iterator();
    List<CloudProviderInfo> providers = new ArrayList<>();

    OIDCTokens response = IamClient.getAccessToken();
    String accessToken = response.getAccessToken().toString();

    while (providersIterator.hasNext()) {
      CloudProviderInfo provider = providersIterator.next();
      if (provider.getKeystoneEndpoint() != null) {
        providerId = provider.getProviderId();
        keystoneUrl = provider.getKeystoneEndpoint();
        providers.add(provider);
        try {
          if (!providers.isEmpty()) {
            log.info("Task scheduled for the provider: " + providerId
                + " whose identity endpoint is " + keystoneUrl);
            collector = new OpenstackCollector(accessToken, providerId, keystoneUrl);
            // If authentication to Openstack was successful add it to tasks array
            tasks.add(collector);
          }
        } catch (Exception iae) {
          log.debug(ERROR_MESSAGE + providerId, iae);
        }
      }
    }
    return tasks;
  }
}
