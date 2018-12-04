package com.indigo.mesosprobe.marathon;

import com.indigo.mesosprobe.MesosProbeTags;
import com.indigo.zabbix.utils.CmdbServiceThread;
import com.indigo.zabbix.utils.IamClient;
import com.indigo.zabbix.utils.beans.DocDataType;
import com.indigo.zabbix.utils.beans.ServiceInfo;
import com.nimbusds.openid.connect.sdk.token.OIDCTokens;

public class MarathonThread extends CmdbServiceThread<MarathonCollector> {

  protected MarathonThread() {
    super("IaaS", "Marathon", "RuntimeTestTemplate");
  }

  public static void main(String[] args) {
    new MarathonThread().run(MesosProbeTags.CONFIG_FILE, args);
  }

  @Override
  protected MarathonCollector createServiceCollector(ServiceInfo service) {
    OIDCTokens accessToken = IamClient.getAccessToken();
    if (accessToken != null) {
      return new MarathonCollector(
          service.getDoc().getData().getEndpoint(), accessToken.getAccessToken().toString());
    }
    return null;
  }

  @Override
  protected DocDataType.ServiceType getServiceType() {
    return DocDataType.ServiceType.MARATHON;
  }
}
