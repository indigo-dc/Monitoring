package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "userid", "alias", "name", "surname", "url", "autologin", "autologout", "lang",
    "refresh", "type", "theme", "attempt_failed", "attempt_ip", "attempt_clock", "rows_per_page",
    "usrgrps", "mediatypes" })

public class ZabbixGetUserResponse {

  @JsonProperty("userid")
  private String userid;
  @JsonProperty("alias")
  private String alias;
  @JsonProperty("name")
  private String name;
  @JsonProperty("surname")
  private String surname;
  @JsonProperty("url")
  private String url;
  @JsonProperty("autologin")
  private String autologin;
  @JsonProperty("autologout")
  private String autologout;
  @JsonProperty("lang")
  private String lang;
  @JsonProperty("refresh")
  private String refresh;
  @JsonProperty("type")
  private String type;
  @JsonProperty("theme")
  private String theme;
  @JsonProperty("attempt_failed")
  private String attemptFailed;
  @JsonProperty("attempt_ip")
  private String attemptIp;
  @JsonProperty("attempt_clock")
  private String attemptClock;
  @JsonProperty("rows_per_page")
  private String rowsPerPage;
  @JsonProperty("usrgrps")
  private List<Usrgrp> usrgrps = new ArrayList<Usrgrp>();
  @JsonProperty("mediatypes")
  private List<MediaTypeResponse> mediatypes = new ArrayList<MediaTypeResponse>();
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("userid")
  public String getUserid() {
    return userid;
  }

  @JsonProperty("userid")
  public void setUserid(String userid) {
    this.userid = userid;
  }

  @JsonProperty("alias")
  public String getAlias() {
    return alias;
  }

  @JsonProperty("alias")
  public void setAlias(String alias) {
    this.alias = alias;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("surname")
  public String getSurname() {
    return surname;
  }

  @JsonProperty("surname")
  public void setSurname(String surname) {
    this.surname = surname;
  }

  @JsonProperty("url")
  public String getUrl() {
    return url;
  }

  @JsonProperty("url")
  public void setUrl(String url) {
    this.url = url;
  }

  @JsonProperty("autologin")
  public String getAutologin() {
    return autologin;
  }

  @JsonProperty("autologin")
  public void setAutologin(String autologin) {
    this.autologin = autologin;
  }

  @JsonProperty("autologout")
  public String getAutologout() {
    return autologout;
  }

  @JsonProperty("autologout")
  public void setAutologout(String autologout) {
    this.autologout = autologout;
  }

  @JsonProperty("lang")
  public String getLang() {
    return lang;
  }

  @JsonProperty("lang")
  public void setLang(String lang) {
    this.lang = lang;
  }

  @JsonProperty("refresh")
  public String getRefresh() {
    return refresh;
  }

  @JsonProperty("refresh")
  public void setRefresh(String refresh) {
    this.refresh = refresh;
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(String type) {
    this.type = type;
  }

  @JsonProperty("theme")
  public String getTheme() {
    return theme;
  }

  @JsonProperty("theme")
  public void setTheme(String theme) {
    this.theme = theme;
  }

  @JsonProperty("attempt_failed")
  public String getAttemptFailed() {
    return attemptFailed;
  }

  @JsonProperty("attempt_failed")
  public void setAttemptFailed(String attemptFailed) {
    this.attemptFailed = attemptFailed;
  }

  @JsonProperty("attempt_ip")
  public String getAttemptIp() {
    return attemptIp;
  }

  @JsonProperty("attempt_ip")
  public void setAttemptIp(String attemptIp) {
    this.attemptIp = attemptIp;
  }

  @JsonProperty("attempt_clock")
  public String getAttemptClock() {
    return attemptClock;
  }

  @JsonProperty("attempt_clock")
  public void setAttemptClock(String attemptClock) {
    this.attemptClock = attemptClock;
  }

  @JsonProperty("rows_per_page")
  public String getRowsPerPage() {
    return rowsPerPage;
  }

  @JsonProperty("rows_per_page")
  public void setRowsPerPage(String rowsPerPage) {
    this.rowsPerPage = rowsPerPage;
  }

  @JsonProperty("usrgrps")
  public List<Usrgrp> getUsrgrps() {
    return usrgrps;
  }

  @JsonProperty("usrgrps")
  public void setUsrgrps(List<Usrgrp> usrgrps) {
    this.usrgrps = usrgrps;
  }

  @JsonProperty("mediatypes")
  public List<MediaTypeResponse> getMediatypes() {
    return mediatypes;
  }

  @JsonProperty("mediatypes")
  public void setMediatypes(List<MediaTypeResponse> mediatypes) {
    this.mediatypes = mediatypes;
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

}