package com.indigo.zabbix.utils.beans;

import java.util.HashMap;
import java.util.Map;

/** Created by jose on 11/16/16. */
public class AppOperation {

  public enum Operation {
    CLEAR,
    CREATE,
    RUN,
    DELETE
  }

  private Operation operation;
  private Boolean result;
  private Integer status;
  private Long responseTime;

  /**
   * Generic operation constructor.
   *
   * @param operation The operations executed.
   * @param result True if the operation was executed successfully.
   * @param status Status code for the operation.
   * @param responseTime Time it took to execute it.
   */
  public AppOperation(Operation operation, boolean result, int status, long responseTime) {
    this.operation = operation;
    this.result = result;
    this.status = status;
    this.responseTime = responseTime;
  }

  public Operation getOperation() {
    return operation;
  }

  public boolean isResult() {
    return result;
  }

  public int getStatus() {
    return status;
  }

  public long getResponseTime() {
    return responseTime;
  }

  /**
   * Get a zabbix metrics representation of this operation results.
   *
   * @return The metrics representation as a key-value pair.
   */
  public Map<String, String> getMetrics() {
    Map<String, String> metrics = new HashMap<>();

    metrics.put(operation.toString().toLowerCase() + ".result", result.toString());
    metrics.put(operation.toString().toLowerCase() + ".status", status.toString());
    metrics.put(operation.toString().toLowerCase() + ".responseTime", responseTime.toString());

    return metrics;
  }
}
