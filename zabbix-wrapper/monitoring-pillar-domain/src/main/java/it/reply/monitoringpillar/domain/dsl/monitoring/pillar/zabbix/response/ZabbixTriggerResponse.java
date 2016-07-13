package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "triggerid", "expression", "description", "url", "status", "value", "priority",
    "lastchange", "comments", "error", "templateid", "type", "state", "flags" })
public class ZabbixTriggerResponse {

  @JsonProperty("triggerid")
  private String triggerid;
  @JsonProperty("expression")
  private String expression;
  @JsonProperty("description")
  private String description;
  @JsonProperty("url")
  private String url;
  @JsonProperty("status")
  private String status;
  @JsonProperty("value")
  private String value;
  @JsonProperty("priority")
  private String priority;
  @JsonProperty("lastchange")
  private String lastchange;
  @JsonProperty("comments")
  private String comments;
  @JsonProperty("error")
  private String error;
  @JsonProperty("templateid")
  private String templateid;
  @JsonProperty("type")
  private String type;
  @JsonProperty("state")
  private String state;
  @JsonProperty("flags")
  private String flags;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("triggerid")
  public String getTriggerid() {
    return triggerid;
  }

  @JsonProperty("triggerid")
  public void setTriggerid(String triggerid) {
    this.triggerid = triggerid;
  }

  @JsonProperty("expression")
  public String getExpression() {
    return expression;
  }

  @JsonProperty("expression")
  public void setExpression(String expression) {
    this.expression = expression;
  }

  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  @JsonProperty("url")
  public String getUrl() {
    return url;
  }

  @JsonProperty("url")
  public void setUrl(String url) {
    this.url = url;
  }

  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(String status) {
    this.status = status;
  }

  @JsonProperty("value")
  public String getValue() {
    return value;
  }

  @JsonProperty("value")
  public void setValue(String value) {
    this.value = value;
  }

  @JsonProperty("priority")
  public String getPriority() {
    return priority;
  }

  @JsonProperty("priority")
  public void setPriority(String priority) {
    this.priority = priority;
  }

  @JsonProperty("lastchange")
  public String getLastchange() {
    return lastchange;
  }

  @JsonProperty("lastchange")
  public void setLastchange(String lastchange) {
    this.lastchange = lastchange;
  }

  @JsonProperty("comments")
  public String getComments() {
    return comments;
  }

  @JsonProperty("comments")
  public void setComments(String comments) {
    this.comments = comments;
  }

  @JsonProperty("error")
  public String getError() {
    return error;
  }

  @JsonProperty("error")
  public void setError(String error) {
    this.error = error;
  }

  @JsonProperty("templateid")
  public String getTemplateid() {
    return templateid;
  }

  @JsonProperty("templateid")
  public void setTemplateid(String templateid) {
    this.templateid = templateid;
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(String type) {
    this.type = type;
  }

  @JsonProperty("state")
  public String getState() {
    return state;
  }

  @JsonProperty("state")
  public void setState(String state) {
    this.state = state;
  }

  @JsonProperty("flags")
  public String getFlags() {
    return flags;
  }

  @JsonProperty("flags")
  public void setFlags(String flags) {
    this.flags = flags;
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

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(triggerid).append(expression).append(description)
        .append(url).append(status).append(value).append(priority).append(lastchange)
        .append(comments).append(error).append(templateid).append(type).append(state).append(flags)
        .append(additionalProperties).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof ZabbixTriggerResponse) == false) {
      return false;
    }
    ZabbixTriggerResponse rhs = ((ZabbixTriggerResponse) other);
    return new EqualsBuilder().append(triggerid, rhs.triggerid).append(expression, rhs.expression)
        .append(description, rhs.description).append(url, rhs.url).append(status, rhs.status)
        .append(value, rhs.value).append(priority, rhs.priority).append(lastchange, rhs.lastchange)
        .append(comments, rhs.comments).append(error, rhs.error).append(templateid, rhs.templateid)
        .append(type, rhs.type).append(state, rhs.state).append(flags, rhs.flags)
        .append(additionalProperties, rhs.additionalProperties).isEquals();
  }

}
