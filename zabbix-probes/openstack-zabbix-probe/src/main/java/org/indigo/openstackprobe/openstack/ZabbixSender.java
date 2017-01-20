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

package org.indigo.openstackprobe.openstack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The ZabbixSender class receives the monitored metrics for a Cloud Provider and it executes
 * the corresponding commands of the Zabbix agent in order to send all the metrics to the Zabbix
 * server, in line with the configured template and its properties.
 * Since the Zabbix agent does not support concurrent invocations and we use several monitoring
 * threads, this class follows a singleton pattern and provides some synchronized methods as a
 * way to gather metrics and send them only once.
 * @author ATOS
 *
 */
public class ZabbixSender {
  
  private static ZabbixSender _instance = null;
  private String zabbixLocation;
  private String zabbixSender;
  private Runtime rt;
  private ZabbixWrapperClient myClient;
  private ArrayList<OpenstackProbeResult> metricsQueue;
  
  /**
   * It provides the current ZabbixSender instance and, in case it does not exist,
   * it creates a new instance and gives it as the result.
   * @return The current single instance of the ZabbixSender 
   */
  public static synchronized ZabbixSender instance() {
    if (null == _instance) {
      _instance = new ZabbixSender();
      System.out.println("A new instance of the Zabbix Sender was created!");
    }
    return _instance;
  }
  
  /**
   * Main constructor of the class. It retrieves the configuration properties related to the
   * Zabbix server and it constructs the Runtime object.
   * @param targetProvider String with the name of the Cloud Provider evaluated
   */
  private ZabbixSender() {
    // Retrieve location of the Zabbix Server and the Zabbix sender (local)
    PropertiesManager myProp = new PropertiesManager();
    zabbixLocation = myProp.getProperty(PropertiesManager.ZABBIX_IP);
    zabbixSender = myProp.getProperty(PropertiesManager.ZABBIX_SENDER);
    metricsQueue = new ArrayList<OpenstackProbeResult>();
    
    // Create standard Runtime and Wrapper Client
    rt = Runtime.getRuntime();
    myClient = new ZabbixWrapperClient();
  }
  
  /**
   * It provides the current ZabbixSender instance and, in case it does not exist,
   * it creates a new instance and gives it as the result. This method is for
   * unit testing purposes.
   * @return The current single instance of the ZabbixSender 
   */
  public static synchronized ZabbixSender instance(Runtime mockRuntime, ZabbixWrapperClient mock) {
    _instance = new ZabbixSender(mockRuntime, mock);
    System.out.println("A new testing instance of the Zabbix sender for testing was created!");
    
    return _instance;
  }
  
  /**
   * Constructor for unit testing purposes.
   * @param mockRuntime Mock runtime for testing
   */
  private ZabbixSender(Runtime mockRuntime, ZabbixWrapperClient wrapperMock) {
    rt = mockRuntime;
    myClient = wrapperMock;
    metricsQueue = new ArrayList<OpenstackProbeResult>();
  }
  
  /**
   * This method enqueues a set of monitoring metrics coming from an evaluation.
   * They will be ready to be pushed to Zabbix whenever required.
   * @param metrics An object containing all the gathered metrics
   * @return True if the operation succeed.
   */
  public synchronized boolean addMetricToQueue(OpenstackProbeResult metrics) {
    if (metrics == null) {
      return false;
    }
    metricsQueue.add(metrics);
    return true;
  }
  
  /**
   * It iterates que queue in order to push all the gathered metrics to Zabbix at the same
   * time, in order to avoid issues with concurrent calls. 
   * @return True if all the metrics where pushed correctly. False if not.
   */
  public synchronized boolean sendMetrics() {
    // Iterate the queue and push the metrics to Zabbix
    if (metricsQueue.size() == 0) {
      return false;
    }
    boolean result = true;
    Iterator<OpenstackProbeResult> metricsIterator = metricsQueue.iterator();
    while (metricsIterator.hasNext()) {
      OpenstackProbeResult currentMetric = metricsIterator.next();
      System.out.println("Pushing metrics from: " + currentMetric.getProviderName());
      result = result & sendMetrics(currentMetric);
    }
    return result;  
  }
  
  /**
   * This method extracts all the metrics gathered about the provider and it performs
   * one call per metric to Zabbix by using the zabbix_sender command of the Zaabix agent.
   * @param metrics An object containing all the gathered metrics
   * @return It indicates operation successful (true) or failed (false).
   */
  private boolean sendMetrics(OpenstackProbeResult metrics) {
    // Check the input is not null
    if (metrics == null || metrics.getCreateVmElement() == null) {
      return false;
    }
    
    // Check the provider is available as Zabbix Host
    String provider = metrics.getProviderName();
    correctHostRegistration(provider);
    
    // Prepare invocation strings and failures counter
    int failures = 0;
    
    String globalAvailability = "-z " + zabbixLocation + " -s \"" 
        + provider + "\" -k openstack.global[availability] -o " 
        + metrics.getGlobalAvailability();
    String globalResult = "-z " + zabbixLocation + " -s \"" 
        + provider + "\" -k openstack.global[result] -o " 
        + metrics.getGlobalResult();
    String globalResponseTime = "-z " + zabbixLocation + " -s \"" 
        + provider + "\" -k openstack.global[responseTime] -o " 
        + metrics.getGlobalResponseTime();
    
    String createVmAvailability = "-z " + zabbixLocation + " -s \"" 
        + provider + "\" -k openstack.createvm[availability] -o " 
        + metrics.getCreateVmElement().getCreateVmAvailability();
    String createVmResult = "-z " + zabbixLocation + " -s \"" 
        + provider + "\" -k openstack.createvm[result] -o " 
        + metrics.getCreateVmElement().getCreateVmResult();
    String createVmResponseTime = "-z " + zabbixLocation + " -s \"" 
        + provider + "\" -k openstack.createvm[responseTime] -o " 
        + metrics.getCreateVmElement().getCreateVmResponseTime();
    
    String inspectVmAvailability = "";
    String inspectVmResult = "";
    String inspectVmResponseTime = "";
    if (metrics.getInspectVmElement() != null) {
      inspectVmAvailability = "-z " + zabbixLocation + " -s \"" 
          + provider + "\" -k openstack.inspectvm[availability] -o " 
          + metrics.getInspectVmElement().getInspectVmAvailability();
      inspectVmResult = "-z " + zabbixLocation + " -s \"" 
          + provider + "\" -k openstack.inspectvm[result] -o " 
          + metrics.getInspectVmElement().getInspectVmResult();
      inspectVmResponseTime = "-z " + zabbixLocation + " -s \"" 
          + provider + "\" -k openstack.inspectvm[responseTime] -o " 
          + metrics.getInspectVmElement().getInspectVmResponseTime();
    }
    
    String deleteVmAvailability = "";
    String deleteVmResult = "";
    String deleteVmResponseTime = "";
    if (metrics.getDeleteVmElement() != null) {
      deleteVmAvailability = "-z " + zabbixLocation + " -s \"" 
          + provider + "\" -k openstack.deletevm[availability] -o " 
          + metrics.getDeleteVmElement().getDeleteVmAvailability();
      deleteVmResult = "-z " + zabbixLocation + " -s \"" 
          + provider + "\" -k openstack.deletevm[result] -o " 
          + metrics.getDeleteVmElement().getDeleteVmResult();
      deleteVmResponseTime = "-z " + zabbixLocation + " -s \"" 
          + provider + "\" -k openstack.deletevm[responseTime] -o " 
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
  
  private boolean correctHostRegistration(String hostName) {
    // Look for the host in the Wrapper
    boolean result = false;
    result = myClient.isHostRegistered(hostName);
    
    // If it is not available, register it
    if (result == false) {
      result = myClient.registerHost(hostName);
      System.out.println("It was not possible to register the Host. Try anyway...");
    }
    
    return result;
  }
  
  /**
   * Typical main for testing.
   * @param args Typical args
   */
  public static void main(String[] args) {
    // Create a monitoring element and send it
    CreateVmResult create = new CreateVmResult(1, 200, 1429, "testVM");
    InspectVmResult inspect = new InspectVmResult(1, 200, 426);
    DeleteVmResult delete = new DeleteVmResult(1, 204, 612);
    OpenstackProbeResult global = new OpenstackProbeResult(1, 204, 2467, "PruHost");
    global.addCreateVmInfo(create);
    global.addInspectVmInfo(inspect);
    global.addDeleteVmInfo(delete);

    // Send metrics and check the result
    ZabbixSender mySender = ZabbixSender.instance();
    mySender.addMetricToQueue(global);
    boolean result = mySender.sendMetrics();
    System.out.println("Result: " + result);
  }
}