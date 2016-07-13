package it.reply.monitoringpillar.domain.dsl.monitoring.businesslayer.paas.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "service", "vcpusUsed", "hypervisorType", "localGbUsed", "hostIp",
    "hypervisorHostname", "id", "memoryMb", "currentWorkload", "vcpus", "freeRamMb", "runningVms",
    "free_disk_gb", "hypervisor_version", "disk_available_least", "localGb", "cpuInfo",
    "memoryMbUsed" })
public class Hypervisor implements Serializable {

  private static final long serialVersionUID = -8425673865503069848L;

  @JsonProperty("service")
  private Service service;
  @JsonProperty("vcpusUsed")
  private Integer vcpusUsed;
  @JsonProperty("hypervisorType")
  private String hypervisorType;
  @JsonProperty("localGbUsed")
  private Integer localGbUsed;
  @JsonProperty("hostIp")
  private String hostIp;
  @JsonProperty("hypervisorHostname")
  private String hypervisorHostname;
  @JsonProperty("id")
  private Integer id;
  @JsonProperty("memoryMb")
  private Integer memoryMb;
  @JsonProperty("currentWorkload")
  private Integer currentWorkload;
  @JsonProperty("vcpus")
  private Integer vcpus;
  @JsonProperty("freeRamMb")
  private Integer freeRamMb;
  @JsonProperty("runningVms")
  private Integer runningVms;
  @JsonProperty("freeDiskGb")
  private Integer freeDiskGb;
  @JsonProperty("hypervisorVersion")
  private Integer hypervisorVersion;
  @JsonProperty("diskAvailableLeast")
  private Integer diskAvailableLeast;
  @JsonProperty("localGb")
  private Integer localGb;
  @JsonProperty("cpuInfo")
  private String cpuInfo;
  @JsonProperty("memoryMbUsed")
  private Integer memoryMbUsed;

  @JsonProperty("service")
  public Service getService() {
    return service;
  }

  @JsonProperty("service")
  public void setService(Service service) {
    this.service = service;
  }

  @JsonProperty("vcpusUsed")
  public Integer getVcpusUsed() {
    return vcpusUsed;
  }

  @JsonProperty("vcpusUsed")
  public void setVcpusUsed(Integer vcpusUsed) {
    this.vcpusUsed = vcpusUsed;
  }

  @JsonProperty("hypervisorType")
  public String getHypervisorType() {
    return hypervisorType;
  }

  @JsonProperty("hypervisorType")
  public void setHypervisorType(String hypervisorType) {
    this.hypervisorType = hypervisorType;
  }

  @JsonProperty("localGbUsed")
  public Integer getLocalGbUsed() {
    return localGbUsed;
  }

  @JsonProperty("localGbUsed")
  public void setLocalGbUsed(Integer localGbUsed) {
    this.localGbUsed = localGbUsed;
  }

  @JsonProperty("hostIp")
  public String getHostIp() {
    return hostIp;
  }

  @JsonProperty("hostIp")
  public void setHostIp(String hostIp) {
    this.hostIp = hostIp;
  }

  @JsonProperty("hypervisorHostname")
  public String getHypervisorHostname() {
    return hypervisorHostname;
  }

  @JsonProperty("hypervisorHostname")
  public void setHypervisorHostname(String hypervisorHostname) {
    this.hypervisorHostname = hypervisorHostname;
  }

  @JsonProperty("id")
  public Integer getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(Integer id) {
    this.id = id;
  }

  @JsonProperty("memoryMb")
  public Integer getMemoryMb() {
    return memoryMb;
  }

  @JsonProperty("memoryMb")
  public void setMemoryMb(Integer memoryMb) {
    this.memoryMb = memoryMb;
  }

  @JsonProperty("currentWorkload")
  public Integer getCurrentWorkload() {
    return currentWorkload;
  }

  @JsonProperty("currentWorkload")
  public void setCurrentWorkload(Integer currentWorkload) {
    this.currentWorkload = currentWorkload;
  }

  @JsonProperty("vcpus")
  public Integer getVcpus() {
    return vcpus;
  }

  @JsonProperty("vcpus")
  public void setVcpus(Integer vcpus) {
    this.vcpus = vcpus;
  }

  @JsonProperty("freeRamMb")
  public Integer getFreeRamMb() {
    return freeRamMb;
  }

  @JsonProperty("freeRamMb")
  public void setFreeRamMb(Integer freeRamMb) {
    this.freeRamMb = freeRamMb;
  }

  @JsonProperty("runningVms")
  public Integer getRunningVms() {
    return runningVms;
  }

  @JsonProperty("runningVms")
  public void setRunningVms(Integer runningVms) {
    this.runningVms = runningVms;
  }

  @JsonProperty("freeDiskGb")
  public Integer getFreeDiskGb() {
    return freeDiskGb;
  }

  @JsonProperty("freeDiskGb")
  public void setFreeDiskGb(Integer freeDiskGb) {
    this.freeDiskGb = freeDiskGb;
  }

  @JsonProperty("hypervisorVersion")
  public Integer getHypervisorVersion() {
    return hypervisorVersion;
  }

  @JsonProperty("hypervisorVersion")
  public void setHypervisorVersion(Integer hypervisorVersion) {
    this.hypervisorVersion = hypervisorVersion;
  }

  @JsonProperty("diskAvailableLeast")
  public Integer getDiskAvailableLeast() {
    return diskAvailableLeast;
  }

  @JsonProperty("diskAvailableLeast")
  public void setDiskAvailableLeast(Integer diskAvailableLeast) {
    this.diskAvailableLeast = diskAvailableLeast;
  }

  @JsonProperty("localGb")
  public Integer getLocalGb() {
    return localGb;
  }

  @JsonProperty("localGb")
  public void setLocalGb(Integer localGb) {
    this.localGb = localGb;
  }

  @JsonProperty("cpuInfo")
  public String getCpuInfo() {
    return cpuInfo;
  }

  @JsonProperty("cpuInfo")
  public void setCpuInfo(String cpuInfo) {
    this.cpuInfo = cpuInfo;
  }

  @JsonProperty("memoryMbUsed")
  public Integer getMemoryMbUsed() {
    return memoryMbUsed;
  }

  @JsonProperty("memoryMbUsed")
  public void setMemoryMbUsed(Integer memoryMbUsed) {
    this.memoryMbUsed = memoryMbUsed;
  }

  @Override
  public String toString() {
    return "Hypervisor [service=" + service + ", vcpusUsed=" + vcpusUsed + ", hypervisorType="
        + hypervisorType + ", localGbUsed=" + localGbUsed + ", hostIp=" + hostIp
        + ", hypervisorHostname=" + hypervisorHostname + ", id=" + id + ", memoryMb=" + memoryMb
        + ", currentWorkload=" + currentWorkload + ", vcpus=" + vcpus + ", freeRamMb=" + freeRamMb
        + ", runningVms=" + runningVms + ", freeDiskGb=" + freeDiskGb + ", hypervisorVersion="
        + hypervisorVersion + ", diskAvailableLeast=" + diskAvailableLeast + ", localGb=" + localGb
        + ", cpuInfo=" + cpuInfo + ", memoryMbUsed=" + memoryMbUsed + "]";
  }

}
