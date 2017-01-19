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

import java.io.Serializable;

/**
 * This class supports Host serialization when new Zabbix hosts are created.
 * @author ATOS
 *
 */
public class ZabbixHost implements Serializable {
  
  private static final long serialVersionUID = 1L;
  private String ip;  
  private String serviceCategory;
  private String serviceId;
  private String uuid;
  private String[] atomicServices;
  private boolean activeMode;
  
  /**
   * This is the constructor of the class, setting by default most of the properties.
   * @param hostName Identifier of the host
   */
  public ZabbixHost(String hostName) {
    ip = "127.0.0.1";    
    uuid = hostName;
    serviceCategory = "IaaS";
    serviceId = "id";
    atomicServices = new String[]{"OCCI"};
    activeMode = false;
  }
  
  /**
   * It retrieves the IP.
   * @return Default IP: 127.0.0.1
   */
  public String getIp() {
    return ip;
  }
  
  /**
   * It retrieves the Uuid.
   * @return It corresponds to the Host Name
   */
  public String getUuid() {
    return uuid;
  }
  
  /**
   * It retrieves the service category.
   * @return The default value is IaaS
   */
  public String getServiceCategory() {
    return serviceCategory;
  }
  
  /**
   * It retrieves the service Identifier.
   * @return It is serviceId by default.
   */
  public String getServiceId() {
    return serviceId;
  }
  
  /**
   * It retrieves the list of atomic services.
   * @return Set to OCCI by default.
   */
  public String[] getAtomicServices() {
    return atomicServices;
  }
  
  /**
   * It retrieves the mode.
   * @return Set to false by default.
   */
  public boolean getActiveMode() {
    return activeMode;
  }
}
