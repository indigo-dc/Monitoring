package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "groupsIdInMetrics", "groupsIdinWatcher", "groupsIdinIaaS" })
public class UpdatedPaasGroup implements Serializable {

  private static final long serialVersionUId = -8425673865503069848L;

  @JsonProperty("groupsIdInMetrics")
  private List<String> groupsIdInMetrics = new ArrayList<String>();
  @JsonProperty("groupsIdinWatcher")
  private List<String> groupsIdinWatcher = new ArrayList<String>();
  @JsonProperty("groupsIdinIaaS")
  private List<String> groupsIdinIaaS = new ArrayList<String>();

  @JsonProperty("groupsIdInMetrics")
  public List<String> getGroupsIdInMetrics() {
    return groupsIdInMetrics;
  }

  @JsonProperty("groupsIdInMetrics")
  public void setGroupsIdInMetrics(List<String> groupsIdInMetrics) {
    this.groupsIdInMetrics = groupsIdInMetrics;
  }

  @JsonProperty("groupsIdinWatcher")
  public List<String> getGroupsIdinWatcher() {
    return groupsIdinWatcher;
  }

  @JsonProperty("groupsIdinWatcher")
  public void setGroupsIdinWatcher(List<String> groupsIdinWatcher) {
    this.groupsIdinWatcher = groupsIdinWatcher;
  }

  @JsonProperty("groupsIdinIaaS")
  public List<String> getgroupsIdinIaaS() {
    return groupsIdinIaaS;
  }

  @JsonProperty("groupsIdinWatcher")
  public void setGroupsIdinIaaS(List<String> groupsIdinIaaS) {
    this.groupsIdinIaaS = groupsIdinIaaS;
  }

}
