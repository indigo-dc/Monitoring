/**
 * Copyright 2016 ATOS SPAIN S.A.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the License); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * <p>Authors Contact: Francisco Javier Nieto. Atos Research and Innovation, Atos SPAIN SA
 *
 * @email francisco.nieto@atos.net
 */
package org.indigo.heapsterprobe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * The ZabbixSender class receives the monitored metrics of a Kubernetes cluster and it executes the
 * corresponding commands of the Zabbix agent in order to send all the metrics to the Zabbix server,
 * in line with the configured template and its properties.
 *
 * @author ATOS
 */
public class ZabbixSender {

  private String zabbixLocation;
  private String zabbixSender;
  private String cluster;
  private Runtime rt;
  private ZabbixWrapperClient myClient;

  /**
   * Main constructor of the class. It retrieves the configuration properties related to the Zabbix
   * server and it constructs the Runtime object.
   *
   * @param clusterName String with the name of the Cluster evaluated
   */
  public ZabbixSender(String clusterName) {
    // Retrieve location of the Zabbix Server and the Zabbix sender (local)
    PropertiesManager myProp = new PropertiesManager();
    zabbixLocation = myProp.getProperty(PropertiesManager.ZABBIX_IP);
    zabbixSender = myProp.getProperty(PropertiesManager.ZABBIX_SENDER);
    cluster = clusterName;

    // Create standard Runtime and Wrapper Client
    rt = Runtime.getRuntime();
    myClient = new ZabbixWrapperClient();
  }

  /**
   * Constructor for unit testing purposes.
   *
   * @param mockRuntime Mock runtime for testing
   */
  public ZabbixSender(Runtime mockRuntime, ZabbixWrapperClient mockWrapper) {
    rt = mockRuntime;
    myClient = mockWrapper;
  }

  /**
   * This method extracts all the metrics gathered about a pod and it performs one call per metric
   * to Zabbix by using the zabbix_sender command of the Zaabix agent.
   *
   * @param metrics An object containing all the gathered metrics
   * @return It indicates operation successful (true) or failed (false).
   */
  public boolean sendPodMetrics(PodMetrics metrics) {

    // Check the input is not null
    if (metrics == null) {
      return false;
    }

    // Check the provider is available as Zabbix Host
    String podName = metrics.getPodName();
    correctPodRegistration(podName);

    // Prepare local variables and failure counters
    int failures = 0;

    // Prepare invocation strings
    String rxErrors =
        "-z "
            + zabbixLocation
            + " -s \""
            + podName
            + "\" -k pod.network[rxErrors] -o "
            + metrics.getNetworkTxErrors();
    String rxErrorsRate =
        "-z "
            + zabbixLocation
            + " -s \""
            + podName
            + "\" -k pod.network[rxErrorsRate] -o "
            + metrics.getNetworkRxErrorsRate();
    String txErrors =
        "-z "
            + zabbixLocation
            + " -s \""
            + podName
            + "\" -k pod.network[txErrors] -o "
            + metrics.getNetworkTxErrors();
    String txErrorsRate =
        "-z "
            + zabbixLocation
            + " -s \""
            + podName
            + "\" -k pod.network[txErrorsRate] -o "
            + metrics.getNetworkTxErrorsRate();

    String majorPageFaults =
        "-z "
            + zabbixLocation
            + " -s \""
            + podName
            + "\" -k pod.memory[majorPageFaultsRate] -o "
            + metrics.getMemoryMajorPageFaultsRate();
    String pageFaults =
        "-z "
            + zabbixLocation
            + " -s \""
            + podName
            + "\" -k pod.memory[pageFaultsRate] -o "
            + metrics.getMemoryPageFaultsRate();
    String uptime =
        "-z " + zabbixLocation + " -s \"" + podName + "\" -k pod.uptime -o " + metrics.getUptime();

    try {
      // Determine execution context
      String opSys = System.getProperty("os.name").toLowerCase();
      if (opSys.indexOf("win") >= 0) {
        rxErrors = zabbixSender + "/zabbix_sender.exe " + rxErrors;
        rxErrorsRate = zabbixSender + "/zabbix_sender.exe " + rxErrorsRate;
        txErrors = zabbixSender + "/zabbix_sender.exe " + txErrors;
        txErrorsRate = zabbixSender + "/zabbix_sender.exe " + txErrorsRate;

        majorPageFaults = zabbixSender + "/zabbix_sender.exe " + majorPageFaults;
        pageFaults = zabbixSender + "/zabbix_sender.exe " + pageFaults;
        uptime = zabbixSender + "/zabbix_sender.exe " + uptime;
      } else {
        rxErrors = zabbixSender + "/zabbix_sender " + rxErrors;
        rxErrorsRate = zabbixSender + "/zabbix_sender " + rxErrorsRate;
        txErrors = zabbixSender + "/zabbix_sender " + txErrors;
        txErrorsRate = zabbixSender + "/zabbix_sender " + txErrorsRate;

        majorPageFaults = zabbixSender + "/zabbix_sender " + majorPageFaults;
        pageFaults = zabbixSender + "/zabbix_sender " + pageFaults;
        uptime = zabbixSender + "/zabbix_sender " + uptime;
      }

      Process pr = rt.exec(rxErrors);
      readExecResponse(pr.getInputStream());
      failures = failures + pr.waitFor();
      System.out.println("Failures: " + failures);

      pr = rt.exec(rxErrorsRate);
      readExecResponse(pr.getInputStream());
      failures = failures + pr.waitFor();
      System.out.println("Failures: " + failures);

      pr = rt.exec(txErrors);
      readExecResponse(pr.getInputStream());
      failures = failures + pr.waitFor();
      System.out.println("Failures: " + failures);

      pr = rt.exec(txErrorsRate);
      readExecResponse(pr.getInputStream());
      failures = failures + pr.waitFor();
      System.out.println("Failures: " + failures);

      pr = rt.exec(majorPageFaults);
      readExecResponse(pr.getInputStream());
      failures = failures + pr.waitFor();
      System.out.println("Failures: " + failures);

      pr = rt.exec(pageFaults);
      readExecResponse(pr.getInputStream());
      failures = failures + pr.waitFor();
      System.out.println("Failures: " + failures);

      pr = rt.exec(uptime);
      readExecResponse(pr.getInputStream());
      failures = failures + pr.waitFor();
      System.out.println("Failures: " + failures);
    } catch (IOException ex) {
      System.out.println("Error: " + ex.getMessage());
      return false;
    } catch (InterruptedException iex) {
      System.out.println("Error: " + iex.getMessage());
      return false;
    }

    // Check execution status
    return failures <= 0;
  }

  /**
   * This method extracts all the metrics gathered about a container and it performs one call per
   * metric to Zabbix by using the zabbix_sender command of the Zaabix agent.
   *
   * @param metrics An object containing all the gathered metrics
   * @return It indicates operation successful (true) or failed (false).
   */
  public boolean sendContainerMetrics(ContainerMetrics metrics) {

    // Check the input is not null
    if (metrics == null) {
      return false;
    }

    // Check the provider is available as Zabbix Host
    String containerName = metrics.getContainerName();
    correctContainerRegistration(containerName);

    // Prepare local variables and failure counters
    int failures = 0;

    // Prepare invocation strings
    String cpuUsageRate =
        "-z "
            + zabbixLocation
            + " -s \""
            + containerName
            + "\" -k container.cpu[usageRate] -o "
            + metrics.getCpuUsageRate();
    String cpuUsage =
        "-z "
            + zabbixLocation
            + " -s \""
            + containerName
            + "\" -k container.cpu[usage] -o "
            + metrics.getCpuUsage();

    String memoryUsage =
        "-z "
            + zabbixLocation
            + " -s \""
            + containerName
            + "\" -k container.memory[usage] -o "
            + metrics.getMemoryUsage();
    String workingSet =
        "-z "
            + zabbixLocation
            + " -s \""
            + containerName
            + "\" -k container.memory[workingSet] -o "
            + metrics.getWorkingset();
    String majorPageFaultsRate =
        "-z "
            + zabbixLocation
            + " -s \""
            + containerName
            + "\" -k container.memory[majorPageFaultsRate] -o "
            + metrics.getMajorPageFaultsRate();
    String pageFaultsRate =
        "-z "
            + zabbixLocation
            + " -s \""
            + containerName
            + "\" -k container.memory[pageFaultsRate] -o "
            + metrics.getPageFaultsRate();

    String uptime =
        "-z "
            + zabbixLocation
            + " -s \""
            + containerName
            + "\" -k container.uptime -o "
            + metrics.getUptime();

    try {
      // Determine execution context
      String opSys = System.getProperty("os.name").toLowerCase();
      if (opSys.indexOf("win") >= 0) {
        cpuUsageRate = zabbixSender + "/zabbix_sender.exe " + cpuUsageRate;
        cpuUsage = zabbixSender + "/zabbix_sender.exe " + cpuUsage;

        memoryUsage = zabbixSender + "/zabbix_sender.exe " + memoryUsage;
        workingSet = zabbixSender + "/zabbix_sender.exe " + workingSet;
        majorPageFaultsRate = zabbixSender + "/zabbix_sender.exe " + majorPageFaultsRate;
        pageFaultsRate = zabbixSender + "/zabbix_sender.exe " + pageFaultsRate;

        uptime = zabbixSender + "/zabbix_sender.exe " + uptime;
      } else {
        cpuUsageRate = zabbixSender + "/zabbix_sender " + cpuUsageRate;
        cpuUsage = zabbixSender + "/zabbix_sender " + cpuUsage;

        memoryUsage = zabbixSender + "/zabbix_sender " + memoryUsage;
        workingSet = zabbixSender + "/zabbix_sender " + workingSet;
        majorPageFaultsRate = zabbixSender + "/zabbix_sender " + majorPageFaultsRate;
        pageFaultsRate = zabbixSender + "/zabbix_sender " + pageFaultsRate;

        uptime = zabbixSender + "/zabbix_sender " + uptime;
      }

      Process pr = rt.exec(cpuUsageRate);
      readExecResponse(pr.getInputStream());
      failures = failures + pr.waitFor();
      System.out.println("Failures: " + failures);

      pr = rt.exec(cpuUsage);
      readExecResponse(pr.getInputStream());
      failures = failures + pr.waitFor();
      System.out.println("Failures: " + failures);

      pr = rt.exec(memoryUsage);
      readExecResponse(pr.getInputStream());
      failures = failures + pr.waitFor();
      System.out.println("Failures: " + failures);

      pr = rt.exec(workingSet);
      readExecResponse(pr.getInputStream());
      failures = failures + pr.waitFor();
      System.out.println("Failures: " + failures);

      pr = rt.exec(majorPageFaultsRate);
      readExecResponse(pr.getInputStream());
      failures = failures + pr.waitFor();
      System.out.println("Failures: " + failures);

      pr = rt.exec(pageFaultsRate);
      readExecResponse(pr.getInputStream());
      failures = failures + pr.waitFor();
      System.out.println("Failures: " + failures);

      pr = rt.exec(uptime);
      readExecResponse(pr.getInputStream());
      failures = failures + pr.waitFor();
      System.out.println("Failures: " + failures);
    } catch (IOException ex) {
      System.out.println("Error: " + ex.getMessage());
      return false;
    } catch (InterruptedException iex) {
      System.out.println("Error: " + iex.getMessage());
      return false;
    }

    // Check execution status
    return failures <= 0;
  }

  private void readExecResponse(InputStream response) {
    BufferedReader input = new BufferedReader(new InputStreamReader(response));

    try {
      String line = null;
      while ((line = input.readLine()) != null) {
        System.out.println(line);
      }
    } catch (Exception ex) {
      System.out.println("Error: " + ex.getMessage());
    }
  }

  private boolean correctPodRegistration(String hostName) {
    // Look for the host in the Wrapper
    boolean result = false;
    result = myClient.isPodRegistered(hostName);

    // If it is not available, register it
    if (result == false) {
      result = myClient.registerPodHost(hostName);
      System.out.println("It was not possible to register the Host. Try anyway...");
    }

    return result;
  }

  private boolean correctContainerRegistration(String hostName) {
    // Look for the host in the Wrapper
    boolean result = false;
    result = myClient.isContainerRegistered(hostName);

    // If it is not available, register it
    if (result == false) {
      result = myClient.registerContainerHost(hostName);
      System.out.println("It was not possible to register the Host. Try anyway...");
    }

    return result;
  }

  /**
   * Typical main for simple testing.
   *
   * @param args Typical arguments
   */
  public static void main(String[] args) {
    HeapsterClient myClient = new HeapsterClient();
    PodMetrics myResult = myClient.getPodMetrics("");

    ZabbixSender mySender = new ZabbixSender("Indigo");
    mySender.sendPodMetrics(myResult);
  }
}
