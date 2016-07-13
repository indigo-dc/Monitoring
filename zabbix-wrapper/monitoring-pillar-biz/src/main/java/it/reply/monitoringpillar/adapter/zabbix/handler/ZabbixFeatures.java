package it.reply.monitoringpillar.adapter.zabbix.handler;

import java.util.ArrayList;
import java.util.List;

public class ZabbixFeatures {

  public enum ZabbixMethods {
    HOSTCREATE("host.create"), HOSTGROUPCREATE("hostgroup.create"), HOSTDELETE(
        "host.delete"), HOSTGROUPUPDATE("hostgroup.update"), GROUPDELETE("hostgroup.delete"), HOST(
            "host.get"), HOSTGROUP("hostgroup.get"), TEMPLATE("template.get"), INTERFACE(
                "hostinterface.get"), METRIC("item.get"), TRIGGER("trigger.get"), HISTORY(
                    "history.get"), EVENT("event.get"), MASSUPDATE("host.massupdate"), HOSTUPDATE(
                        "host.update"), ITEMUPDATE("item.update"), TREND("trend.get"), GETPROXY(
                            "proxy.get"), CREATEPROXY("proxy.create"), DELETEPROXY(
                                "proxy.delete"), GETUSERS("user.get"), CREATEUSER("user.create");

    private ZabbixMethods(String zabbixMethod) {
      this.zabbixMethod = zabbixMethod;
    }

    private String zabbixMethod;

    public String getzabbixMethod() {
      return this.zabbixMethod;
    }

    public String toString() {
      return this.zabbixMethod;
    }
  }

  public enum ServerSignature {

    SERVERIAAS("infrastructure"), SERVERMETRICS("service"), SERVERWATCHER("watcher");

    private String serverType;

    private ServerSignature(String serverType) {
      this.serverType = serverType;
    }

    public String getServerType() {
      return this.serverType;
    }

    public String toString() {
      return this.serverType;
    }

    /**
     * Get list of servers.
     * 
     * @return list of servers
     */
    public static List<String> getAllServer() {
      List<String> server = new ArrayList<String>();
      for (ServerSignature a : values()) {
        server.add(a.getServerType());
      }
      return server;
    }

    /**
     * Get server type.
     * 
     * @param server
     *          type
     * @return type
     * @throws IllegalArgumentException
     *           excep
     */
    public static ServerSignature lookupFromName(String server) throws IllegalArgumentException {
      for (ServerSignature s : values()) {
        if (server.equals(s.getServerType())) {
          return s;
        }
      }
      throw new IllegalArgumentException("Cannot find [" + server + "] into ServerType enum");
    }
  }

  public enum ServerType {

    SERVERIAAS("infrastructure"), SERVERMETRICS("service"), SERVERWATCHER("watcher"), PAAS(
        "paas"), ALL("all");

    private String serverType;

    private ServerType(String serverType) {
      this.serverType = serverType;
    }

    public String getServerType() {
      return this.serverType;
    }

    public String toString() {
      return this.serverType;
    }

    /**
     * Get servers.
     * 
     * @return list of servers
     */
    public static List<String> getAllServer() {
      List<String> server = new ArrayList<String>();
      for (ServerType a : values()) {
        server.add(a.getServerType());
      }
      return server;
    }

    /**
     * Get servers.
     * 
     * @param server
     *          type
     * @return server type
     * @throws IllegalArgumentException
     *           excep
     */
    public static ServerType lookupFromName(String server) throws IllegalArgumentException {
      for (ServerType s : values()) {
        if (server.equals(s.getServerType())) {
          return s;
        }
      }
      throw new IllegalArgumentException("Cannot find [" + server + "] into ServerType enum");
    }
  }

}
