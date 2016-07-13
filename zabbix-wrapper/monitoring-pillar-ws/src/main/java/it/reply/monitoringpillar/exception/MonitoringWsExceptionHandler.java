package it.reply.monitoringpillar.exception;

import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.protocol.MonitoringResponse;
import it.reply.monitoringpillar.domain.exception.DuplicateResourceMonitoringException;
import it.reply.monitoringpillar.domain.exception.IllegalArgumentMonitoringException;
import it.reply.monitoringpillar.domain.exception.MonitoringException;
import it.reply.monitoringpillar.domain.exception.NotFoundMonitoringException;
import it.reply.monitoringpillar.domain.exception.NotImplementedMonitoringException;
import it.reply.utils.misc.StackTrace;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.EJBException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * This class handles all the {@link MonitoringException} and it maps the exceptions to a proper
 * HTTP response. Exception stack trace is logged here.
 * 
 * @author m.bassi
 * 
 */
@Provider
public class MonitoringWsExceptionHandler implements ExceptionMapper<Exception> {

  private static final long serialVersionUID = 1L;

  private static Logger LOG = LogManager.getLogger(MonitoringWsExceptionHandler.class);

  @Override
  public Response toResponse(Exception ex) {

    while (ex instanceof EJBException) {
      Exception exc = ((EJBException) ex).getCausedByException();
      if (exc != null) {
        ex = exc;
      }

    }

    // Logs all the stack trace
    LOG.catching(ex);

    String verbose = null;
    verbose = StackTrace.getStackTraceToString(ex);

    if (ex instanceof NotImplementedMonitoringException) {
      return MonitoringResponse.status(Status.NOT_FOUND, ex.getMessage(), verbose).build().build();
    }
    if (ex instanceof IllegalArgumentMonitoringException) {
      return MonitoringResponse.status(Status.BAD_REQUEST, ex.getMessage(), verbose).build()
          .build();
    }
    if (ex instanceof IllegalArgumentMonitoringException) {
      return MonitoringResponse.status(Status.BAD_REQUEST, ex.getMessage(), verbose).build()
          .build();
    }
    if (ex instanceof DuplicateResourceMonitoringException) {
      return MonitoringResponse.status(Status.CONFLICT, ex.getMessage(), verbose).build().build();
    } else if (ex instanceof NotFoundMonitoringException) {
      return MonitoringResponse.status(Status.NOT_FOUND, ex.getMessage(), verbose).build().build();
    }
    return MonitoringResponse.status(Status.INTERNAL_SERVER_ERROR, ex.getMessage(), verbose).build()
        .build();
  }

}
