package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "hostIDInMetrics", "hostIDinWatcher" })
public class CreatedHostInPaaS implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("hostIDInMetrics")
  private List<String> hostIdInMetrics = new ArrayList<String>();
  @JsonProperty("hostIDinWatcher")
  private List<String> hostIDinWatcher = new ArrayList<String>();

  @JsonProperty("hostIDInMetrics")
  public List<String> getHostIdInMetrics() {
    return hostIdInMetrics;
  }

  @JsonProperty("hostIDInMetrics")
  public void setHostIdInMetrics(List<String> hostIdInMetrics) {
    this.hostIdInMetrics = hostIdInMetrics;
  }

  @JsonProperty("hostIDinWatcher")
  public List<String> getHostIDinWatcher() {
    return hostIDinWatcher;
  }

  @JsonProperty("hostIDinWatcher")
  public void setHostIDinWatcher(List<String> hostIDinWatcher) {
    this.hostIDinWatcher = hostIDinWatcher;
  }

}