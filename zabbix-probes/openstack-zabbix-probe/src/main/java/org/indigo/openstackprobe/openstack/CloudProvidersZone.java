package org.indigo.openstackprobe.openstack;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

// TODO: Auto-generated Javadoc
/**
 * The Class CloudProvidersZone.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "username", "password", "name", "tenant" })
public class CloudProvidersZone {

	/** The username. */
	@JsonProperty("username")
	private String username;
	
	/** The password. */
	@JsonProperty("password")
	private String password;
	
	/** The name. */
	@JsonProperty("name")
	private String name;
	
	/** The tenant. */
	@JsonProperty("tenant")
	private String tenant;
	
	/** The additional properties. */
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	@JsonProperty("username")
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	@JsonProperty("username")
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	@JsonProperty("password")
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	@JsonProperty("password")
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the tenant.
	 *
	 * @return the tenant
	 */
	@JsonProperty("tenant")
	public String getTenant() {
		return tenant;
	}

	/**
	 * Sets the tenant.
	 *
	 * @param tenant the new tenant
	 */
	@JsonProperty("tenant")
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
}
