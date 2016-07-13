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
@JsonPropertyOrder({ "environment", "adapter", "options" })
public class MonitoringConfigurations {

  @JsonProperty("environment")
  private Environment environment;
  @JsonProperty("adapter")
  private Adapter adapter;
  @JsonProperty("options")
  private Options options;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("environment")
  public Environment getEnvironment() {
    return environment;
  }

  @JsonProperty("environment")
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  @JsonProperty("adapter")
  public Adapter getAdapter() {
    return adapter;
  }

  @JsonProperty("adapter")
  public void setAdapter(Adapter adapter) {
    this.adapter = adapter;
  }

  @JsonProperty("options")
  public Options getOptions() {
    return options;
  }

  @JsonProperty("options")
  public void setOptions(Options options) {
    this.options = options;
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
