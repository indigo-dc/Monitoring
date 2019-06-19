package org.indigo.openstackprobe.openstack;

import com.indigo.zabbix.utils.CmdbServiceThread;
import com.indigo.zabbix.utils.IamClient;
import com.indigo.zabbix.utils.beans.DocDataType;
import com.indigo.zabbix.utils.beans.ServiceInfo;
import com.nimbusds.openid.connect.sdk.token.OIDCTokens;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/** @author Reply Santer. */
public class OpenstackThread extends CmdbServiceThread<OpenstackCollector> {

  private static final Logger log = LogManager.getLogger(OpenStackClient.class);

  static int countColls = 0;
  private static List<OpenstackCollector> collectors;

  /** Constructor for test purposes. */
  protected OpenstackThread(List<OpenstackCollector> collectorsMocked) {
    super("IaaS", "Cloud_Providers", "TemplateOpenstack");
    collectors = collectorsMocked;
  }

  /** Constructor for initializing the the the implementation of the thread. */
  protected OpenstackThread() {
    super("IaaS", "Cloud_Providers", "TemplateOpenstack");
  }

  /**
   * The very entry point of the code.
   *
   * @param args args
   */
  public static void main(String[] args) {
    new OpenstackThread().run(OpenStackProbeTags.CONFIG_FILE, args);
  }

  @Override
  protected OpenstackCollector createServiceCollector(ServiceInfo service) {
    OIDCTokens accessToken = IamClient.getAccessToken();
    if (accessToken != null) {
      String serviceId = service.getId();
      String providerId = service.getDoc().getData().getProviderId();
      String keystoneUrl = service.getDoc().getData().getEndpoint();
      return new OpenstackCollector(accessToken.getAccessToken().toString(), serviceId, providerId, keystoneUrl);
    }
    return null;
  }

  @Override
  protected DocDataType.ServiceType getServiceType() {
    return DocDataType.ServiceType.OPENSTACK;
  }
}
