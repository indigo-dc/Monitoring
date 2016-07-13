package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.protocol;

import it.reply.domain.dsl.prisma.ErrorCode;
import it.reply.domain.dsl.prisma.restprotocol.PrismaResponseWrapper;

import java.io.Serializable;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.StatusType;

public class MonitoringResponse implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  public static MonitoringResponseBuilder status(StatusType status) {
    return new MonitoringResponseBuilder(status);
  }

  public static <ResultTypeT> MonitoringResponseBuilder status(StatusType status,
      ResultTypeT result) {
    return new MonitoringResponseBuilder(status, result);
  }

  public static MonitoringResponseBuilder status(StatusType status, Error error) {
    return new MonitoringResponseBuilder(status, error);
  }

  public static MonitoringResponseBuilder status(StatusType status, ErrorCode errorCode,
      String verbose) {

    return new MonitoringResponseBuilder(status, errorCode, verbose);
  }

  /**
   * Get builder.
   */
  public static MonitoringResponseBuilder status(StatusType status, String errorMsg,
      String verbose) {
    MonitoringErrorCodeImpl error = new MonitoringErrorCodeImpl(status.getStatusCode(),
        status.getReasonPhrase(), errorMsg, status.getStatusCode());
    return new MonitoringResponseBuilder(status, error, verbose);
  }

  public static class MonitoringResponseBuilder {

    private final ResponseBuilder responseBuilder;

    public <ResultType> MonitoringResponseBuilder(StatusType status) {
      PrismaResponseWrapper<?> prismaResponseWrapper = PrismaResponseWrapper.status(status).build();
      responseBuilder = Response.status(status).entity(prismaResponseWrapper);
    }

    /**
     * Ger monitoring builder response.
     * 
     * @param status
     *          status
     * @param result
     *          result
     */
    public <ResultType> MonitoringResponseBuilder(StatusType status, ResultType result) {
      PrismaResponseWrapper<?> prismaResponseWrapper =
          PrismaResponseWrapper.status(status, result).build();
      responseBuilder = Response.status(status).entity(prismaResponseWrapper);
    }

    /**
     * Get builder.
     * 
     * @param status
     *          status
     * @param errorCode
     *          error
     * @param verbose
     *          verbose
     */
    public MonitoringResponseBuilder(StatusType status, ErrorCode errorCode, String verbose) {
      PrismaResponseWrapper<?> prismaResponseWrapper =
          PrismaResponseWrapper.status(status, errorCode, verbose).build();
      responseBuilder = Response.status(status).entity(prismaResponseWrapper);
    }

    /**
     * Get builder.
     * 
     * @param status
     *          status
     * @param error
     *          error
     */
    public MonitoringResponseBuilder(StatusType status, Error error) {
      PrismaResponseWrapper<?> prismaResponseWrapper =
          PrismaResponseWrapper.status(status, error).build();
      responseBuilder = Response.status(status).entity(prismaResponseWrapper);
    }

    public ResponseBuilder build() {
      return responseBuilder;
    }

  }
}
