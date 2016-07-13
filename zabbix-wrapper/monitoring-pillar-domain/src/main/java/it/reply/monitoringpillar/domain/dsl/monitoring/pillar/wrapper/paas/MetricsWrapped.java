package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.wrapper.paas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "machineName", "ip", "serviceCategory", "serviceId", "metrics" })
public class MetricsWrapped implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("metricName")
  private List<String> metricName;

  @JsonProperty("metricName")
  public List<String> getmetricName() {
    return metricName;
  }

  @JsonProperty("metricName")
  public void setmetricName(List<String> metricName) {
    this.metricName = metricName;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
