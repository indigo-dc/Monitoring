package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "output", "hostids", "templateids", "search", "sortfield", "itemids",
    "triggerids" })
public class ZabbixParamItemRequest {

  @JsonProperty("output")
  private String output;
  @JsonProperty("hostids")
  private String hostids;
  @JsonProperty("search")
  private ZabbixSearchKeyRequest search;
  @JsonProperty("sortfield")
  private String sortfield;

  @JsonProperty("templateids")
  private String templateids;

  @JsonProperty("itemids")
  private String itemids;

  @JsonProperty("triggerids")
  private String triggerids;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("output")
  public String getOutput() {
    return output;
  }

  @JsonProperty("output")
  public void setOutput(String output) {
    this.output = output;
  }

  @JsonProperty("hostids")
  public String getHostids() {
    return hostids;
  }

  @JsonProperty("hostids")
  public void setHostids(String hostids) {
    this.hostids = hostids;
  }

  @JsonProperty("search")
  public ZabbixSearchKeyRequest getSearch() {
    return search;
  }

  @JsonProperty("search")
  public void setSearch(ZabbixSearchKeyRequest search) {
    this.search = search;
  }

  @JsonProperty("sortfield")
  public String getSortfield() {
    return sortfield;
  }

  @JsonProperty("sortfield")
  public void setSortfield(String sortfield) {
    this.sortfield = sortfield;
  }

  @JsonProperty("templateids")
  public String getTemplateids() {
    return templateids;
  }

  @JsonProperty("templateids")
  public void setTemplateids(String templateids) {
    this.templateids = templateids;
  }

  @JsonProperty("itemids")
  public String getItemids() {
    return itemids;
  }

  @JsonProperty("itemids")
  public void setItemids(String itemids) {
    this.itemids = itemids;
  }

  @JsonProperty("triggerids")
  public String triggerids() {
    return triggerids;
  }

  @JsonProperty("triggerids")
  public void setTriggerids(String triggerids) {
    this.triggerids = triggerids;
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