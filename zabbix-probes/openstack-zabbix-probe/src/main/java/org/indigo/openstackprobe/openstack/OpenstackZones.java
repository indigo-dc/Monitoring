package org.indigo.openstackprobe.openstack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class OpenstackZones.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "cloud-providers-zones" })
public class OpenstackZones {

	/** The cloud providers zones. */
	@JsonProperty("cloud-providers-zones")
	private List<CloudProvidersZone> cloudProvidersZones = null;
	
	/** The additional properties. */
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * Gets the cloud providers zones.
	 *
	 * @return the cloud providers zones
	 */
	@JsonProperty("cloud-providers-zones")
	public List<CloudProvidersZone> getCloudProvidersZones() {
		return cloudProvidersZones;
	}

	/**
	 * Sets the cloud providers zones.
	 *
	 * @param cloudProvidersZones the new cloud providers zones
	 */
	@JsonProperty("cloud-providers-zones")
	public void setCloudProvidersZones(List<CloudProvidersZone> cloudProvidersZones) {
		this.cloudProvidersZones = cloudProvidersZones;
	}


}