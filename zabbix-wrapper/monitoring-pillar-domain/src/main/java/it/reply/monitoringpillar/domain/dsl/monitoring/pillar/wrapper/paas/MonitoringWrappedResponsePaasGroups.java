package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    // "environment",
    "groups" })
public class MonitoringWrappedResponsePaasGroups implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("groups")
  private List<GroupsWrappedName> groups = new ArrayList<GroupsWrappedName>();

  @JsonProperty("groups")
  public List<GroupsWrappedName> getGroups() {
    return groups;
  }

  @JsonProperty("groups")
  public void setGroups(List<GroupsWrappedName> groups) {
    this.groups = groups;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}