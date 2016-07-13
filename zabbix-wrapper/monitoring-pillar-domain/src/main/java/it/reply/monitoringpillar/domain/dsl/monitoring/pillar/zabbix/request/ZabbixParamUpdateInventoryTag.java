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
@JsonPropertyOrder({ "hostid", "inventory" })
public class ZabbixParamUpdateInventoryTag {

  @JsonProperty("hostid")
  private String hostid;
  @JsonProperty("inventory")
  private SearchInventory inventory;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("hostid")
  public String getHostid() {
    return hostid;
  }

  @JsonProperty("hostid")
  public void setHostid(String hostid) {
    this.hostid = hostid;
  }

  @JsonProperty("inventory")
  public SearchInventory getInventory() {
    return inventory;
  }

  @JsonProperty("inventory")
  public void setInventory(SearchInventory inventory) {
    this.inventory = inventory;
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
