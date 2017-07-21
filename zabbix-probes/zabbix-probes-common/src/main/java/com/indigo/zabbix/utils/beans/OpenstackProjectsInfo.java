package com.indigo.zabbix.utils.beans;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jose on 31/03/17.
 */
public class OpenstackProjectsInfo {

  public static class Project {

    @SerializedName("is_domain")
    private boolean isDomain;
    private String description;
    private boolean enabled;
    private String id;

    @SerializedName("parent_id")
    private String parentId;

    @SerializedName("domain_id")
    private String domainId;

    private String name;

    public boolean isDomain() {
      return isDomain;
    }

    public void setDomain(boolean domain) {
      isDomain = domain;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getParentId() {
      return parentId;
    }

    public void setParentId(String parentId) {
      this.parentId = parentId;
    }

    public String getDomainId() {
      return domainId;
    }

    public void setDomainId(String domainId) {
      this.domainId = domainId;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  private List<Project> projects;

  public List<Project> getProjects() {
    return projects;
  }

  public void setProjects(List<Project> projects) {
    this.projects = projects;
  }
}
