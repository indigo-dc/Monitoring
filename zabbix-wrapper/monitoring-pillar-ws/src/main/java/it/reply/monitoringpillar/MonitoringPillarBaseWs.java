package it.reply.monitoringpillar;

import it.reply.monitoringpillar.config.Configuration;
import it.reply.monitoringpillar.domain.dsl.monitoring.Info;

import java.net.InetAddress;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class MonitoringPillarBaseWs {

  @Inject
  private Configuration config;

  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Info getHome() {
    return getInfo();
  }

  /**
   * Info version of the project.
   * 
   * @return Info object
   */
  @GET
  @Path("/info")
  @Produces(MediaType.APPLICATION_JSON)
  public Info getInfo() {
    Info info = new Info();
    info.setProjectVersion(config.getVersionProperty(Configuration.PROJECT_BUILD_VERSION));
    info.setProjectRevision(config.getVersionProperty(Configuration.PROJECT_BUILD_REVISION));
    info.setProjectTimestamp(config.getVersionProperty(Configuration.PROJECT_BUILD_TIMESTAMP));

    String hostname;
    try {
      hostname = InetAddress.getLocalHost().getHostName();
    } catch (Exception ex) {
      hostname = "NOT AVAILABLE";
    }
    info.setServerHostname(hostname);

    String re = config.getMonitoringConfigurations().getEnvironment().getName();
    re = (re != null && re.length() > 0 ? re : "NOT AVAILABLE");
    info.setRuntimeEnvironment(re);

    return info;
  }

  @GET
  @Path("/misc/debug")
  @Produces(MediaType.APPLICATION_JSON)
  public Info getDebugInfo() {
    return getInfo();
  }

}