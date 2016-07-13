package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MonitoringWrappedResponsePaasGroups4HostList implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("groups")
  private List<GroupWrapped4HostList> groups = new ArrayList<GroupWrapped4HostList>();

  @JsonProperty("groups")
  public List<GroupWrapped4HostList> getGroups() {
    return groups;
  }

  @JsonProperty("groups")
  public void setGroups(List<GroupWrapped4HostList> groups) {
    this.groups = groups;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}