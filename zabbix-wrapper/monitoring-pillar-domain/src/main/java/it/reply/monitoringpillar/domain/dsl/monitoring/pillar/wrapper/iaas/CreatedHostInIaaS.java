package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.iaas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "hostIdInIaaS" })
public class CreatedHostInIaaS implements Serializable {

  private static final long serialVersionUId = -8425673865503069848L;

  @JsonProperty("hostIdInIaaS")
  private List<String> hostIdInIaaS = new ArrayList<String>();

  @JsonProperty("hostIdInIaaS")
  public List<String> getHostIdInIaaS() {
    return hostIdInIaaS;
  }

  @JsonProperty("hostIdInIaaS")
  public void setHostIdInIaaS(List<String> hostIdInIaaS) {
    this.hostIdInIaaS = hostIdInIaaS;
  }
}