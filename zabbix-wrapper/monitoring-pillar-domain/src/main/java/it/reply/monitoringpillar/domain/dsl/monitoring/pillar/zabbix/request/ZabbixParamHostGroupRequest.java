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
@JsonPropertyOrder({ "output", "filter", "hostids" })
public class ZabbixParamHostGroupRequest {

  @JsonProperty("output")
  private String output;
  @JsonProperty("filter")
  private ZabbixFilterRequest filter;
  @JsonProperty("hostids")
  private String hostids;
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

  @JsonProperty("filter")
  public ZabbixFilterRequest getFilter() {
    return filter;
  }

  @JsonProperty("filter")
  public void setFilter(ZabbixFilterRequest filter) {
    this.filter = filter;
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
