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

/**
 * This class is for managing information about the Cloud providers listed
 * by the CMDB component. All these providers are expected to be monitored.
 * @author ATOS
 *
 */
public class ProviderInfo {

  private String providerId;
  private boolean isOpenStack;
  private String keystoneUrl;
  private String occiUrl;
  
  /**
   * This constructor applies to those Cloud Providers who are providing services
   * based on OpenStack platforms (which require Keystone for authentication).
   * @param identifier String identifying the Cloud provider
   * @param keystoneLocation Base URL for Keystone [IP:Port]
   * @param occiLocation Base URL for OCCI API [IP:Port]
   */
  public ProviderInfo(String identifier, String keystoneLocation, String occiLocation) {
    isOpenStack = true;
    providerId = identifier;
    keystoneUrl = keystoneLocation;
    occiUrl = occiLocation;
  }
  
  /**
   * This constructor is used with those Cloud Providers who are providing services
   * based on OpenNebula platforms.
   * @param identifier String identifying the Cloud provider
   * @param occiLocation Base URL for OCCI API [IP:Port]
   */
  public ProviderInfo(String identifier, String occiLocation) {
    isOpenStack = false;
    providerId = identifier;
    occiUrl = occiLocation;
  }
  
  /**
   * It provides a boolean indicating whether the provider is based on OpenStack.
   * @return True for OpenStack providers, False for others.
   */
  public boolean isOpenStack() {
    return isOpenStack;
  }
  
  /**
   * It provides the identifier of the Cloud provider.
   * @return String with the identifier.
   */
  public String getProviderId() {
    return providerId;
  }
  
  /**
   * It provides the base URL of the REST OCCI API.
   * @return String with the base URL in the form [IP:Port]
   */
  public String getOcciUrl() {
    return occiUrl;
  }
  
  /**
   * It provides the base URL of the Keystone component.
   * @return String with the base URL in the form [IP:Port]
   */
  public String getKeystoneUrl() {
    return keystoneUrl;
  }
}
