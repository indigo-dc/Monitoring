package com.indigo.zabbix.utils;

import com.indigo.zabbix.utils.beans.AppOperation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jose on 11/17/16.
 */
public abstract class LifecycleCollector implements MetricsCollector {

  @Override
  public ZabbixMetrics getMetrics() {
    String hostName = getHostName();

    ZabbixMetrics result = new ZabbixMetrics();
    result.setHostName(hostName);

    Map<String, String> metrics = new HashMap<>();

    AppOperation clear = clear();
    metrics.putAll(clear.getMetrics());

    if (clear.isResult()) {

      AppOperation created = create();
      metrics.putAll(created.getMetrics());

      if (created.isResult()) {

        AppOperation getAppResponse = retrieve();
        metrics.putAll(getAppResponse.getMetrics());
        if (getAppResponse.isResult()) {

          AppOperation delete = delete();
          metrics.putAll(delete.getMetrics());

        } else {
          metrics.putAll(new AppOperation(AppOperation.Operation.DELETE,false,503,0).getMetrics());
        }

      } else {
        metrics.putAll(new AppOperation(AppOperation.Operation.RUN,false,503,0).getMetrics());
        metrics.putAll(new AppOperation(AppOperation.Operation.DELETE,false,503,0).getMetrics());
      }
    } else {
      metrics.putAll(new AppOperation(AppOperation.Operation.CREATE,false,503,0).getMetrics());
      metrics.putAll(new AppOperation(AppOperation.Operation.RUN,false,503,0).getMetrics());
      metrics.putAll(new AppOperation(AppOperation.Operation.DELETE,false,503,0).getMetrics());
    }

    result.setMetrics(metrics);
    return result;

  }

  protected abstract String getHostName();

  protected abstract AppOperation clear();

  protected abstract AppOperation create();

  protected abstract AppOperation retrieve();

  protected abstract AppOperation delete();


}