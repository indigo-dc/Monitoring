package org.indigo.occiprobe.openstack.beans;

/**
 * Created by jose on 8/02/17.
 */
public class ProviderInfo {

  String provider_id;
  String type;

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
