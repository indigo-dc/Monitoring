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
@JsonPropertyOrder({ "name", "serverTypes" })
public class PillarZone4Server {

  @JsonProperty("name")
  private String name;
  @JsonProperty("serverTypes")
  private List<String> serverTypes = new ArrayList<String>();

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("serverTypes")
  public List<String> getServerTypes() {
    return serverTypes;
  }

  @JsonProperty("serverTypes")
  public void setServerTypes(List<String> serverTypes) {
    this.serverTypes = serverTypes;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}