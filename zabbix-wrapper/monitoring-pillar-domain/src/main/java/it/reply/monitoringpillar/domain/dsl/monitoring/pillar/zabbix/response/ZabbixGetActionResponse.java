package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "actionid", "name", "eventsource", "status", "esc_period", "def_shortdata",
    "def_longdata", "recovery_msg", "rshortdata", "rlongdata" })
public class ZabbixGetActionResponse {

  @JsonProperty("actionid")
  private String actionid;
  @JsonProperty("name")
  private String name;
  @JsonProperty("eventsource")
  private String eventsource;
  @JsonProperty("status")
  private String status;
  @JsonProperty("esc_period")
  private String escPeriod;
  @JsonProperty("def_shortdata")
  private String defShortdata;
  @JsonProperty("def_longdata")
  private String defLongdata;
  @JsonProperty("recovery_msg")
  private String recoveryMsg;
  @JsonProperty("rshortdata")
  private String rshortdata;
  @JsonProperty("rlongdata")
  private String rlongdata;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("actionid")
  public String getActionid() {
    return actionid;
  }

  @JsonProperty("actionid")
  public void setActionid(String actionid) {
    this.actionid = actionid;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("eventsource")
  public String getEventsource() {
    return eventsource;
  }

  @JsonProperty("eventsource")
  public void setEventsource(String eventsource) {
    this.eventsource = eventsource;
  }

  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(String status) {
    this.status = status;
  }

  @JsonProperty("esc_period")
  public String getEscPeriod() {
    return escPeriod;
  }

  @JsonProperty("esc_period")
  public void setEscPeriod(String escPeriod) {
    this.escPeriod = escPeriod;
  }

  @JsonProperty("def_shortdata")
  public String getDefShortdata() {
    return defShortdata;
  }

  @JsonProperty("def_shortdata")
  public void setDefShortdata(String defShortdata) {
    this.defShortdata = defShortdata;
  }

  @JsonProperty("def_longdata")
  public String getDefLongdata() {
    return defLongdata;
  }

  @JsonProperty("def_longdata")
  public void setDefLongdata(String defLongdata) {
    this.defLongdata = defLongdata;
  }

  @JsonProperty("recovery_msg")
  public String getRecoveryMsg() {
    return recoveryMsg;
  }

  @JsonProperty("recovery_msg")
  public void setRecoveryMsg(String recoveryMsg) {
    this.recoveryMsg = recoveryMsg;
  }

  @JsonProperty("rshortdata")
  public String getRShortdata() {
    return rshortdata;
  }

  @JsonProperty("rshortdata")
  public void setRShortdata(String rshortdata) {
    this.rshortdata = rshortdata;
  }

  @JsonProperty("rlongdata")
  public String getRLongdata() {
    return rlongdata;
  }

  @JsonProperty("rlongdata")
  public void setRLongdata(String rlongdata) {
    this.rlongdata = rlongdata;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
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
