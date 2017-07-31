package org.indigo.openstackprobe.openstack;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class OpenstackZones.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"cloud-providers-zones"})
public class OpenStackZones {

  /** The cloud providers zones. */
  @JsonProperty("cloud-providers-zones")
  private List<CloudProviderZone> cloudProvidersZones = null;

  /** The additional properties. */
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  /**
   * Gets the cloud providers zones.
   *
   * @return the cloud providers zones
   */
  @JsonProperty("cloud-providers-zones")
  public List<CloudProviderZone> getCloudProvidersZones() {
    return cloudProvidersZones;
  }

  /**
   * Sets the cloud providers zones.
   *
   * @param cloudProvidersZones the new cloud providers zones
   */
  @JsonProperty("cloud-providers-zones")
  public void setCloudProvidersZones(List<CloudProviderZone> cloudProvidersZones) {
    this.cloudProvidersZones = cloudProvidersZones;
  }


}
