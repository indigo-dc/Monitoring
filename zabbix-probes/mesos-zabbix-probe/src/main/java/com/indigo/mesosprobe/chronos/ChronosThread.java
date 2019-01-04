package com.indigo.mesosprobe.chronos;

import com.indigo.mesosprobe.MesosProbeTags;
import com.indigo.zabbix.utils.CmdbServiceThread;
import com.indigo.zabbix.utils.IamClient;
import com.indigo.zabbix.utils.beans.DocDataType;
import com.indigo.zabbix.utils.beans.ServiceInfo;
import com.nimbusds.openid.connect.sdk.token.OIDCTokens;

/** Created by jose on 11/21/16. */
public class ChronosThread extends CmdbServiceThread<ChronosCollector> {

  public ChronosThread() {
    super("IaaS", "Cloud_Providers", "RuntimeTestTemplate");
  }

  public static void main(String[] args) {
    new ChronosThread().run(MesosProbeTags.CONFIG_FILE, args);
  }

  @Override
  protected ChronosCollector createServiceCollector(ServiceInfo service) {
    OIDCTokens accessToken = IamClient.getAccessToken();
    if (accessToken != null) {
      return new ChronosCollector(
          service.getDoc().getData().getEndpoint(), accessToken.getAccessToken().toString());
    }
    return null;
  }

  @Override
  protected DocDataType.ServiceType getServiceType() {
    return DocDataType.ServiceType.CHRONOS;
  }
}
