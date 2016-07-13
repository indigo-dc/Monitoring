package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "itemid", "type", "snmp_community", "snmp_oid", "hostid", "name", "key_",
    "delay", "history", "trends", "lastvalue", "lastclock", "prevvalue", "status", "value_type",
    "trapper_hosts", "units", "multiplier", "delta", "prevorgvalue", "snmpv3_securityname",
    "snmpv3_securitylevel", "snmpv3_authpassphrase", "snmpv3_privpassphrase", "formula", "error",
    "lastlogsize", "logtimefmt", "templateid", "valuemapid", "delay_flex", "params", "ipmi_sensor",
    "data_type", "authtype", "username", "password", "publickey", "privatekey", "mtime", "lastns",
    "flags", "filter", "interfaceid", "port", "description", "inventory_link", "lifetime",

    // variables of interfaces
    "interfaceid", "ip", "dns", "port",

    // variables of triggers
    "triggerid", "expression", "description", "status", "value", "priority", "lastchange", "error",
    "type", "state",

    // Useful for history
    "clock", "ns",

    // Useful for Events
    "objectid", "acknowledged", "triggers", "triggerids",

    // USEFUL for TREND
    "valueAvg", "valueMin", "valueMax" })
public class ZabbixItemResponse {

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
  @JsonProperty("lastvalue")
  private String lastvalue;
  @JsonProperty("lastclock")
  private String lastclock;
  @JsonProperty("prevvalue")
  private String prevvalue;
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
  @JsonProperty("prevorgvalue")
  private String prevorgvalue;
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
  @JsonProperty("lastns")
  private String lastns;
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

  // variables for interfaces
  @JsonProperty("ip")
  private String ip;
  @JsonProperty("dns")
  private String dns;

  // useful for trigger
  @JsonProperty("triggerid")
  private String triggerid;
  @JsonProperty("expression")
  private String expression;
  // Useful for history as well
  @JsonProperty("value")
  private String value;
  @JsonProperty("priority")
  private String priority;
  @JsonProperty("lastchange")
  private String lastchange;
  @JsonProperty("state")
  private String state;

  // useful for History
  @JsonProperty("clock")
  private String clock;
  @JsonProperty("ns")
  private String ns;

  // Useful for Event
  @JsonProperty("objectid")
  private String objectid;
  @JsonProperty("acknowledged")
  private String acknowledged;
  @JsonProperty("triggers")
  private List<Trigger> triggers = new ArrayList<Trigger>();
  @JsonProperty("triggerids")
  private String triggerids;

  // USEFUL for TREND
  @JsonProperty("valueAvg")
  private String valueAvg;
  @JsonProperty("valueMax")
  private String valueMax;
  @JsonProperty("valueMin")
  private String valueMin;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("triggers")
  public List<Trigger> getTriggers() {
    return triggers;
  }

  @JsonProperty("triggers")
  public void setTriggers(List<Trigger> triggers) {
    this.triggers = triggers;
  }

  @JsonProperty("triggerid")
  public String getTriggerid() {
    return triggerid;
  }

  @JsonProperty("triggerid")
  public void setTriggerid(String triggerid) {
    this.triggerid = triggerid;
  }

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

  @JsonProperty("lastvalue")
  public String getLastvalue() {
    return lastvalue;
  }

  @JsonProperty("lastvalue")
  public void setLastvalue(String lastvalue) {
    this.lastvalue = lastvalue;
  }

  @JsonProperty("lastclock")
  public String getLastclock() {
    return lastclock;
  }

  @JsonProperty("lastclock")
  public void setLastclock(String lastclock) {
    this.lastclock = lastclock;
  }

  @JsonProperty("prevvalue")
  public String getPrevvalue() {
    return prevvalue;
  }

  @JsonProperty("prevvalue")
  public void setPrevvalue(String prevvalue) {
    this.prevvalue = prevvalue;
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

  @JsonProperty("prevorgvalue")
  public String getPrevorgvalue() {
    return prevorgvalue;
  }

  @JsonProperty("prevorgvalue")
  public void setPrevorgvalue(String prevorgvalue) {
    this.prevorgvalue = prevorgvalue;
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

  @JsonProperty("lastns")
  public String getLastns() {
    return lastns;
  }

  @JsonProperty("lastns")
  public void setLastns(String lastns) {
    this.lastns = lastns;
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

  // useful for interfaces

  @JsonProperty("ip")
  public String getIp() {
    return ip;
  }

  @JsonProperty("ip")
  public void setIp(String ip) {
    this.ip = ip;
  }

  @JsonProperty("dns")
  public String getDns() {
    return dns;
  }

  @JsonProperty("dns")
  public void setDns(String dns) {
    this.dns = dns;
  }

  @JsonProperty("expression")
  public String getExpression() {
    return expression;
  }

  @JsonProperty("expression")
  public void setExpression(String expression) {
    this.expression = expression;
  }

  @JsonProperty("value")
  public String getValue() {
    return value;
  }

  @JsonProperty("value")
  public void setValue(String value) {
    this.value = value;
  }

  @JsonProperty("priority")
  public String getPriority() {
    return priority;
  }

  @JsonProperty("priority")
  public void setPriority(String priority) {
    this.priority = priority;
  }

  @JsonProperty("lastchange")
  public String getLastchange() {
    return lastchange;
  }

  @JsonProperty("lastchange")
  public void setLastchange(String lastchange) {
    this.lastchange = lastchange;
  }

  @JsonProperty("state")
  public String getState() {
    return state;
  }

  @JsonProperty("state")
  public void setState(String state) {
    this.state = state;
  }

  // Useful for Graph

  @JsonProperty("clock")
  public String getClock() {
    return clock;
  }

  @JsonProperty("clock")
  public void setClock(String clock) {
    this.clock = clock;
  }

  @JsonProperty("ns")
  public String getNs() {
    return ns;
  }

  @JsonProperty("ns")
  public void setNs(String ns) {
    this.ns = ns;
  }

  @JsonProperty("objectid")
  public String getObjectid() {
    return objectid;
  }

  @JsonProperty("objectid")
  public void setObjectid(String objectid) {
    this.objectid = objectid;
  }

  @JsonProperty("acknowledged")
  public String getAcknowledged() {
    return acknowledged;
  }

  @JsonProperty("acknowledged")
  public void setAcknowledged(String acknowledged) {
    this.acknowledged = acknowledged;
  }

  @JsonProperty("valueAvg")
  public String getValue_avg() {
    return valueAvg;
  }

  @JsonProperty("valueAvg")
  public void setValue_avg(String valueAvg) {
    this.valueAvg = valueAvg;
  }

  @JsonProperty("valueMax")
  public String getValue_max() {
    return valueMax;
  }

  @JsonProperty("valueMax")
  public void setValue_max(String valueMax) {
    this.valueMax = valueMax;
  }

  @JsonProperty("valueMin")
  public String getValue_min() {
    return valueMin;
  }

  @JsonProperty("valueMin")
  public void setValue_min(String valueMin) {
    this.valueMin = valueMin;
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
