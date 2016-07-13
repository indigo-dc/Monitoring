package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "servers" })
public class PillarServerType implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("servers")
  private List<String> servers = new ArrayList<>();

  @JsonProperty("servers")
  public List<String> getServers() {
    return servers;
  }

  @JsonProperty("servers")
  public void setServers(List<String> servers) {
    this.servers = servers;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public PillarServerType withServers(List<String> servers) {
    this.servers = servers;
    return this;
  }

}
