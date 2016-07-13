package it.reply.utils.web.ws.rest.apiclients.zabbix;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.JsonRpcError;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

import javax.annotation.Generated;

/**
 * This class is the wrapper for every response of Prisma Rest Protocol.
 * 
 * @author l.biava
 * 
 * @param <ResultType>
 *          the type of generic object result
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "meta", "result", "error" })
public class ZabbixResponseWrapper<ResultTypeT> implements Serializable {

  private static final long serialVersionUID = -2767269124571953296L;
  @JsonProperty("id")
  private Integer id;
  @JsonProperty("result")
  private ResultTypeT result;
  @JsonProperty("error")
  private JsonRpcError error;

  @JsonProperty("id")
  public void setMeta(Integer id) {
    this.id = id;
  }

  @JsonProperty("result")
  public ResultTypeT getResult() {
    return result;
  }

  @JsonProperty("result")
  public void setResult(ResultTypeT result) {
    this.result = result;
  }

  @JsonProperty("error")
  public JsonRpcError getError() {
    return error;
  }

  @JsonProperty("error")
  public void setError(JsonRpcError error) {
    this.error = error;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object other) {
    return EqualsBuilder.reflectionEquals(this, other);
  }

}