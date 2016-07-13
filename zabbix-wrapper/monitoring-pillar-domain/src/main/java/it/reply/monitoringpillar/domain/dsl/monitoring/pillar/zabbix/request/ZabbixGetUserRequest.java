package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request;

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
@JsonPropertyOrder({ "output", "selectUsrgrps", "selectMediatypes" })
public class ZabbixGetUserRequest {

  @JsonProperty("output")
  private String output;
  @JsonProperty("selectUsrgrps")
  private String selectUsrgrps;
  @JsonProperty("selectMediatypes")
  private String selectMediatypes;
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

  @JsonProperty("selectUsrgrps")
  public String getSelectUsrgrps() {
    return selectUsrgrps;
  }

  @JsonProperty("selectUsrgrps")
  public void setSelectUsrgrps(String selectUsrgrps) {
    this.selectUsrgrps = selectUsrgrps;
  }

  @JsonProperty("selectMediatypes")
  public String getSelectMediatypes() {
    return selectMediatypes;
  }

  @JsonProperty("selectMediatypes")
  public void setSelectMediatypes(String selectMediatypes) {
    this.selectMediatypes = selectMediatypes;
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