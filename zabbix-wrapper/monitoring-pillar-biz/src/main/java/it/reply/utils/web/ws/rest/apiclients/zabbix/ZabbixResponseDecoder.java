package it.reply.utils.web.ws.rest.apiclients.zabbix;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.JsonRpcResponse;
import it.reply.utils.web.ws.rest.apiencoding.MappingException;
import it.reply.utils.web.ws.rest.apiencoding.NoMappingModelFoundException;
import it.reply.utils.web.ws.rest.apiencoding.RestMessage;
import it.reply.utils.web.ws.rest.apiencoding.ServerErrorResponseException;
import it.reply.utils.web.ws.rest.apiencoding.decode.BaseRestResponseDecoder;
import it.reply.utils.web.ws.rest.apiencoding.decode.BaseRestResponseResult;
import it.reply.utils.web.ws.rest.apiencoding.decode.RestResponseDecodeStrategy;

import org.javatuples.Pair;

import java.io.IOException;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

public class ZabbixResponseDecoder<ApiResponseTypeT> extends BaseRestResponseDecoder {

  public ZabbixResponseDecoder(JavaType targetClass) {
    super();
    this.defaultDecodeStrategy = new ZabbixRdStrategy<ApiResponseTypeT>(targetClass);
  }

  public ZabbixResponseDecoder(Class<ApiResponseTypeT> cl) {
    super();
    this.defaultDecodeStrategy = new ZabbixRdStrategy<ApiResponseTypeT>(cl);
  }

  public ZabbixResponseDecoder(@SuppressWarnings("rawtypes") Class... cl) {
    super();
    this.defaultDecodeStrategy = new ZabbixRdStrategy<ApiResponseTypeT>(cl);
  }

  /**
   * <b>Supports only default strategy. MUST pass null in strategy field !</b> <br/>
   * {@inheritDoc}
   */
  @Override
  public BaseRestResponseResult decode(RestMessage msg, RestResponseDecodeStrategy strategy)
      throws NoMappingModelFoundException, MappingException, ServerErrorResponseException {

    if (strategy != null) {
      throw new UnsupportedOperationException(
          "Only default decoding strategy can be used with this decoder.");
    }

    return decode(msg);
  }

  @Override
  public BaseRestResponseResult decode(RestMessage msg)
      throws NoMappingModelFoundException, MappingException, ServerErrorResponseException {

    StatusType status = defaultDecodeStrategy.getStatus(msg);
    JavaType mappingClass = defaultDecodeStrategy.getModelClass(msg);

    // Check also content type (application/json)
    if (msg.getHeaders().containsKey(HttpHeaders.CONTENT_TYPE) && !msg.getHeaders()
        .getFirst(HttpHeaders.CONTENT_TYPE).equals(MediaType.APPLICATION_JSON)) {
      throw new MappingException("Not JSON encoded body.", null, msg);
    }

    Object result = null;
    if (msg.getBody() != null) {
      try {
        result = new ObjectMapper().readValue((String) msg.getBody(), mappingClass);
      } catch (IOException ioe) {
        throw new MappingException(ioe.getMessage(), ioe, msg);
      }
    }

    return new BaseRestResponseResult(status, result, mappingClass, msg);
  }

  @SuppressWarnings("hiding")
  public class ZabbixRdStrategy<ApiResponseTypeT> implements RestResponseDecodeStrategy {

    private JavaType targetClass;

    /**
     * Constructor for a provided target class type descriptor.
     * 
     * @param targetClass
     *          the {@link JavaType} mapping class descriptor.
     */
    public ZabbixRdStrategy(JavaType targetClass) {
      this.targetClass = targetClass;
    }

    /**
     * Constructor for a single class type (not nested types ! Use the other constructor instead,
     * <code>PrismaRRDStrategy(Class... c)</code>).
     */
    public ZabbixRdStrategy(Class<ApiResponseTypeT> cl) {
      ObjectMapper mapper = new ObjectMapper();
      targetClass = mapper.getTypeFactory().constructType(cl);
    }

    /**
     * Constructor for nested classes types (ie. List&lt;Set&lt;String&gt;&gt;).
     * 
     * @param cl
     *          an array of nested classes. <br/>
     *          For example, to express the following: Use: new PrismaRRDStrategy(List.class,
     *          Set.class, String.class)
     * 
     */
    public ZabbixRdStrategy(@SuppressWarnings("rawtypes") Class... cl) {
      if (cl.length < 2) {
        throw new IllegalArgumentException(
            "The array of nested classes must be at least of two items !");
      }

      int num = cl.length;
      TypeFactory tf = TypeFactory.defaultInstance();
      targetClass = tf.constructParametricType(cl[num - 2], cl[num - 1]);
      for (int i = num - 3; i >= 0; i--) {
        targetClass = tf.constructParametricType(cl[i], targetClass);
      }

    }

    @Override
    public JavaType getModelClass(RestMessage msg)
        throws NoMappingModelFoundException, ServerErrorResponseException {
      Pair<StatusType, JavaType> result = strategy(msg);
      return result.getValue1();
    }

    @Override
    public StatusType getStatus(RestMessage msg)
        throws NoMappingModelFoundException, ServerErrorResponseException {
      Pair<StatusType, JavaType> result = strategy(msg);
      return result.getValue0();
    }

    private Pair<StatusType, JavaType> strategy(RestMessage msg)
        throws NoMappingModelFoundException, ServerErrorResponseException {

      JavaType clazz;
      StatusType status = Status.fromStatusCode(msg.getHttpStatusCode());

      if (msg.getHeaders().containsKey(HttpHeaders.CONTENT_TYPE)) {
        if (msg.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE)
            .equals(MediaType.APPLICATION_JSON)) {
          // ResponseWrapper
          ObjectMapper mapper = new ObjectMapper();
          clazz =
              mapper.getTypeFactory().constructParametricType(JsonRpcResponse.class, targetClass);
        } else {
          // SERVER ERROR
          throw new ServerErrorResponseException("SERVER_ERROR_RESPONSE", null, msg,
              status.getStatusCode());
        }
      } else {
        ObjectMapper mapper = new ObjectMapper();
        clazz = mapper.getTypeFactory().constructParametricType(JsonRpcResponse.class, targetClass);
      }

      return new Pair<StatusType, JavaType>(status, clazz);
    }
  }

}
