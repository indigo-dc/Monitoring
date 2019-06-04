package com.indigo.mesosprobe;

import com.indigo.zabbix.utils.CmdbServiceThread;
import com.indigo.zabbix.utils.beans.DocDataType;
import com.indigo.zabbix.utils.beans.ServiceInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Created by jose on 22/09/16. */
public class MesosThread extends CmdbServiceThread<MesosCollector> {

  private static final Log logger = LogFactory.getLog(MesosThread.class);

  protected MesosThread() {
    super("IaaS", "Cloud_Providers", "TemplateMesos");
  }

  /**
   * Start monitoring process.
   *
   * @param args Arguments will be ignored.
   */
  public static void main(String[] args) {
    new MesosThread().run(MesosProbeTags.CONFIG_FILE, args);
  }

  @Override
  protected MesosCollector createServiceCollector(ServiceInfo service) {
    return new MesosCollector(service);
  }

  @Override
  protected DocDataType.ServiceType getServiceType() {
    return DocDataType.ServiceType.MESOS;
  }
}
