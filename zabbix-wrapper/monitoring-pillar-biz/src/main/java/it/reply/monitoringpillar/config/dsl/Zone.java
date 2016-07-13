
package it.reply.monitoringpillar.config.dsl;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.reply.monitoringpillar.domain.exception.NotFoundMonitoringException;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "id", "distributedArchitecture", "name", "servers" })
public class Zone {

  @JsonProperty("id")
  private Integer id;
  @JsonProperty("distributedArchitecture")
  private Boolean distributedArchitecture;
  @JsonProperty("name")
  private String name;
  @JsonProperty("servers")
  private List<Server> servers = new ArrayList<Server>();
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("id")
  public Integer getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(Integer id) {
    this.id = id;
  }

  @JsonProperty("distributedArchitecture")
  public Boolean getDistributedArchitecture() {
    return distributedArchitecture;
  }

  @JsonProperty("distributedArchitecture")
  public void setDistributedArchitecture(Boolean distributedArchitecture) {
    this.distributedArchitecture = distributedArchitecture;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("servers")
  public List<Server> getServers() {
    return servers;
  }

  @JsonProperty("servers")
  public void setServers(List<Server> servers) {
    this.servers = servers;
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

  /**
   * Get list of servers.
   * 
   */
  public Server getServer(String type) {
    boolean serverFound = false;
    for (Server server : getServers()) {
      if (server.getType().equalsIgnoreCase(type)) {
        serverFound = true;
        return server;
      }
    }
    if (!serverFound) {
      throw new NotFoundMonitoringException("Unable to find server: " + type + " one of these: "
          + getServers().toString() + "have to be inserted");
    }
    return null;
  }

}
