package com.indigo.zabbix.utils.beans;

import com.google.gson.annotations.SerializedName;

/** Created by jose on 8/02/17. */
public class DocDataType {

  public enum ServiceType {
    @SerializedName("eu.egi.cloud.vm-management.openstack")
    OPENSTACK,
    @SerializedName("eu.egi.cloud.storage-management.oneprovider")
    ONEPROVIDER,
    @SerializedName("eu.indigo-datacloud.marathon")
    MARATHON,
    @SerializedName("eu.indigo-datacloud.chronos")
    CHRONOS,
    @SerializedName("eu.egi.storage-element")
    STORAGE,
    @SerializedName("eu.indigo-datacloud.mesos")
    MESOS
  }

  public class PropertiesType {

    @SerializedName("gpu_support")
    private Boolean gpuSupport;

    public Boolean getGpuSupport() {
      return gpuSupport;
    }

    public void setGpuSupport(Boolean gpuSupport) {
      this.gpuSupport = gpuSupport;
    }
  }

  @SerializedName("service_type")
  ServiceType serviceType;

  String endpoint;

  @SerializedName("provider_id")
  String providerId;

  String type;

  PropertiesType properties;

  public ServiceType getServiceType() {
    return serviceType;
  }

  public void setServiceType(ServiceType serviceType) {
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

  public PropertiesType getProperties() {
    return properties;
  }

  public void setProperties(PropertiesType properties) {
    this.properties = properties;
  }
}
