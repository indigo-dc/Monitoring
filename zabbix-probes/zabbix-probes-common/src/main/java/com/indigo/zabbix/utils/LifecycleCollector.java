package com.indigo.zabbix.utils;

import com.indigo.zabbix.utils.beans.AppOperation;

import java.util.HashMap;
import java.util.Map;

/** Created by jose on 11/17/16. */
public abstract class LifecycleCollector implements MetricsCollector {

  protected Map<AppOperation.Operation, AppOperation> result = new HashMap<>();

  protected void addOperation(AppOperation operation) {
    result.put(operation.getOperation(), operation);
  }

  protected Map<AppOperation.Operation, AppOperation> executeLifecycle() {

    AppOperation clear = clear();
    addOperation(clear);

    if (clear.isResult()) {

      AppOperation created = create();
      addOperation(created);

      if (created.isResult()) {

        AppOperation getAppResponse = retrieve();
        addOperation(getAppResponse);
        if (getAppResponse.isResult()) {

          AppOperation delete = delete();
          addOperation(delete);

        } else {
          addOperation(new AppOperation(AppOperation.Operation.DELETE, false, 503, 0));
        }

      } else {
        addOperation(new AppOperation(AppOperation.Operation.RUN, false, 503, 0));
        addOperation(new AppOperation(AppOperation.Operation.DELETE, false, 503, 0));
      }
    } else {
      addOperation(new AppOperation(AppOperation.Operation.CREATE, false, 503, 0));
      addOperation(new AppOperation(AppOperation.Operation.RUN, false, 503, 0));
      addOperation(new AppOperation(AppOperation.Operation.DELETE, false, 503, 0));
    }

    return result;
  }

  @Override
  public ZabbixMetrics getMetrics() {

    ZabbixMetrics result = new ZabbixMetrics();
    result.setHostName(getHostName());
    result.setHostGroup(getGroup());
    result.setServiceType(getServiceType());

    Map<String, String> metrics = new HashMap<>();

    Map<AppOperation.Operation, AppOperation> execution = executeLifecycle();

    execution.entrySet().forEach(entry -> metrics.putAll(entry.getValue().getMetrics()));

    result.setMetrics(metrics);
    return result;
  }

  protected abstract AppOperation clear();

  protected abstract AppOperation create();

  protected abstract AppOperation retrieve();

  protected abstract AppOperation delete();
}
