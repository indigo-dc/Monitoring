package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "proxyid", "hosts" })
public class ZabbixUpdateProxyRequest {

  @JsonProperty("proxyid")
  private String proxyid;
  @JsonProperty("hosts")
  private List<String> hosts = new ArrayList<String>();
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("proxyid")
  public String getProxyid() {
    return proxyid;
  }

  @JsonProperty("proxyid")
  public void setProxyid(String proxyid) {
    this.proxyid = proxyid;
  }

  @JsonProperty("hosts")
  public List<String> getHosts() {
    return hosts;
  }

  @JsonProperty("hosts")
  public void setHosts(List<String> hosts) {
    this.hosts = hosts;
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