package it.reply.monitoringpillar.domain.dsl.monitoring;

public enum MachineStatus {
  READY("READY"), PROBLEM("PROBLEM"), VALUE_ZERO("0");

  private String machineStatus;

  private MachineStatus(String machineStatus) {
    this.machineStatus = machineStatus;
  }

  public String getStatus() {
    return this.machineStatus;
  }

  public String toString() {
    return this.machineStatus;
  }
}
