package com.indigo.mesosprobe;

import com.indigo.zabbix.utils.MetricsCollector;
import com.indigo.zabbix.utils.PropertiesManager;
import com.indigo.zabbix.utils.ZabbixMetrics;

import mesosphere.marathon.client.Marathon;
import mesosphere.marathon.client.model.v2.App;
import mesosphere.marathon.client.model.v2.Container;
import mesosphere.marathon.client.model.v2.Docker;
import mesosphere.marathon.client.model.v2.GetAppResponse;
import mesosphere.marathon.client.model.v2.GetServerInfoResponse;
import mesosphere.marathon.client.model.v2.Result;
import mesosphere.marathon.client.utils.MarathonException;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by jose on 25/10/16.
 */
public class MarathonCollector implements MetricsCollector {

  private static final Log logger = LogFactory.getLog(MarathonCollector.class);

  private static final String APP_NAME = "zabbix-test-app";

  private static final String TRUE = "1";
  private static final String FALSE = "1";

  Marathon client;

  public MarathonCollector() {

    String url = PropertiesManager.getProperty(MesosProbeTags.MARATHON_ENDPOINT);
    String username = PropertiesManager.getProperty(MesosProbeTags.MARATHON_USERNAME);
    String password = PropertiesManager.getProperty(MesosProbeTags.MARATHON_PASSWORD);

    client = mesosphere.marathon.client.MarathonClient.getInstanceWithBasicAuth(
        url, username, password
    );
  }

  @Override
  public ZabbixMetrics getMetrics() {

    try {
      GetServerInfoResponse serverInfo = client.getServerInfo();

      if (serverInfo != null) {
        GetServerInfoResponse.MarathonConfig config = serverInfo.getMarathon_config();

        if (config != null) {
          String hostName = config.getHostname();

          ZabbixMetrics result = new ZabbixMetrics();
          result.setHostName(hostName);

          Map<String, String> metrics = new HashMap<>();

          boolean clear = clear(client);
          if (clear) {

            metrics.put("clear.result", TRUE);

            App created = createApp(client);
            if (created != null) {
              metrics.put("create.result", TRUE);

              GetAppResponse getAppResponse = client.getApp(APP_NAME);
              if (getAppResponse != null && getAppResponse.getApp() != null
                  && ("/" + APP_NAME).equals(getAppResponse.getApp().getId())) {
                metrics.put("run.result", TRUE);

                Result deleteResult = client.deleteApp(APP_NAME);
                String depId = deleteResult.getDeploymentId();

                metrics.put("delete.result", BooleanUtils.toString(depId != null, TRUE, FALSE));

              }

            } else {
              metrics.put("create.result", FALSE);
              metrics.put("run.result", FALSE);
              metrics.put("delete.result", FALSE);
            }
          } else {
            metrics.put("clear.result", FALSE);
            metrics.put("create.result", FALSE);
            metrics.put("run.result", FALSE);
            metrics.put("delete.result", FALSE);
          }

          result.setMetrics(metrics);
          return result;

        }
      }


    } catch (MarathonException e) {
      logger.error("Error getting marathon metrics",e);
    }

    return null;
  }

  private boolean clear(Marathon client) {
    GetAppResponse getAppResponse = null;
    try {
      getAppResponse = client.getApp(APP_NAME);
    } catch (MarathonException e) {
      if (404 == e.getStatus()) {
        return true;
      } else {
        return false;
      }
    }

    if (getAppResponse != null && getAppResponse.getApp() != null
        && ("/" + APP_NAME).equals(getAppResponse.getApp().getId())) {

      Result delResult = null;
      try {
        delResult = client.deleteApp(APP_NAME);
      } catch (MarathonException e) {
        logger.error("Can't clear test application",e);
        return false;
      }
      return delResult.getDeploymentId() != null;

    }

    return true;
  }

  private App createApp(Marathon client) throws MarathonException {

    App app = new App();
    app.setId(APP_NAME);

    Container container = new Container();
    container.setType("DOCKER");

    Docker docker = new Docker();
    docker.setImage("busybox");
    container.setDocker(docker);

    app.setContainer(container);

    app.setCmd("echo hi; sleep 10; echo bye;");
    app.setCpus(1.0);
    app.setMem(16.0);
    app.setInstances(1);
    return client.createApp(app);

  }
}
