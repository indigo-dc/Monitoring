package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "host", "proxyHostid", "macros", "interfaces", "groups", "templates",
    "inventory" })
public class ZabbixParamCreateHostRequest {

  @JsonProperty("host")
  private String host;
  @JsonProperty("proxyHostid")
  private String proxyHostid;
  @JsonProperty("macros")
  private List<Macros> macros;
  @JsonProperty("interfaces")
  private ArrayList<ZabbixParamInterfaceIntoHostCreateRequest> interfaces =
      new ArrayList<ZabbixParamInterfaceIntoHostCreateRequest>();
  @JsonProperty("groups")
  private ArrayList<ZabbixParamGroupIntoHostCreateRequest> groups =
      new ArrayList<ZabbixParamGroupIntoHostCreateRequest>();
  @JsonProperty("templates")
  private List<String> templates = new ArrayList<String>();
  @JsonProperty("inventory")
  private Inventory inventory = new Inventory();
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("host")
  public String getHost() {
    return host;
  }

  @JsonProperty("host")
  public void setHost(String host) {
    this.host = host;
  }

  @JsonProperty("proxyHostid")
  public String getProxy_hostid() {
    return proxyHostid;
  }

  @JsonProperty("proxyHostid")
  public void setProxy_hostid(String proxyHostid) {
    this.proxyHostid = proxyHostid;
  }

  @JsonProperty("macros")
  public List<Macros> getMacros() {
    return macros;
  }

  @JsonProperty("macros")
  public void setMacros(List<Macros> macros) {
    this.macros = macros;
  }

  @JsonProperty("interfaces")
  public List<ZabbixParamInterfaceIntoHostCreateRequest> getInterfaces() {
    return interfaces;
  }

  @JsonProperty("interfaces")
  public void setInterfaces(ArrayList<ZabbixParamInterfaceIntoHostCreateRequest> zabbixInterface) {
    this.interfaces = zabbixInterface;
  }

  @JsonProperty("groups")
  public List<ZabbixParamGroupIntoHostCreateRequest> getGroups() {
    return groups;
  }

  @JsonProperty("groups")
  public void setGroups(ArrayList<ZabbixParamGroupIntoHostCreateRequest> groups) {
    this.groups = groups;
  }

  @JsonProperty("templates")
  public List<String> getTemplates() {
    return templates;
  }

  @JsonProperty("templates")
  public void setTemplates(List<String> templates) {
    this.templates = templates;
  }

  @JsonProperty("inventory")
  public Inventory getInventory() {
    return inventory;
  }

  @JsonProperty("inventory")
  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
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