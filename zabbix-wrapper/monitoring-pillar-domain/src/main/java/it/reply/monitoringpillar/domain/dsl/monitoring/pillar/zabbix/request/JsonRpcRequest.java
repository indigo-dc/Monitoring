package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "jsonrpc", "method", "params", "auth", "id" })
public class JsonRpcRequest<T> {

  @JsonProperty("jsonrpc")
  private String jsonrpc;
  @JsonProperty("method")
  private String method;
  @JsonProperty("params")
  private T params;
  @JsonProperty("auth")
  private Object auth;
  @JsonProperty("id")
  private Integer id;
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("jsonrpc")
  public String getJsonrpc() {
    return jsonrpc;
  }

  @JsonProperty("jsonrpc")
  public void setJsonrpc(String jsonrpc) {
    this.jsonrpc = jsonrpc;
  }

  @JsonProperty("method")
  public String getMethod() {
    return method;
  }

  @JsonProperty("method")
  public void setMethod(String method) {
    this.method = method;
  }

  @JsonProperty
  public T getParams() {
    return params;
  }

  @JsonProperty
  public void setParams(T tp) {
    this.params = tp;
  }

  @JsonProperty("auth")
  public Object getAuth() {
    return auth;
  }

  @JsonProperty("auth")
  public void setAuth(Object auth) {
    this.auth = auth;
  }

  @JsonProperty("id")
  public Integer getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(Integer id) {
    this.id = id;
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