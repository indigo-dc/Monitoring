package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({ "adapters" })
public class PillarAdapter implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("adapters")
  private List<String> adapters = new ArrayList<String>();

  @JsonProperty("adapters")
  public List<String> getAdapters() {
    return adapters;
  }

  @JsonProperty("adapters")
  public void setAdapters(List<String> list) {
    this.adapters = list;
  }

  public PillarAdapter withAdapters(List<String> adapters) {
    this.adapters = adapters;
    return this;
  }

}