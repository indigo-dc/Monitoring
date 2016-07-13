
package it.reply.monitoringpillar.config.dsl;

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
@JsonPropertyOrder({ "rpcVersion", "version", "type" })
public class Adapter {

  @JsonProperty("rpcVersion")
  private String rpcVersion;
  @JsonProperty("version")
  private String version;
  @JsonProperty("type")
  private String type;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  /**
   * Get Rpc version.
   * 
   * @return The rpcVersion
   */
  @JsonProperty("rpcVersion")
  public String getRpcVersion() {
    return rpcVersion;
  }

  /**
   * Get rpc version zabbix API.
   * 
   * @param rpcVersion
   *          The rpc-version
   */
  @JsonProperty("rpcVersion")
  public void setRpcVersion(String rpcVersion) {
    this.rpcVersion = rpcVersion;
  }

  /**
   * Get version of code.
   * 
   * @return The version
   */
  @JsonProperty("version")
  public String getVersion() {
    return version;
  }

  /**
   * Set the version of code.
   * 
   * @param version
   *          The version
   */
  @JsonProperty("version")
  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * Get the type of server.
   * 
   * @return The type
   */
  @JsonProperty("type")
  public String getType() {
    return type;
  }

  /**
   * Set the type of server.
   * 
   * @param type
   *          The type
   */
  @JsonProperty("type")
  public void setType(String type) {
    this.type = type;
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
