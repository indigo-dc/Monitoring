/**
Copyright 2016 ATOS SPAIN S.A.

Licensed under the Apache License, Version 2.0 (the License);
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Authors Contact:
Francisco Javier Nieto. Atos Research and Innovation, Atos SPAIN SA
@email francisco.nieto@atos.net
**/

package org.indigo.occiprobe.openstack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * The ZabbixSender class receives the monitored metrics for a Cloud Provider and it executes
 * the corresponding commands of the Zabbix agent in order to send all the metrics to the Zabbix
 * server, in line with the configured template and its properties.
 * @author ATOS
 *
 */
public class ZabbixSender {
  
  private String zabbixLocation;
  private String zabbixSender;
  private String provider = "Testing";
  private Runtime rt;
  
  /**
   * Main constructor of the class. It retrieves the configuration properties related to the
   * Zabbix server and it constructs the Runtime object.
   * @param targetProvider String with the name of the Cloud Provider evaluated
   */
  public ZabbixSender(String targetProvider) {
    // Retrieve location of the Zabbix Server and the Zabbix sender (local)
    PropertiesManager myProp = new PropertiesManager();
    zabbixLocation = myProp.getProperty(PropertiesManager.ZABBIX_IP);
    zabbixSender = myProp.getProperty(PropertiesManager.ZABBIX_SENDER);
    provider = targetProvider;
    
    // Create standard Runtime
    rt = Runtime.getRuntime();
  }
  
  /**
   * Constructor for unit testing purposes.
   * @param mockRuntime Mock runtime for testing
   */
  public ZabbixSender(Runtime mockRuntime) {
    rt = mockRuntime;
  }
  
  /**
   * This method extracts all the metrics gathered about the provider and it performs
   * one call per metric to Zabbix by using the zabbix_sender command of the Zaabix agent.
   * @param metrics An object containing all the gathered metrics
   * @return It indicates operation successful (true) or failed (false).
   */
  public boolean sendMetrics(OcciProbeResult metrics) {
    // Check the input is not null
    if (metrics == null || metrics.getCreateVmElement() == null) {
      return false;
    }
    
    // Prepare invocation strings and failures counter
    int failures = 0;
    String globalAvailability = "-z " + zabbixLocation + " -s \"" 
        + provider + "\" -k occi.global[availability] -o " 
        + metrics.getGlobalAvailability();
    String globalResult = "-z " + zabbixLocation + " -s \"" 
        + provider + "\" -k occi.global[result] -o " 
        + metrics.getGlobalResult();
    String globalResponseTime = "-z " + zabbixLocation + " -s \"" 
        + provider + "\" -k occi.global[responseTime] -o " 
        + metrics.getGlobalResponseTime();
    
    String createVmAvailability = "-z " + zabbixLocation + " -s \"" 
        + provider + "\" -k occi.createvm[availability] -o " 
        + metrics.getCreateVmElement().getCreateVmAvailability();
    String createVmResult = "-z " + zabbixLocation + " -s \"" 
        + provider + "\" -k occi.createvm[result] -o " 
        + metrics.getCreateVmElement().getCreateVmResult();
    String createVmResponseTime = "-z " + zabbixLocation + " -s \"" 
        + provider + "\" -k occi.createvm[responseTime] -o " 
        + metrics.getCreateVmElement().getCreateVmResponseTime();
    
    String inspectVmAvailability = "";
    String inspectVmResult = "";
    String inspectVmResponseTime = "";
    if (metrics.getInspectVmElement() != null) {
      inspectVmAvailability = "-z " + zabbixLocation + " -s \"" 
          + provider + "\" -k occi.inspectvm[availability] -o " 
          + metrics.getInspectVmElement().getInspectVmAvailability();
      inspectVmResult = "-z " + zabbixLocation + " -s \"" 
          + provider + "\" -k occi.inspectvm[result] -o " 
          + metrics.getInspectVmElement().getInspectVmResult();
      inspectVmResponseTime = "-z " + zabbixLocation + " -s \"" 
          + provider + "\" -k occi.inspectvm[responseTime] -o " 
          + metrics.getInspectVmElement().getInspectVmResponseTime();
    }
    
    String deleteVmAvailability = "";
    String deleteVmResult = "";
    String deleteVmResponseTime = "";
    if (metrics.getDeleteVmElement() != null) {
      deleteVmAvailability = "-z " + zabbixLocation + " -s \"" 
          + provider + "\" -k occi.deletevm[availability] -o " 
          + metrics.getDeleteVmElement().getDeleteVmAvailability();
      deleteVmResult = "-z " + zabbixLocation + " -s \"" 
          + provider + "\" -k occi.deletevm[result] -o " 
          + metrics.getDeleteVmElement().getDeleteVmResult();
      deleteVmResponseTime = "-z " + zabbixLocation + " -s \"" 
          + provider + "\" -k occi.deletevm[responseTime] -o " 
          + metrics.getDeleteVmElement().getDeleteVmResponseTime();
    }
    
    try {
      // Determine execution context
      String opSystem = System.getProperty("os.name").toLowerCase();
      if (opSystem.indexOf("win") >= 0) {
        globalAvailability = zabbixSender + "/zabbix_sender.exe " + globalAvailability;
        globalResult = zabbixSender + "/zabbix_sender.exe " + globalResult;
        globalResponseTime = zabbixSender + "/zabbix_sender.exe " + globalResponseTime;
        
        createVmAvailability = zabbixSender + "/zabbix_sender.exe " + createVmAvailability;
        createVmResult = zabbixSender + "/zabbix_sender.exe " + createVmResult;
        createVmResponseTime = zabbixSender + "/zabbix_sender.exe " + createVmResponseTime;
        
        inspectVmAvailability = zabbixSender + "/zabbix_sender.exe " + inspectVmAvailability;
        inspectVmResult = zabbixSender + "/zabbix_sender.exe " + inspectVmResult;
        inspectVmResponseTime = zabbixSender + "/zabbix_sender.exe " + inspectVmResponseTime;
        
        deleteVmAvailability = zabbixSender + "/zabbix_sender.exe " + deleteVmAvailability;
        deleteVmResult = zabbixSender + "/zabbix_sender.exe " + deleteVmResult;
        deleteVmResponseTime = zabbixSender + "/zabbix_sender.exe " + deleteVmResponseTime;
      } else {
        globalAvailability = zabbixSender + "/zabbix_sender " + globalAvailability;
        globalResult = zabbixSender + "/zabbix_sender " + globalResult;
        globalResponseTime = zabbixSender + "/zabbix_sender " + globalResponseTime;
        
        createVmAvailability = zabbixSender + "/zabbix_sender " + createVmAvailability;
        createVmResult = zabbixSender + "/zabbix_sender " + createVmResult;
        createVmResponseTime = zabbixSender + "/zabbix_sender " + createVmResponseTime;
        
        inspectVmAvailability = zabbixSender + "/zabbix_sender " + inspectVmAvailability;
        inspectVmResult = zabbixSender + "/zabbix_sender " + inspectVmResult;
        inspectVmResponseTime = zabbixSender + "/zabbix_sender " + inspectVmResponseTime;
        
        deleteVmAvailability = zabbixSender + "/zabbix_sender " + deleteVmAvailability;
        deleteVmResult = zabbixSender + "/zabbix_sender " + deleteVmResult;
        deleteVmResponseTime = zabbixSender + "/zabbix_sender " + deleteVmResponseTime;
      }
      
      // Run calls to Zabbix Sender
      // Process pr = rt.exec("cmd /c dir");
      Process pr = rt.exec(globalAvailability);
      readExecResponse(pr.getInputStream());
      failures = failures + pr.waitFor();
      System.out.println("Failures: " + failures);
      
      pr = rt.exec(globalResult);
      readExecResponse(pr.getInputStream());
      failures = failures + pr.waitFor();
      System.out.println("Failures: " + failures);
      
      pr = rt.exec(globalResponseTime);
      readExecResponse(pr.getInputStream());
      failures = failures + pr.waitFor();
      System.out.println("Failures: " + failures);
      
      pr = rt.exec(createVmAvailability);
      readExecResponse(pr.getInputStream());
      failures = failures + pr.waitFor();
      System.out.println("Failures: " + failures);
      
      pr = rt.exec(createVmResult);
      readExecResponse(pr.getInputStream());
      failures = failures + pr.waitFor();
      System.out.println("Failures: " + failures);
      
      pr = rt.exec(createVmResponseTime);
      readExecResponse(pr.getInputStream());
      failures = failures + pr.waitFor();
      System.out.println("Failures: " + failures);
      
      if (metrics.getInspectVmElement() != null) {
        pr = rt.exec(inspectVmAvailability);
        readExecResponse(pr.getInputStream());
        failures = failures + pr.waitFor();
        System.out.println("Failures: " + failures);
        
        pr = rt.exec(inspectVmResult);
        readExecResponse(pr.getInputStream());
        failures = failures + pr.waitFor();
        System.out.println("Failures: " + failures);
        
        pr = rt.exec(inspectVmResponseTime);
        readExecResponse(pr.getInputStream());
        failures = failures + pr.waitFor();
        System.out.println("Failures: " + failures);
      }
      
      if (metrics.getDeleteVmElement() != null) {
        pr = rt.exec(deleteVmAvailability);
        readExecResponse(pr.getInputStream());
        failures = failures + pr.waitFor();
        System.out.println("Failures: " + failures);
        
        pr = rt.exec(deleteVmResult);
        readExecResponse(pr.getInputStream());
        failures = failures + pr.waitFor();
        System.out.println("Failures: " + failures);
        
        pr = rt.exec(deleteVmResponseTime);
        readExecResponse(pr.getInputStream());
        failures = failures + pr.waitFor();
        System.out.println("Failures: " + failures);
      }
    } catch (IOException ex) {
      System.out.println("Error: " + ex.getMessage());
      return false;
    } catch (InterruptedException iex) {
      System.out.println("Error: " + iex.getMessage());
      return false;
    }
    
    // Check execution status
    if (failures > 0) {
      return false; 
    }
    
    return true;
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
}
