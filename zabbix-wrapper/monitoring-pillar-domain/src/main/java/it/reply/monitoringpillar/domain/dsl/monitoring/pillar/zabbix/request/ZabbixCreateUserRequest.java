package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.request;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response.Usrgrp;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "alias", "passwd", "usrgrps", "user_medias" })
public class ZabbixCreateUserRequest {

  @JsonProperty("alias")
  private String alias;
  @JsonProperty("passwd")
  private String passwd;
  @JsonProperty("usrgrps")
  private List<Usrgrp> usrgrps = new ArrayList<Usrgrp>();
  @JsonProperty("user_medias")
  private List<UserMediaRequest> userMedias = new ArrayList<UserMediaRequest>();
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("alias")
  public String getAlias() {
    return alias;
  }

  @JsonProperty("alias")
  public void setAlias(String alias) {
    this.alias = alias;
  }

  @JsonProperty("passwd")
  public String getPasswd() {
    return passwd;
  }

  @JsonProperty("passwd")
  public void setPasswd(String passwd) {
    this.passwd = passwd;
  }

  @JsonProperty("usrgrps")
  public List<Usrgrp> getUsrgrps() {
    return usrgrps;
  }

  @JsonProperty("usrgrps")
  public void setUsrgrps(List<Usrgrp> usrgrps) {
    this.usrgrps = usrgrps;
  }

  @JsonProperty("user_medias")
  public List<UserMediaRequest> getUserMedias() {
    return userMedias;
  }

  @JsonProperty("user_medias")
  public void setUserMedias(List<UserMediaRequest> userMedias) {
    this.userMedias = userMedias;
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