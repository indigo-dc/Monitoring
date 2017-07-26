package com.indigo.zabbix.utils.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jose on 8/02/17.
 */
public class DocDataType {

  @SerializedName("service_type")
  String serviceType;
  String endpoint;
  @SerializedName("provider_id")
  String providerId;
  String type;

  public String getServiceType() {
    return serviceType;
  }

  public void setServiceType(String serviceType) {
    this.serviceType = serviceType;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getProviderId() {
    return providerId;
  }

  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}