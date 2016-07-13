package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "hostids", "output", "expandDescription", "expandExpression", "itemids",
    "onlyTrue", "group" })
public class ZabbixParamTriggerRequest {

  @JsonProperty("hostids")
  private String hostids;
  @JsonProperty("output")
  private String output;
  @JsonProperty("expandDescription")
  private boolean expandDescription;
  @JsonProperty("expandExpression")
  private boolean expandExpression;
  @JsonProperty("itemids")
  private List<String> itemids = new ArrayList<String>();
  @JsonProperty("onlyTrue")
  private Boolean onlyTrue;
  @JsonProperty("group")
  private String group;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("hostids")
  public String getHostids() {
    return hostids;
  }

  @JsonProperty("hostids")
  public void setHostids(String hostids) {
    this.hostids = hostids;
  }

  @JsonProperty("output")
  public String getOutput() {
    return output;
  }

  @JsonProperty("output")
  public void setOutput(String output) {
    this.output = output;
  }

  @JsonProperty("expandExpression")
  public boolean getexpandExpression() {
    return expandExpression;
  }

  @JsonProperty("expandExpression")
  public void setExpandExpression(boolean expandExpression) {
    this.expandExpression = expandExpression;
  }

  @JsonProperty("onlyTrue")
  public Boolean getOnly_true() {
    return onlyTrue;
  }

  @JsonProperty("onlyTrue")
  public void setOnly_true(Boolean onlyTrue) {
    this.onlyTrue = onlyTrue;
  }

  @JsonProperty("expandDescription")
  public Boolean getExpandDescription() {
    return expandDescription;
  }

  @JsonProperty("expandDescription")
  public void setExpandDescription(Boolean expandDescription) {
    this.expandDescription = expandDescription;
  }

  @JsonProperty("itemids")
  public List<String> getItemids() {
    return itemids;
  }

  @JsonProperty("itemids")
  public void setItemids(List<String> itemids) {
    this.itemids = itemids;
  }

  @JsonProperty("group")
  public String getGroup() {
    return group;
  }

  @JsonProperty("group")
  public void setGroup(String group) {
    this.group = group;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

}