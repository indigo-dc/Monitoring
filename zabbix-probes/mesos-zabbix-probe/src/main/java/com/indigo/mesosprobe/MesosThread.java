package com.indigo.mesosprobe;

import com.indigo.zabbix.utils.CmdbClient;
import com.indigo.zabbix.utils.ProbesTags;
import com.indigo.zabbix.utils.PropertiesManager;
import com.indigo.zabbix.utils.ZabbixClient;
import com.indigo.zabbix.utils.beans.DocDataType;
import com.indigo.zabbix.utils.beans.ServiceInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.List;

/** Created by jose on 22/09/16. */
public class MesosThread {

  private static final Log logger = LogFactory.getLog(MesosThread.class);

  private ZabbixClient zabbixMesosClient = new ZabbixClient("IaaS", "Mesos", "TemplateMesos");

  private void startMonitoring() {
    String cmdbUrl = PropertiesManager.getProperty(ProbesTags.CMDB_URL);

    MesosCollector collector;

    if (cmdbUrl == null) {
      logger.error("CMDB Url not found. Assuming Mesos URLs are in the configuration file");
      collector = new MesosCollector();
      zabbixMesosClient.sendMetrics(collector.getMetrics());
    } else {
      CmdbClient cmdbClient = new CmdbClient();
      List<ServiceInfo> mesosServices = cmdbClient.getServiceList(DocDataType.ServiceType.MESOS);
      for (ServiceInfo info : mesosServices) {
        try {
          collector = new MesosCollector(info.getDoc().getData().getEndpoint());
          zabbixMesosClient.sendMetrics(collector.getMetrics());
        } catch (Exception e) {
          logger.error(
              "Error getting metrics from Mesos host " + info.getDoc().getData().getEndpoint(), e);
        }
      }
    }
  }

  /**
   * Start monitoring process.
   *
   * @param args Arguments will be ignored.
   */
  public static void main(String[] args) {

    try {
      PropertiesManager.loadProperties(MesosProbeTags.CONFIG_FILE);
      MesosThread thread = new MesosThread();
      thread.startMonitoring();
    } catch (IOException e) {
      logger.error("Error reading configuration file", e);
    }
  }
}
