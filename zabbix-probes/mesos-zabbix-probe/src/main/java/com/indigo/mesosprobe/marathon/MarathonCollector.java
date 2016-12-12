package com.indigo.mesosprobe.marathon;

import com.indigo.mesosprobe.MesosProbeTags;
import com.indigo.zabbix.utils.CrudCollector;
import com.indigo.zabbix.utils.PropertiesManager;
import com.indigo.zabbix.utils.beans.AppOperation;

import mesosphere.marathon.client.Marathon;
import mesosphere.marathon.client.model.v2.App;
import mesosphere.marathon.client.model.v2.Container;
import mesosphere.marathon.client.model.v2.Docker;
import mesosphere.marathon.client.model.v2.GetAppResponse;
import mesosphere.marathon.client.model.v2.GetServerInfoResponse;
import mesosphere.marathon.client.model.v2.Result;
import mesosphere.marathon.client.utils.MarathonException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;


/**
 * Created by jose on 25/10/16.
 */
public class MarathonCollector extends CrudCollector {

  private static final String PREFIX = "Marathon_";

  private static final Log logger = LogFactory.getLog(MarathonCollector.class);

  private static final String APP_NAME = "zabbix-test-app";

  Marathon client;

  /**
   * Default constructor.
   */
  public MarathonCollector() {

    String url = PropertiesManager.getProperty(MesosProbeTags.MARATHON_ENDPOINT);
    String username = PropertiesManager.getProperty(MesosProbeTags.MARATHON_USERNAME);
    String password = PropertiesManager.getProperty(MesosProbeTags.MARATHON_PASSWORD);

    client = mesosphere.marathon.client.MarathonClient.getInstanceWithBasicAuth(
        url, username, password
    );
  }

  @Override
  protected String getHostName() {
    try {
      GetServerInfoResponse serverInfo = client.getServerInfo();

      if (serverInfo != null) {
        GetServerInfoResponse.MarathonConfig config = serverInfo.getMarathon_config();
        if (config != null && config.getHostname() != null) {
          return PREFIX + config.getHostname();
        }
      }
    } catch (MarathonException e) {
      logger.error("Error getting host information",e);
    }
    return null;
  }

  @Override
  protected AppOperation clear() {

    int status = 0;
    long currentTime = new Date().getTime();

    GetAppResponse getAppResponse = null;
    try {
      getAppResponse = client.getApp(APP_NAME);
    } catch (MarathonException e) {
      if (404 == e.getStatus()) {
        long respTime = new Date().getTime() - currentTime;
        return new AppOperation(AppOperation.Operation.CLEAR, true, e.getStatus(), respTime);
      } else {
        long respTime = new Date().getTime() - currentTime;
        return new AppOperation(AppOperation.Operation.CLEAR, false, e.getStatus(), respTime);
      }
    }

    if (getAppResponse != null && getAppResponse.getApp() != null
        && ("/" + APP_NAME).equals(getAppResponse.getApp().getId())) {

      Result delResult = null;
      try {
        delResult = client.deleteApp(APP_NAME);
      } catch (MarathonException e) {
        logger.error("Can't clear test application",e);
        long respTime = new Date().getTime() - currentTime;
        return new AppOperation(AppOperation.Operation.CLEAR, false, e.getStatus(), respTime);
      }
    }

    long respTime = new Date().getTime() - currentTime;
    return new AppOperation(AppOperation.Operation.CLEAR, true, 200, respTime);
  }

  @Override
  protected AppOperation create() {
    long time = new Date().getTime();

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

    try {
      App created =  client.createApp(app);
    } catch (MarathonException e) {
      long respTime = new Date().getTime() - time;
      return new AppOperation(AppOperation.Operation.CREATE, false, e.getStatus(), respTime);
    }

    long respTime = new Date().getTime() - time;
    return new AppOperation(AppOperation.Operation.CREATE, true, 200, respTime);
  }

  @Override
  protected AppOperation retrieve() {
    long currentTime = new Date().getTime();

    try {
      GetAppResponse getAppResponse = client.getApp(APP_NAME);
      if (getAppResponse != null && getAppResponse.getApp() != null
          && ("/" + APP_NAME).equals(getAppResponse.getApp().getId())) {

        long respTime = new Date().getTime() - currentTime;
        return new AppOperation(AppOperation.Operation.RUN, true, 200, respTime);

      } else {
        long respTime = new Date().getTime() - currentTime;
        logger.error("Can't finde application by name " + APP_NAME);
        return new AppOperation(AppOperation.Operation.RUN, false, 404, respTime);
      }
    } catch (MarathonException e) {
      logger.error("Error getting application named " + APP_NAME, e);
      long respTime = new Date().getTime() - currentTime;
      return new AppOperation(AppOperation.Operation.RUN, false, e.getStatus(), respTime);
    }
  }

  @Override
  protected AppOperation delete() {

    long currentTime = new Date().getTime();

    try {
      Result deleteResult = client.deleteApp(APP_NAME);
      long respTime = new Date().getTime() - currentTime;
      return new AppOperation(AppOperation.Operation.DELETE,
          (deleteResult != null && deleteResult.getDeploymentId() != null), 200, respTime);
    } catch (MarathonException e) {
      long respTime = new Date().getTime() - currentTime;
      logger.error("Error deleting application " + APP_NAME);
      return new AppOperation(AppOperation.Operation.DELETE, false, e.getStatus(), respTime);
    }

  }

}
