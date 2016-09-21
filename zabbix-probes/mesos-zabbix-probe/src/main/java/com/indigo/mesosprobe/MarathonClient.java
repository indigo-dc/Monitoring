package com.indigo.mesosprobe;


import mesosphere.marathon.client.Marathon;
import mesosphere.marathon.client.model.v2.App;
import mesosphere.marathon.client.model.v2.Container;
import mesosphere.marathon.client.model.v2.Docker;
import mesosphere.marathon.client.model.v2.GetAppResponse;
import mesosphere.marathon.client.model.v2.Result;
import mesosphere.marathon.client.utils.MarathonException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class MarathonClient {

  private static final Log logger = LogFactory.getLog(MarathonClient.class);
  public static final String APP_NAME = "zabbix-test-app";

  /**
   * Test that Marathon installation is working.
   * @return result of the test.
   */
  public boolean testMarathon() {

    boolean result = false;

    String url = PropertiesManager.getProperty(MesosProbeTags.MARATHON_ENDPOINT);
    String username = PropertiesManager.getProperty(MesosProbeTags.MARATHON_USERNAME);
    String password = PropertiesManager.getProperty(MesosProbeTags.MARATHON_PASSWORD);

    Marathon client = mesosphere.marathon.client.MarathonClient.getInstanceWithBasicAuth(
        url, username, password
    );

    try {
      createApp(client);

      GetAppResponse appInfo = client.getApp(APP_NAME);
      if (appInfo != null && appInfo.getApp() != null) {

        Result deleteResult = client.deleteApp(APP_NAME);
        String depId = deleteResult.getDeploymentId();
        result = (depId != null);
      }

    } catch (MarathonException e) {
      logger.error("Error creating app " + APP_NAME,e);
    }


    return result;

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
