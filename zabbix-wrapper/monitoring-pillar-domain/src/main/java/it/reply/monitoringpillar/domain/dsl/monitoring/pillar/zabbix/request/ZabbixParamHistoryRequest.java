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
@JsonPropertyOrder({ "output", "itemids", "hostids", "limit", "history", "time_from", "time_till",
    "sortorder", "sortfield",
    // this one is useful for event field
    "selectAcknowledged" })
public class ZabbixParamHistoryRequest {

  @JsonProperty("output")
  private String output;
  @JsonProperty("itemids")
  private String itemids;
  @JsonProperty("hostids")
  private String hostids;
  @JsonProperty("limit")
  private Integer limit;
  @JsonProperty("history")
  private Integer history;
  @JsonProperty("time_from")
  private String timeFrom;
  @JsonProperty("time_till")
  private String timeTill;
  @JsonProperty("sortorder")
  private String sortorder;
  @JsonProperty("sortfield")
  private String sortfield;
  @JsonProperty("selectAcknowledged")
  private String selectAcknowledged;
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

  @JsonProperty("itemids")
  public String getItemids() {
    return itemids;
  }

  @JsonProperty("itemids")
  public void setItemids(String itemids) {
    this.itemids = itemids;
  }

  @JsonProperty("hostids")
  public String getHostids() {
    return hostids;
  }

  @JsonProperty("hostids")
  public void setHostids(String hostids) {
    this.hostids = hostids;
  }

  @JsonProperty("limit")
  public Integer getLimit() {
    return limit;
  }

  @JsonProperty("limit")
  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  @JsonProperty("history")
  public Integer getHistory() {
    return history;
  }

  @JsonProperty("history")
  public void setHistory(Integer history) {
    this.history = history;
  }

  @JsonProperty("time_from")
  public String getTimeFrom() {
    return timeFrom;
  }

  @JsonProperty("time_from")
  public void setTimeFrom(String timeFrom) {
    this.timeFrom = timeFrom;
  }

  @JsonProperty("time_till")
  public String getTimeTill() {
    return timeTill;
  }

  @JsonProperty("time_till")
  public void setTimeTill(String timeTill) {
    this.timeTill = timeTill;
  }

  @JsonProperty("sortorder")
  public String getSortorder() {
    return sortorder;
  }

  @JsonProperty("sortorder")
  public void setSortorder(String sortorder) {
    this.sortorder = sortorder;
  }

  @JsonProperty("sortfield")
  public String getSortfield() {
    return sortfield;
  }

  @JsonProperty("sortfield")
  public void setSortfield(String sortfield) {
    this.sortfield = sortfield;
  }

  @JsonProperty("selectAcknowledged")
  public String selectAcknowledged() {
    return selectAcknowledged;
  }

  @JsonProperty("selectAcknowledged")
  public void selectAcknowledged(String selectAcknowledged) {
    this.selectAcknowledged = selectAcknowledged;
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