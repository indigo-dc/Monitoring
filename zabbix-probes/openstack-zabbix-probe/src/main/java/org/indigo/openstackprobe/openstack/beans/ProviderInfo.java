package org.indigo.openstackprobe.openstack.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jose on 8/02/17.
 */
public class ProviderInfo {

  @SerializedName("provider_id")
  String providerId;
  String type;

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