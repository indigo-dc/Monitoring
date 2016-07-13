package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MonitoringWrappedResponsePaasGroups4MetricList implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("groups")
  private List<GroupWrapped4MetricList> groups = new ArrayList<GroupWrapped4MetricList>();

  @JsonProperty("groups")
  public List<GroupWrapped4MetricList> getGroups() {
    return groups;
  }

  @JsonProperty("groups")
  public void setGroups(List<GroupWrapped4MetricList> groups) {
    this.groups = groups;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}