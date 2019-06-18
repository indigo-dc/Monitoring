package com.indigo.zabbix.utils.beans;

import java.util.ArrayList;
import java.util.List;

public class ZabbixWrapperGroupsResult {

  public static class Group {

    public class PaasMachine {
      private String machineName;

      public String getMachineName() {
        return machineName;
      }

      public void setMachineName(String machineName) {
        this.machineName = machineName;
      }
    }

    private String groupName;
    private List<PaasMachine> paasMachines = new ArrayList<>();

    public Group() {}

    public Group(String groupName) {
      this.groupName = groupName;
    }

    public String getGroupName() {
      return groupName;
    }

    public void setGroupName(String groupName) {
      this.groupName = groupName;
    }

    public List<PaasMachine> getPaasMachines() {
      return paasMachines;
    }

    public void setPaasMachines(List<PaasMachine> paasMachines) {
      this.paasMachines = paasMachines;
    }
  }

  private List<Group> groups = new ArrayList<>();

  public List<Group> getGroups() {
    return groups;
  }

  public void setGroups(List<Group> groups) {
    this.groups = groups;
  }
}
