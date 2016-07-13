package it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "hosts", "itemid", "clock", "value", "ns" })

public class MonitoringGraphHistoryResponse implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("hosts")
  private List<MonitoringVmCredentialsResponse> hosts =
      new ArrayList<MonitoringVmCredentialsResponse>();
  @JsonProperty("itemid")
  private String itemid;
  @JsonProperty("clock")
  private String clock;
  @JsonProperty("value")
  private String value;
  @JsonProperty("ns")
  private String ns;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("hosts")
  public List<MonitoringVmCredentialsResponse> getHosts() {
    return hosts;
  }

  @JsonProperty("hosts")
  public void setHosts(List<MonitoringVmCredentialsResponse> hosts) {
    this.hosts = hosts;
  }

  @JsonProperty("itemid")
  public String getItemid() {
    return itemid;
  }

  @JsonProperty("itemid")
  public void setItemid(String itemid) {
    this.itemid = itemid;
  }

  @JsonProperty("clock")
  public String getClock() {
    return clock;
  }

  @JsonProperty("clock")
  public void setClock(String clock) {
    this.clock = clock;
  }

  @JsonProperty("value")
  public String getValue() {
    return value;
  }

  @JsonProperty("value")
  public void setValue(String value) {
    this.value = value;
  }

  @JsonProperty("ns")
  public String getNs() {
    return ns;
  }

  @JsonProperty("ns")
  public void setNs(String ns) {
    this.ns = ns;
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
