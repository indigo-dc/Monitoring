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

package com.indigo.zabbix.utils;

import com.indigo.zabbix.utils.beans.DocDataType;

import java.io.Serializable;
import java.util.UUID;

/**
 * This class supports Host serialization when new Zabbix hosts are created.
 *
 * @author ATOS
 */
public class ZabbixHost implements Serializable {

  private static final long serialVersionUID = 1L;
  private String ip;
  private String uuid;
  private DocDataType.ServiceType serviceCategory;
  private String serviceId;
  private String[] atomicServices;
  private boolean activeMode;

  /**
   * This is the constructor of the class, setting by default most of the properties.
   *
   * @param hostName Identifier of the host
   */
  public ZabbixHost(
      String hostName,
      DocDataType.ServiceType serviceType,
      String category,
      String group,
      String template) {
    ip = "127.0.0.1";
    uuid = UUID.randomUUID().toString();
    serviceCategory = serviceType;
    serviceId = category + "_" + group + "_" + hostName;
    atomicServices = new String[] {template};
    activeMode = false;
  }

  /**
   * It retrieves the IP.
   *
   * @return Default IP: 127.0.0.1
   */
  public String getIp() {
    return ip;
  }

  /**
   * It retrieves the Uuid.
   *
   * @return It corresponds to the Host Name
   */
  public String getUuid() {
    return uuid;
  }

  /**
   * It retrieves the service category.
   *
   * @return The default value is PaaS
   */
  public DocDataType.ServiceType getServiceCategory() {
    return serviceCategory;
  }

  /**
   * It retrieves the service Identifier.
   *
   * @return It is serviceId by default.
   */
  public String getServiceId() {
    return serviceId;
  }

  /**
   * It retrieves the list of atomic services.
   *
   * @return Set to Heapster by default.
   */
  public String[] getAtomicServices() {
    return atomicServices;
  }

  /**
   * It retrieves the mode.
   *
   * @return Set to false by default.
   */
  public boolean getActiveMode() {
    return activeMode;
  }
}
