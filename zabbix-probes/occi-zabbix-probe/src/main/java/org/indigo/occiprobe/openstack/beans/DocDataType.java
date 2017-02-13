package org.indigo.occiprobe.openstack.beans;

/**
 * Created by jose on 8/02/17.
 */
public class DocDataType {

  String service_type;
  String endpoint;
  String provider_id;
  String type;

  public String getService_type() {
    return service_type;
  }

  public void setService_type(String service_type) {
    this.service_type = service_type;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getProvider_id() {
    return provider_id;
  }

  public void setProvider_id(String provider_id) {
    this.provider_id = provider_id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
