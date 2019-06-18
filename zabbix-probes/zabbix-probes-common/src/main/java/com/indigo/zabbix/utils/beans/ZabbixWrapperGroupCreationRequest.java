package com.indigo.zabbix.utils.beans;

public class ZabbixWrapperGroupCreationRequest {

  private String hostGroupName;

  public ZabbixWrapperGroupCreationRequest() {}

  public ZabbixWrapperGroupCreationRequest(String hostGroupName) {
    this.hostGroupName = hostGroupName;
  }

  public String getHostGroupName() {
    return hostGroupName;
  }

  public void setHostGroupName(String hostGroupName) {
    this.hostGroupName = hostGroupName;
  }
}
