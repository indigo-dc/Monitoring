package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "pillarzone4Server" })
public class PillarServerTypesResponse {

  @JsonProperty("pillarzone4Server")
  private List<PillarZone4Server> pillarzone4Server = new ArrayList<PillarZone4Server>();

  @JsonProperty("pillarzone4Server")
  public List<PillarZone4Server> getPillarzone4Server() {
    return pillarzone4Server;
  }

  @JsonProperty("pillarzone4Server")
  public void setPillarzone4Server(List<PillarZone4Server> pillarzone4Server) {
    this.pillarzone4Server = pillarzone4Server;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}