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
@JsonPropertyOrder({ "type", "main", "useip", "ip", "dns", "port" })
public class ZabbixParamInterfaceIntoHostCreateRequest {

  @JsonProperty("type")
  private Integer type;
  @JsonProperty("main")
  private Integer main;
  @JsonProperty("useip")
  private Integer useip;
  @JsonProperty("ip")
  private String ip;
  @JsonProperty("dns")
  private String dns;
  @JsonProperty("port")
  private String port;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("type")
  public Integer getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(Integer type) {
    this.type = type;
  }

  @JsonProperty("main")
  public Integer getMain() {
    return main;
  }

  @JsonProperty("main")
  public void setMain(Integer main) {
    this.main = main;
  }

  @JsonProperty("useip")
  public Integer getUseip() {
    return useip;
  }

  @JsonProperty("useip")
  public void setUseip(Integer useip) {
    this.useip = useip;
  }

  @JsonProperty("ip")
  public String getIp() {
    return ip;
  }

  @JsonProperty("ip")
  public void setIp(String ip) {
    this.ip = ip;
  }

  @JsonProperty("dns")
  public String getDns() {
    return dns;
  }

  @JsonProperty("dns")
  public void setDns(String dns) {
    this.dns = dns;
  }

  @JsonProperty("port")
  public String getPort() {
    return port;
  }

  @JsonProperty("port")
  public void setPort(String port) {
    this.port = port;
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
