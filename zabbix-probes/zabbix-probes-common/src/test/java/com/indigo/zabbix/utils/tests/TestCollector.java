package com.indigo.zabbix.utils.tests;

import com.indigo.zabbix.utils.LifecycleCollector;
import com.indigo.zabbix.utils.beans.AppOperation;

/**
 * Created by jose on 9/12/16.
 */
public class TestCollector extends LifecycleCollector {

  private String hostname;

  private boolean clearResult;
  private boolean createResult;
  private boolean retrieveResult;
  private boolean deleteResult;

  public TestCollector(String hostname, boolean clearResult, boolean createResult,
                       boolean retrieveResult, boolean deleteResult) {

    this.hostname = hostname;
    this.clearResult = clearResult;
    this.createResult = createResult;
    this.retrieveResult = retrieveResult;
    this.deleteResult = deleteResult;
  }

  @Override
  protected String getHostName() {
    return this.hostname;
  }

  @Override
  protected AppOperation clear() {
    return new AppOperation(
        AppOperation.Operation.CLEAR,
        clearResult,
        getCode(clearResult),
        100
    );
  }

  @Override
  protected AppOperation create() {
    return new AppOperation(
        AppOperation.Operation.CREATE,
        createResult,
        getCode(createResult),
        100
    );
  }

  @Override
  protected AppOperation retrieve() {
    return new AppOperation(
        AppOperation.Operation.RUN,
        retrieveResult,
        getCode(retrieveResult),
        100
    );
  }

  @Override
  protected AppOperation delete() {
    return new AppOperation(
        AppOperation.Operation.DELETE,
        deleteResult,
        getCode(deleteResult),
        100
    );
  }

  private int getCode(boolean result) {
    if (result) {
      return 200;
    } else {
      return 500;
    }
  }
}
