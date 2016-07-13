package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "name", "groupid" })
public class HostGroupParamRequest {

  @JsonProperty("name")
  private String name;
  @JsonProperty("groupid")
  private String groupid;

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("groupid")
  public String getGroupid() {
    return groupid;
  }

  @JsonProperty("groupid")
  public void setGroupid(String groupid) {
    this.groupid = groupid;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}