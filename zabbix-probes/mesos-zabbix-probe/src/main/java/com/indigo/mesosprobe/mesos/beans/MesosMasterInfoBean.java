package com.indigo.mesosprobe.mesos.beans;

/**
 * Created by jose on 22/09/16.
 */
public class MesosMasterInfoBean {

  private String leader;

  private String pid;

  private String hostname;

  public String getLeader() {
    return leader;
  }

  public void setLeader(String leader) {
    this.leader = leader;
  }

  public String getPid() {
    return pid;
  }

  public void setPid(String pid) {
    this.pid = pid;
  }

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }
}
