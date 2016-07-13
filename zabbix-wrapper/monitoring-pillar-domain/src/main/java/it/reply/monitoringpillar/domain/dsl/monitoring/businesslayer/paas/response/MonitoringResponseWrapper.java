package it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.reply.domain.dsl.prisma.ErrorCode;
import it.reply.domain.dsl.prisma.restprotocol.Meta;
import it.reply.domain.dsl.prisma.restprotocol.PrismaResponseWrapper;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

import javax.annotation.Generated;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

/**
 * This class is the wrapper for every response of Prisma Rest Protocol.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "meta", "result", "error" })
public class MonitoringResponseWrapper<ResultTypeT> implements Serializable {

  private static final long serialVersionUID = -2767269124571953296L;
  @JsonProperty("meta")
  private Meta meta;
  @JsonProperty("result")
  private ResultTypeT result;
  @JsonProperty("error")
  private Error error;

  @JsonProperty("meta")
  public Meta getMeta() {
    return meta;
  }

  @JsonProperty("meta")
  public void setMeta(Meta meta) {
    this.meta = meta;
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
  public Error getError() {
    return error;
  }

  @JsonProperty("error")
  public void setError(Error error) {
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

  @JsonIgnore
  public boolean isApiError() {
    return getMeta().isError();
  }

  /**
   * Get builder.
   * 
   * @param status
   *          status
   * @return reutlType
   */
  public static <ResultTypeT> PrismaResponseWrapperBuilder status(StatusType status) {
    if (status.getFamily() != Family.SUCCESSFUL) {
      throw new IllegalArgumentException("An error response MUST have an Error body.");
    }
    return new PrismaResponseWrapperBuilder(status);
  }

  /**
   * Return wrapper builder.
   * 
   * @param status
   *          StatusType
   * @param result
   *          ResultType
   * @return wrapper builder
   */
  public static <ResultTypeT> PrismaResponseWrapperBuilder status(StatusType status,
      ResultTypeT result) {
    if (status.getFamily() != Family.SUCCESSFUL) {
      if (!(result instanceof Error)) {
        throw new IllegalArgumentException("An error response MUST have an Error body.");
      }
    }

    return new PrismaResponseWrapperBuilder(status, result);
  }

  /**
   * Get response builder.
   * 
   * @param status
   *          status Type rest
   * @param error
   *          Error
   * @return response builder
   */
  public static PrismaResponseWrapperBuilder status(StatusType status, Error error) {
    if (status.getFamily() != Family.CLIENT_ERROR && status.getFamily() != Family.SERVER_ERROR) {
      throw new IllegalArgumentException("Only error response CAN have an Error body.");
    }

    return new PrismaResponseWrapperBuilder(status, error);
  }

  /**
   * Gets response builder.
   * 
   * @param status
   *          StatusType
   * @param errorCode
   *          Error
   * @param verbose
   *          verbose
   * @return responseBuilder
   */
  public static PrismaResponseWrapperBuilder status(StatusType status, ErrorCode errorCode,
      String verbose) {
    if (status.getFamily() != Family.CLIENT_ERROR && status.getFamily() != Family.SERVER_ERROR) {
      throw new IllegalArgumentException("Only error response CAN have an Error body.");
    }

    return new PrismaResponseWrapperBuilder(status, errorCode, verbose);
  }

  public static class PrismaResponseWrapperBuilder {

    @SuppressWarnings("rawtypes")
    private final PrismaResponseWrapper responseWrapper;

    /**
     * Get Builder.
     */
    public <ResultType> PrismaResponseWrapperBuilder(StatusType status) {
      responseWrapper = new PrismaResponseWrapper<Object>();
      // responseWrapper.setResult(null);
      Meta meta = new Meta();
      meta.setStatus(status.getStatusCode());
      responseWrapper.setMeta(meta);
    }

    /**
     * Get response wrapper builder.
     * 
     * @param status
     *          status
     * @param result
     *          result
     */
    @SuppressWarnings("unchecked")
    public <ResultType> PrismaResponseWrapperBuilder(StatusType status, ResultType result) {
      responseWrapper = new PrismaResponseWrapper<ResultType>();
      responseWrapper.setResult(result);
      Meta meta = new Meta();
      meta.setStatus(status.getStatusCode());
      responseWrapper.setMeta(meta);
    }

    /**
     * GEt response builder.
     * 
     * @param status
     *          StatusType
     * @param errorCode
     *          Error
     * @param verbose
     *          verbose
     */
    public PrismaResponseWrapperBuilder(StatusType status, ErrorCode errorCode, String verbose) {
      responseWrapper = new PrismaResponseWrapper<Object>();

      Error error = new Error();
      Meta meta = new Meta();
      meta.setStatus(status.getStatusCode());
      responseWrapper.setMeta(meta);
    }

    /**
     * Get builder.
     * 
     * @param status
     *          statustype
     * @param error
     *          error
     */
    public PrismaResponseWrapperBuilder(StatusType status, Error error) {
      responseWrapper = new PrismaResponseWrapper<Object>();

      Meta meta = new Meta();
      meta.setStatus(status.getStatusCode());
      responseWrapper.setMeta(meta);
    }

    /**
     * Get Builder.
     * 
     * @param meta
     *          meta
     * @return builder
     */
    public PrismaResponseWrapperBuilder meta(Meta meta) {
      responseWrapper.setMeta(meta);
      return this;
    }

    public PrismaResponseWrapper<?> build() {
      return responseWrapper;
    }

  }

}
