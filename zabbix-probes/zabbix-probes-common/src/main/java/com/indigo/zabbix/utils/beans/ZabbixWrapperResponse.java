package com.indigo.zabbix.utils.beans;

public class ZabbixWrapperResponse<T> {

  public static class Meta {
    private int status;

    public Meta() {}

    public Meta(int status) {
      this.status = status;
    }

    public int getStatus() {
      return status;
    }

    public void setStatus(int status) {
      this.status = status;
    }
  }

  private Meta meta;

  private T result;

  public ZabbixWrapperResponse() {}

  public ZabbixWrapperResponse(int code, T body) {
    this.meta = new Meta(code);
    this.result = body;
  }

  public Meta getMeta() {
    return meta;
  }

  public void setMeta(Meta meta) {
    this.meta = meta;
  }

  public T getResult() {
    return result;
  }

  public void setResult(T result) {
    this.result = result;
  }
}
