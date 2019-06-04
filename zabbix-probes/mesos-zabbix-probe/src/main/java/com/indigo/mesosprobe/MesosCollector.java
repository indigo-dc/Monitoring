package com.indigo.mesosprobe;

import com.indigo.mesosprobe.mesos.MesosFeignClient;
import com.indigo.mesosprobe.mesos.beans.GetMetricsResponse;
import com.indigo.mesosprobe.mesos.beans.MesosMasterInfoBean;
import com.indigo.zabbix.utils.MetricsCollector;
import com.indigo.zabbix.utils.PropertiesManager;
import com.indigo.zabbix.utils.ZabbixMetrics;
import com.indigo.zabbix.utils.beans.ServiceInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Created by jose on 4/10/16. */
public class MesosCollector implements MetricsCollector {

  private static final Log LOGGER = LogFactory.getLog(MesosCollector.class);

  private static final String PREFIX = "Mesos_";

  private String mesosMasterUrl;
  private String hostname;

  public MesosCollector(String masterUrl) {
    this.mesosMasterUrl = masterUrl;
    this.hostname = findHostName(masterUrl);
  }

  public MesosCollector(ServiceInfo service) {
    this.mesosMasterUrl = service.getDoc().getData().getEndpoint();
    this.hostname = service.getDoc().getData().getProviderId();
  }

  private String findHostName(String masterUrl) {
    MesosFeignClient mesosClient = MesosClientFactory.getMesosClient(mesosMasterUrl);

    MesosMasterInfoBean leaderInfo = mesosClient.getMaster(new MesosMasterInfoBean());

    if (leaderInfo.getGetMaster() != null
        && leaderInfo.getGetMaster().getMasterInfo() != null
        && leaderInfo.getGetMaster().getMasterInfo().getHostname() != null) {
      return PREFIX + leaderInfo.getGetMaster().getMasterInfo().getHostname();
    } else {
      LOGGER.error("Can't find leader hostname");
    }
    return null;
  }

  private Map<String, String> readMetrics(GetMetricsResponse metrics, List<String> properties) {
    if (metrics != null
        && metrics.getGetMetrics() != null
        && metrics.getGetMetrics().getMetrics() != null) {
      return metrics
          .getGetMetrics()
          .getMetrics()
          .stream()
          .filter(metric -> properties.contains(metric.getName()))
          .collect(
              Collectors.toMap(
                  metric -> metric.getName().replace("/", "."), metric -> metric.getValue()));
    }
    return new HashMap<>();
  }

  /*private String findLeader() {
    if (mesosMasterUrl != null) {
      MesosFeignClient client = MesosClientFactory.getMesosClient(mesosMasterUrl);
      MesosMasterInfoBean redirect = client.getInfo(accessToken);
      String leader = redirect.getLeader();
      if (leader != null) {
        String[] leaderInfo = leader.split("@");
        if (leaderInfo.length == 2) {
          return leaderInfo[1];
        }
      }
    }
    return null;
  }*/

  @Override
  public ZabbixMetrics getMetrics() {

    List<String> properties = PropertiesManager.getListProperty(MesosProbeTags.MESOS_METRIC);

    if (properties != null && !properties.isEmpty()) {
      MesosFeignClient mesosClient = MesosClientFactory.getMesosClient(mesosMasterUrl);

      ZabbixMetrics result = new ZabbixMetrics();

      result.setHostName(this.getHostName());
      result.setHostGroup(this.getGroup());

      GetMetricsResponse metrics = mesosClient.getMetrics(new GetMetricsResponse(true));

      result.setMetrics(readMetrics(metrics, properties));

      result.setTimestamp(new Date().getTime());

      return result;
    }

    return null;
  }

  @Override
  public String getHostName() {
    return "Mesos";
  }

  @Override
  public String getGroup() {
    return this.hostname;
  }
}
