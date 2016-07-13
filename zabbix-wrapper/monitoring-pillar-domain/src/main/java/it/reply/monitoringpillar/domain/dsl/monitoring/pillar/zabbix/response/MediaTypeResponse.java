package it.reply.monitoringpillar.domain.dsl.monitoring.pillar.zabbix.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "mediatypeid", "type", "description", "smtp_server", "smtp_helo", "smtp_email",
    "exec_path", "gsm_modem", "username", "passwd", "status" })
public class MediaTypeResponse {

  @JsonProperty("mediatypeid")
  private String mediatypeid;
  @JsonProperty("type")
  private String type;
  @JsonProperty("description")
  private String description;
  @JsonProperty("smtp_server")
  private String smtpServer;
  @JsonProperty("smtp_helo")
  private String smtpHelo;
  @JsonProperty("smtp_email")
  private String smtpEmail;
  @JsonProperty("exec_path")
  private String execPath;
  @JsonProperty("gsm_modem")
  private String gsmModem;
  @JsonProperty("username")
  private String username;
  @JsonProperty("passwd")
  private String passwd;
  @JsonProperty("status")
  private String status;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("mediatypeid")
  public String getMediatypeid() {
    return mediatypeid;
  }

  @JsonProperty("mediatypeid")
  public void setMediatypeid(String mediatypeid) {
    this.mediatypeid = mediatypeid;
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(String type) {
    this.type = type;
  }

  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  @JsonProperty("smtp_server")
  public String getSmtpServer() {
    return smtpServer;
  }

  @JsonProperty("smtp_server")
  public void setSmtpServer(String smtpServer) {
    this.smtpServer = smtpServer;
  }

  @JsonProperty("smtp_helo")
  public String getSmtpHelo() {
    return smtpHelo;
  }

  @JsonProperty("smtp_helo")
  public void setSmtpHelo(String smtpHelo) {
    this.smtpHelo = smtpHelo;
  }

  @JsonProperty("smtp_email")
  public String getSmtpEmail() {
    return smtpEmail;
  }

  @JsonProperty("smtp_email")
  public void setSmtpEmail(String smtpEmail) {
    this.smtpEmail = smtpEmail;
  }

  @JsonProperty("exec_path")
  public String getExecPath() {
    return execPath;
  }

  @JsonProperty("exec_path")
  public void setExecPath(String execPath) {
    this.execPath = execPath;
  }

  @JsonProperty("gsm_modem")
  public String getGsmModem() {
    return gsmModem;
  }

  @JsonProperty("gsm_modem")
  public void setGsmModem(String gsmModem) {
    this.gsmModem = gsmModem;
  }

  @JsonProperty("username")
  public String getUsername() {
    return username;
  }

  @JsonProperty("username")
  public void setUsername(String username) {
    this.username = username;
  }

  @JsonProperty("passwd")
  public String getPasswd() {
    return passwd;
  }

  @JsonProperty("passwd")
  public void setPasswd(String passwd) {
    this.passwd = passwd;
  }

  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(String status) {
    this.status = status;
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
