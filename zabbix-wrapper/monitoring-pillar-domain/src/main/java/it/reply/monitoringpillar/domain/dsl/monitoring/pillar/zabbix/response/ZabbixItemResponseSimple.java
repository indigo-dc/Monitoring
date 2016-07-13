package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "itemid", "type", "snmp_community", "snmp_oid", "hostid", "name", "key_",
    "delay", "history", "trends", "status", "value_type", "trapper_hosts", "units", "multiplier",
    "delta", "snmpv3_securityname", "snmpv3_securitylevel", "snmpv3_authpassphrase",
    "snmpv3_privpassphrase", "formula", "error", "lastlogsize", "logtimefmt", "templateid",
    "valuemapid", "delay_flex", "params", "ipmi_sensor", "data_type", "authtype", "username",
    "password", "publickey", "privatekey", "mtime", "flags", "filter", "interfaceid", "port",
    "description", "inventory_link", "lifetime", "snmpv3_authprotocol", "snmpv3_privprotocol",
    "state", "snmpv3_contextname", "lastclock", "lastns", "lastvalue", "prevvalue" })
public class ZabbixItemResponseSimple {

  @JsonProperty("itemid")
  private String itemid;
  @JsonProperty("type")
  private String type;
  @JsonProperty("snmp_community")
  private String snmpCommunity;
  @JsonProperty("snmp_oid")
  private String snmpOid;
  @JsonProperty("hostid")
  private String hostid;
  @JsonProperty("name")
  private String name;
  @JsonProperty("key_")
  private String key;
  @JsonProperty("delay")
  private String delay;
  @JsonProperty("history")
  private String history;
  @JsonProperty("trends")
  private String trends;
  @JsonProperty("status")
  private String status;
  @JsonProperty("value_type")
  private String valueType;
  @JsonProperty("trapper_hosts")
  private String trapperHosts;
  @JsonProperty("units")
  private String units;
  @JsonProperty("multiplier")
  private String multiplier;
  @JsonProperty("delta")
  private String delta;
  @JsonProperty("snmpv3_securityname")
  private String snmpv3Securityname;
  @JsonProperty("snmpv3_securitylevel")
  private String snmpv3Securitylevel;
  @JsonProperty("snmpv3_authpassphrase")
  private String snmpv3Authpassphrase;
  @JsonProperty("snmpv3_privpassphrase")
  private String snmpv3Privpassphrase;
  @JsonProperty("formula")
  private String formula;
  @JsonProperty("error")
  private String error;
  @JsonProperty("lastlogsize")
  private String lastlogsize;
  @JsonProperty("logtimefmt")
  private String logtimefmt;
  @JsonProperty("templateid")
  private String templateid;
  @JsonProperty("valuemapid")
  private String valuemapid;
  @JsonProperty("delay_flex")
  private String delayFlex;
  @JsonProperty("params")
  private String params;
  @JsonProperty("ipmi_sensor")
  private String ipmiSensor;
  @JsonProperty("data_type")
  private String dataType;
  @JsonProperty("authtype")
  private String authtype;
  @JsonProperty("username")
  private String username;
  @JsonProperty("password")
  private String password;
  @JsonProperty("publickey")
  private String publickey;
  @JsonProperty("privatekey")
  private String privatekey;
  @JsonProperty("mtime")
  private String mtime;
  @JsonProperty("flags")
  private String flags;
  @JsonProperty("filter")
  private String filter;
  @JsonProperty("interfaceid")
  private String interfaceid;
  @JsonProperty("port")
  private String port;
  @JsonProperty("description")
  private String description;
  @JsonProperty("inventory_link")
  private String inventoryLink;
  @JsonProperty("lifetime")
  private String lifetime;
  @JsonProperty("snmpv3_authprotocol")
  private String snmpv3Authprotocol;
  @JsonProperty("snmpv3_privprotocol")
  private String snmpv3Privprotocol;
  @JsonProperty("state")
  private String state;
  @JsonProperty("snmpv3_contextname")
  private String snmpv3Contextname;
  @JsonProperty("lastclock")
  private String lastclock;
  @JsonProperty("lastns")
  private String lastns;
  @JsonProperty("lastvalue")
  private String lastvalue;
  @JsonProperty("prevvalue")
  private String prevvalue;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("itemid")
  public String getItemid() {
    return itemid;
  }

  @JsonProperty("itemid")
  public void setItemid(String itemid) {
    this.itemid = itemid;
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(String type) {
    this.type = type;
  }

  @JsonProperty("snmp_community")
  public String getSnmpCommunity() {
    return snmpCommunity;
  }

  @JsonProperty("snmp_community")
  public void setSnmpCommunity(String snmpCommunity) {
    this.snmpCommunity = snmpCommunity;
  }

  @JsonProperty("snmp_oid")
  public String getSnmpOid() {
    return snmpOid;
  }

  @JsonProperty("snmp_oid")
  public void setSnmpOid(String snmpOid) {
    this.snmpOid = snmpOid;
  }

  @JsonProperty("hostid")
  public String getHostid() {
    return hostid;
  }

  @JsonProperty("hostid")
  public void setHostid(String hostid) {
    this.hostid = hostid;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("key_")
  public String getKey() {
    return key;
  }

  @JsonProperty("key_")
  public void setKey(String key) {
    this.key = key;
  }

  @JsonProperty("delay")
  public String getDelay() {
    return delay;
  }

  @JsonProperty("delay")
  public void setDelay(String delay) {
    this.delay = delay;
  }

  @JsonProperty("history")
  public String getHistory() {
    return history;
  }

  @JsonProperty("history")
  public void setHistory(String history) {
    this.history = history;
  }

  @JsonProperty("trends")
  public String getTrends() {
    return trends;
  }

  @JsonProperty("trends")
  public void setTrends(String trends) {
    this.trends = trends;
  }

  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(String status) {
    this.status = status;
  }

  @JsonProperty("value_type")
  public String getValueType() {
    return valueType;
  }

  @JsonProperty("value_type")
  public void setValueType(String valueType) {
    this.valueType = valueType;
  }

  @JsonProperty("trapper_hosts")
  public String getTrapperHosts() {
    return trapperHosts;
  }

  @JsonProperty("trapper_hosts")
  public void setTrapperHosts(String trapperHosts) {
    this.trapperHosts = trapperHosts;
  }

  @JsonProperty("units")
  public String getUnits() {
    return units;
  }

  @JsonProperty("units")
  public void setUnits(String units) {
    this.units = units;
  }

  @JsonProperty("multiplier")
  public String getMultiplier() {
    return multiplier;
  }

  @JsonProperty("multiplier")
  public void setMultiplier(String multiplier) {
    this.multiplier = multiplier;
  }

  @JsonProperty("delta")
  public String getDelta() {
    return delta;
  }

  @JsonProperty("delta")
  public void setDelta(String delta) {
    this.delta = delta;
  }

  @JsonProperty("snmpv3_securityname")
  public String getSnmpv3Securityname() {
    return snmpv3Securityname;
  }

  @JsonProperty("snmpv3_securityname")
  public void setSnmpv3Securityname(String snmpv3Securityname) {
    this.snmpv3Securityname = snmpv3Securityname;
  }

  @JsonProperty("snmpv3_securitylevel")
  public String getSnmpv3Securitylevel() {
    return snmpv3Securitylevel;
  }

  @JsonProperty("snmpv3_securitylevel")
  public void setSnmpv3Securitylevel(String snmpv3Securitylevel) {
    this.snmpv3Securitylevel = snmpv3Securitylevel;
  }

  @JsonProperty("snmpv3_authpassphrase")
  public String getSnmpv3Authpassphrase() {
    return snmpv3Authpassphrase;
  }

  @JsonProperty("snmpv3_authpassphrase")
  public void setSnmpv3Authpassphrase(String snmpv3Authpassphrase) {
    this.snmpv3Authpassphrase = snmpv3Authpassphrase;
  }

  @JsonProperty("snmpv3_privpassphrase")
  public String getSnmpv3Privpassphrase() {
    return snmpv3Privpassphrase;
  }

  @JsonProperty("snmpv3_privpassphrase")
  public void setSnmpv3Privpassphrase(String snmpv3Privpassphrase) {
    this.snmpv3Privpassphrase = snmpv3Privpassphrase;
  }

  @JsonProperty("formula")
  public String getFormula() {
    return formula;
  }

  @JsonProperty("formula")
  public void setFormula(String formula) {
    this.formula = formula;
  }

  @JsonProperty("error")
  public String getError() {
    return error;
  }

  @JsonProperty("error")
  public void setError(String error) {
    this.error = error;
  }

  @JsonProperty("lastlogsize")
  public String getLastlogsize() {
    return lastlogsize;
  }

  @JsonProperty("lastlogsize")
  public void setLastlogsize(String lastlogsize) {
    this.lastlogsize = lastlogsize;
  }

  @JsonProperty("logtimefmt")
  public String getLogtimefmt() {
    return logtimefmt;
  }

  @JsonProperty("logtimefmt")
  public void setLogtimefmt(String logtimefmt) {
    this.logtimefmt = logtimefmt;
  }

  @JsonProperty("templateid")
  public String getTemplateid() {
    return templateid;
  }

  @JsonProperty("templateid")
  public void setTemplateid(String templateid) {
    this.templateid = templateid;
  }

  @JsonProperty("valuemapid")
  public String getValuemapid() {
    return valuemapid;
  }

  @JsonProperty("valuemapid")
  public void setValuemapid(String valuemapid) {
    this.valuemapid = valuemapid;
  }

  @JsonProperty("delay_flex")
  public String getDelayFlex() {
    return delayFlex;
  }

  @JsonProperty("delay_flex")
  public void setDelayFlex(String delayFlex) {
    this.delayFlex = delayFlex;
  }

  @JsonProperty("params")
  public String getParams() {
    return params;
  }

  @JsonProperty("params")
  public void setParams(String params) {
    this.params = params;
  }

  @JsonProperty("ipmi_sensor")
  public String getIpmiSensor() {
    return ipmiSensor;
  }

  @JsonProperty("ipmi_sensor")
  public void setIpmiSensor(String ipmiSensor) {
    this.ipmiSensor = ipmiSensor;
  }

  @JsonProperty("data_type")
  public String getDataType() {
    return dataType;
  }

  @JsonProperty("data_type")
  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  @JsonProperty("authtype")
  public String getAuthtype() {
    return authtype;
  }

  @JsonProperty("authtype")
  public void setAuthtype(String authtype) {
    this.authtype = authtype;
  }

  @JsonProperty("username")
  public String getUsername() {
    return username;
  }

  @JsonProperty("username")
  public void setUsername(String username) {
    this.username = username;
  }

  @JsonProperty("password")
  public String getPassword() {
    return password;
  }

  @JsonProperty("password")
  public void setPassword(String password) {
    this.password = password;
  }

  @JsonProperty("publickey")
  public String getPublickey() {
    return publickey;
  }

  @JsonProperty("publickey")
  public void setPublickey(String publickey) {
    this.publickey = publickey;
  }

  @JsonProperty("privatekey")
  public String getPrivatekey() {
    return privatekey;
  }

  @JsonProperty("privatekey")
  public void setPrivatekey(String privatekey) {
    this.privatekey = privatekey;
  }

  @JsonProperty("mtime")
  public String getMtime() {
    return mtime;
  }

  @JsonProperty("mtime")
  public void setMtime(String mtime) {
    this.mtime = mtime;
  }

  @JsonProperty("flags")
  public String getFlags() {
    return flags;
  }

  @JsonProperty("flags")
  public void setFlags(String flags) {
    this.flags = flags;
  }

  @JsonProperty("filter")
  public String getFilter() {
    return filter;
  }

  @JsonProperty("filter")
  public void setFilter(String filter) {
    this.filter = filter;
  }

  @JsonProperty("interfaceid")
  public String getInterfaceid() {
    return interfaceid;
  }

  @JsonProperty("interfaceid")
  public void setInterfaceid(String interfaceid) {
    this.interfaceid = interfaceid;
  }

  @JsonProperty("port")
  public String getPort() {
    return port;
  }

  @JsonProperty("port")
  public void setPort(String port) {
    this.port = port;
  }

  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  @JsonProperty("inventory_link")
  public String getInventoryLink() {
    return inventoryLink;
  }

  @JsonProperty("inventory_link")
  public void setInventoryLink(String inventoryLink) {
    this.inventoryLink = inventoryLink;
  }

  @JsonProperty("lifetime")
  public String getLifetime() {
    return lifetime;
  }

  @JsonProperty("lifetime")
  public void setLifetime(String lifetime) {
    this.lifetime = lifetime;
  }

  @JsonProperty("snmpv3_authprotocol")
  public String getSnmpv3Authprotocol() {
    return snmpv3Authprotocol;
  }

  @JsonProperty("snmpv3_authprotocol")
  public void setSnmpv3Authprotocol(String snmpv3Authprotocol) {
    this.snmpv3Authprotocol = snmpv3Authprotocol;
  }

  @JsonProperty("snmpv3_privprotocol")
  public String getSnmpv3Privprotocol() {
    return snmpv3Privprotocol;
  }

  @JsonProperty("snmpv3_privprotocol")
  public void setSnmpv3Privprotocol(String snmpv3Privprotocol) {
    this.snmpv3Privprotocol = snmpv3Privprotocol;
  }

  @JsonProperty("state")
  public String getState() {
    return state;
  }

  @JsonProperty("state")
  public void setState(String state) {
    this.state = state;
  }

  @JsonProperty("snmpv3_contextname")
  public String getSnmpv3Contextname() {
    return snmpv3Contextname;
  }

  @JsonProperty("snmpv3_contextname")
  public void setSnmpv3Contextname(String snmpv3Contextname) {
    this.snmpv3Contextname = snmpv3Contextname;
  }

  @JsonProperty("lastclock")
  public String getLastclock() {
    return lastclock;
  }

  @JsonProperty("lastclock")
  public void setLastclock(String lastclock) {
    this.lastclock = lastclock;
  }

  @JsonProperty("lastns")
  public String getLastns() {
    return lastns;
  }

  @JsonProperty("lastns")
  public void setLastns(String lastns) {
    this.lastns = lastns;
  }

  @JsonProperty("lastvalue")
  public String getLastvalue() {
    return lastvalue;
  }

  @JsonProperty("lastvalue")
  public void setLastvalue(String lastvalue) {
    this.lastvalue = lastvalue;
  }

  @JsonProperty("prevvalue")
  public String getPrevvalue() {
    return prevvalue;
  }

  @JsonProperty("prevvalue")
  public void setPrevvalue(String prevvalue) {
    this.prevvalue = prevvalue;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

}
