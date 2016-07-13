package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "maintenances", "hostid", "proxy_hostid", "host", "status", "disable_until",
    "error", "available", "errors_from", "lastaccess", "ipmi_authtype", "ipmi_privilege",
    "ipmi_username", "ipmi_password", "ipmi_disable_until", "ipmi_available", "snmp_disable_until",
    "snmp_available", "maintenanceid", "maintenance_status", "maintenance_type", "maintenance_from",
    "ipmi_errors_from", "snmp_errors_from", "ipmi_error", "snmp_error", "jmx_disable_until",
    "jmx_available", "jmx_errors_from", "jmx_error", "name", "flags", "templateid", "inventory" })
public class ZabbixMonitoredHostsResponse {

  @JsonProperty("maintenances")
  private List<Object> maintenances = new ArrayList<Object>();
  @JsonProperty("hostid")
  private String hostid;
  @JsonProperty("proxy_hostid")
  private String proxyHostid;
  @JsonProperty("host")
  private String host;
  @JsonProperty("status")
  private String status;
  @JsonProperty("disable_until")
  private String disableUntil;
  @JsonProperty("error")
  private String error;
  @JsonProperty("available")
  private String available;
  @JsonProperty("errors_from")
  private String errorsFrom;
  @JsonProperty("lastaccess")
  private String lastaccess;
  @JsonProperty("ipmi_authtype")
  private String ipmiAuthtype;
  @JsonProperty("ipmi_privilege")
  private String ipmiPrivilege;
  @JsonProperty("ipmi_username")
  private String ipmiUsername;
  @JsonProperty("ipmi_password")
  private String ipmiPassword;
  @JsonProperty("ipmi_disable_until")
  private String ipmiDisableUntil;
  @JsonProperty("ipmi_available")
  private String ipmiAvailable;
  @JsonProperty("snmp_disable_until")
  private String snmpDisableUntil;
  @JsonProperty("snmp_available")
  private String snmpAvailable;
  @JsonProperty("maintenanceid")
  private String maintenanceid;
  @JsonProperty("maintenance_status")
  private String maintenanceStatus;
  @JsonProperty("maintenance_type")
  private String maintenanceType;
  @JsonProperty("maintenance_from")
  private String maintenanceFrom;
  @JsonProperty("ipmi_errors_from")
  private String ipmiErrorsFrom;
  @JsonProperty("snmp_errors_from")
  private String snmpErrorsFrom;
  @JsonProperty("ipmi_error")
  private String ipmiError;
  @JsonProperty("snmp_error")
  private String snmpError;
  @JsonProperty("jmx_disable_until")
  private String jmxDisableUntil;
  @JsonProperty("jmx_available")
  private String jmxAvailable;
  @JsonProperty("jmx_errors_from")
  private String jmxErrorsFrom;
  @JsonProperty("jmx_error")
  private String jmxError;
  @JsonProperty("name")
  private String name;
  @JsonProperty("flags")
  private String flags;
  @JsonProperty("templateid")
  private String templateid;
  @JsonProperty("inventory")
  private Inventory inventory;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("maintenances")
  public List<Object> getMaintenances() {
    return maintenances;
  }

  @JsonProperty("maintenances")
  public void setMaintenances(List<Object> maintenances) {
    this.maintenances = maintenances;
  }

  @JsonProperty("hostid")
  public String getHostid() {
    return hostid;
  }

  @JsonProperty("hostid")
  public void setHostid(String hostid) {
    this.hostid = hostid;
  }

  @JsonProperty("proxy_hostid")
  public String getProxyHostid() {
    return proxyHostid;
  }

  @JsonProperty("proxy_hostid")
  public void setProxyHostid(String proxyHostid) {
    this.proxyHostid = proxyHostid;
  }

  @JsonProperty("host")
  public String getHost() {
    return host;
  }

  @JsonProperty("host")
  public void setHost(String host) {
    this.host = host;
  }

  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(String status) {
    this.status = status;
  }

  @JsonProperty("disable_until")
  public String getDisableUntil() {
    return disableUntil;
  }

  @JsonProperty("disable_until")
  public void setDisableUntil(String disableUntil) {
    this.disableUntil = disableUntil;
  }

  @JsonProperty("error")
  public String getError() {
    return error;
  }

  @JsonProperty("error")
  public void setError(String error) {
    this.error = error;
  }

  @JsonProperty("available")
  public String getAvailable() {
    return available;
  }

  @JsonProperty("available")
  public void setAvailable(String available) {
    this.available = available;
  }

  @JsonProperty("errors_from")
  public String getErrorsFrom() {
    return errorsFrom;
  }

  @JsonProperty("errors_from")
  public void setErrorsFrom(String errorsFrom) {
    this.errorsFrom = errorsFrom;
  }

  @JsonProperty("lastaccess")
  public String getLastaccess() {
    return lastaccess;
  }

  @JsonProperty("lastaccess")
  public void setLastaccess(String lastaccess) {
    this.lastaccess = lastaccess;
  }

  @JsonProperty("ipmi_authtype")
  public String getIpmiAuthtype() {
    return ipmiAuthtype;
  }

  @JsonProperty("ipmi_authtype")
  public void setIpmiAuthtype(String ipmiAuthtype) {
    this.ipmiAuthtype = ipmiAuthtype;
  }

  @JsonProperty("ipmi_privilege")
  public String getIpmiPrivilege() {
    return ipmiPrivilege;
  }

  @JsonProperty("ipmi_privilege")
  public void setIpmiPrivilege(String ipmiPrivilege) {
    this.ipmiPrivilege = ipmiPrivilege;
  }

  @JsonProperty("ipmi_username")
  public String getIpmiUsername() {
    return ipmiUsername;
  }

  @JsonProperty("ipmi_username")
  public void setIpmiUsername(String ipmiUsername) {
    this.ipmiUsername = ipmiUsername;
  }

  @JsonProperty("ipmi_password")
  public String getIpmiPassword() {
    return ipmiPassword;
  }

  @JsonProperty("ipmi_password")
  public void setIpmiPassword(String ipmiPassword) {
    this.ipmiPassword = ipmiPassword;
  }

  @JsonProperty("ipmi_disable_until")
  public String getIpmiDisableUntil() {
    return ipmiDisableUntil;
  }

  @JsonProperty("ipmi_disable_until")
  public void setIpmiDisableUntil(String ipmiDisableUntil) {
    this.ipmiDisableUntil = ipmiDisableUntil;
  }

  @JsonProperty("ipmi_available")
  public String getIpmiAvailable() {
    return ipmiAvailable;
  }

  @JsonProperty("ipmi_available")
  public void setIpmiAvailable(String ipmiAvailable) {
    this.ipmiAvailable = ipmiAvailable;
  }

  @JsonProperty("snmp_disable_until")
  public String getSnmpDisableUntil() {
    return snmpDisableUntil;
  }

  @JsonProperty("snmp_disable_until")
  public void setSnmpDisableUntil(String snmpDisableUntil) {
    this.snmpDisableUntil = snmpDisableUntil;
  }

  @JsonProperty("snmp_available")
  public String getSnmpAvailable() {
    return snmpAvailable;
  }

  @JsonProperty("snmp_available")
  public void setSnmpAvailable(String snmpAvailable) {
    this.snmpAvailable = snmpAvailable;
  }

  @JsonProperty("maintenanceid")
  public String getMaintenanceid() {
    return maintenanceid;
  }

  @JsonProperty("maintenanceid")
  public void setMaintenanceid(String maintenanceid) {
    this.maintenanceid = maintenanceid;
  }

  @JsonProperty("maintenance_status")
  public String getMaintenanceStatus() {
    return maintenanceStatus;
  }

  @JsonProperty("maintenance_status")
  public void setMaintenanceStatus(String maintenanceStatus) {
    this.maintenanceStatus = maintenanceStatus;
  }

  @JsonProperty("maintenance_type")
  public String getMaintenanceType() {
    return maintenanceType;
  }

  @JsonProperty("maintenance_type")
  public void setMaintenanceType(String maintenanceType) {
    this.maintenanceType = maintenanceType;
  }

  @JsonProperty("maintenance_from")
  public String getMaintenanceFrom() {
    return maintenanceFrom;
  }

  @JsonProperty("maintenance_from")
  public void setMaintenanceFrom(String maintenanceFrom) {
    this.maintenanceFrom = maintenanceFrom;
  }

  @JsonProperty("ipmi_errors_from")
  public String getIpmiErrorsFrom() {
    return ipmiErrorsFrom;
  }

  @JsonProperty("ipmi_errors_from")
  public void setIpmiErrorsFrom(String ipmiErrorsFrom) {
    this.ipmiErrorsFrom = ipmiErrorsFrom;
  }

  @JsonProperty("snmp_errors_from")
  public String getSnmpErrorsFrom() {
    return snmpErrorsFrom;
  }

  @JsonProperty("snmp_errors_from")
  public void setSnmpErrorsFrom(String snmpErrorsFrom) {
    this.snmpErrorsFrom = snmpErrorsFrom;
  }

  @JsonProperty("ipmi_error")
  public String getIpmiError() {
    return ipmiError;
  }

  @JsonProperty("ipmi_error")
  public void setIpmiError(String ipmiError) {
    this.ipmiError = ipmiError;
  }

  @JsonProperty("snmp_error")
  public String getSnmpError() {
    return snmpError;
  }

  @JsonProperty("snmp_error")
  public void setSnmpError(String snmpError) {
    this.snmpError = snmpError;
  }

  @JsonProperty("jmx_disable_until")
  public String getJmxDisableUntil() {
    return jmxDisableUntil;
  }

  @JsonProperty("jmx_disable_until")
  public void setJmxDisableUntil(String jmxDisableUntil) {
    this.jmxDisableUntil = jmxDisableUntil;
  }

  @JsonProperty("jmx_available")
  public String getJmxAvailable() {
    return jmxAvailable;
  }

  @JsonProperty("jmx_available")
  public void setJmxAvailable(String jmxAvailable) {
    this.jmxAvailable = jmxAvailable;
  }

  @JsonProperty("jmx_errors_from")
  public String getJmxErrorsFrom() {
    return jmxErrorsFrom;
  }

  @JsonProperty("jmx_errors_from")
  public void setJmxErrorsFrom(String jmxErrorsFrom) {
    this.jmxErrorsFrom = jmxErrorsFrom;
  }

  @JsonProperty("jmx_error")
  public String getJmxError() {
    return jmxError;
  }

  @JsonProperty("jmx_error")
  public void setJmxError(String jmxError) {
    this.jmxError = jmxError;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("flags")
  public String getFlags() {
    return flags;
  }

  @JsonProperty("flags")
  public void setFlags(String flags) {
    this.flags = flags;
  }

  @JsonProperty("templateid")
  public String getTemplateid() {
    return templateid;
  }

  @JsonProperty("templateid")
  public void setTemplateid(String templateid) {
    this.templateid = templateid;
  }

  @JsonProperty("inventory")
  public Inventory getInventory() {
    return inventory;
  }

  @JsonProperty("inventory")
  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(maintenances).append(hostid).append(proxyHostid)
        .append(host).append(status).append(disableUntil).append(error).append(available)
        .append(errorsFrom).append(lastaccess).append(ipmiAuthtype).append(ipmiPrivilege)
        .append(ipmiUsername).append(ipmiPassword).append(ipmiDisableUntil).append(ipmiAvailable)
        .append(snmpDisableUntil).append(snmpAvailable).append(maintenanceid)
        .append(maintenanceStatus).append(maintenanceType).append(maintenanceFrom)
        .append(ipmiErrorsFrom).append(snmpErrorsFrom).append(ipmiError).append(snmpError)
        .append(jmxDisableUntil).append(jmxAvailable).append(jmxErrorsFrom).append(jmxError)
        .append(name).append(flags).append(templateid).append(inventory)
        .append(additionalProperties).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof ZabbixMonitoredHostsResponse) == false) {
      return false;
    }
    ZabbixMonitoredHostsResponse rhs = ((ZabbixMonitoredHostsResponse) other);
    return new EqualsBuilder().append(maintenances, rhs.maintenances).append(hostid, rhs.hostid)
        .append(proxyHostid, rhs.proxyHostid).append(host, rhs.host).append(status, rhs.status)
        .append(disableUntil, rhs.disableUntil).append(error, rhs.error)
        .append(available, rhs.available).append(errorsFrom, rhs.errorsFrom)
        .append(lastaccess, rhs.lastaccess).append(ipmiAuthtype, rhs.ipmiAuthtype)
        .append(ipmiPrivilege, rhs.ipmiPrivilege).append(ipmiUsername, rhs.ipmiUsername)
        .append(ipmiPassword, rhs.ipmiPassword).append(ipmiDisableUntil, rhs.ipmiDisableUntil)
        .append(ipmiAvailable, rhs.ipmiAvailable).append(snmpDisableUntil, rhs.snmpDisableUntil)
        .append(snmpAvailable, rhs.snmpAvailable).append(maintenanceid, rhs.maintenanceid)
        .append(maintenanceStatus, rhs.maintenanceStatus)
        .append(maintenanceType, rhs.maintenanceType).append(maintenanceFrom, rhs.maintenanceFrom)
        .append(ipmiErrorsFrom, rhs.ipmiErrorsFrom).append(snmpErrorsFrom, rhs.snmpErrorsFrom)
        .append(ipmiError, rhs.ipmiError).append(snmpError, rhs.snmpError)
        .append(jmxDisableUntil, rhs.jmxDisableUntil).append(jmxAvailable, rhs.jmxAvailable)
        .append(jmxErrorsFrom, rhs.jmxErrorsFrom).append(jmxError, rhs.jmxError)
        .append(name, rhs.name).append(flags, rhs.flags).append(templateid, rhs.templateid)
        .append(inventory, rhs.inventory).append(additionalProperties, rhs.additionalProperties)
        .isEquals();
  }

}