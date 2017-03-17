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
 * This class holds information about a Cloud provider which was found by getting
 * data from the CMDB. It contains the main fields for the probe (provider identifier,
 * nova endpoint and Keystone endpoint) and also includes other fields that can be used
 * for filtering those providers to monitor.
 * @author ATOS
 *
 */
public class CloudProviderInfo {

  public static final int OPENSTACK = 0;
  public static final int OPENNEBULA = 1;
  
  private String novaEndpoint;
  private String keystoneEndpoint;
  private int cloudType;
  private boolean isMonitored;
  private boolean isBeta;
  private boolean isProduction;
  private String providerId;
  
  /**
   * This is the constructor of the class. It just fills in the internal fields with the
   * information gathered from the CMDB.
   * @param provider String with the provider identifier
   * @param nove Nova Compute endpoint as [URL/IP:Port]
   * @param keystone Keystone endpoint as [URL/IP:Port]
   * @param type Integer indicating the Cloud platform, according to the constants
   * @param monitored Boolean indicating whether the provider is monitored
   * @param beta Boolean indicating whether the provider uses a beta version
   * @param production Boolean indicating whether the provider platform is in production
   */
  public CloudProviderInfo(String provider, /*String nova,*/ String keystone, int type,
      boolean monitored, boolean beta, boolean production) {
    providerId = provider;
    keystoneEndpoint = keystone;
    cloudType = type;
    isMonitored = monitored;
    isBeta = beta;
    isProduction = production;
  }
  
  /**
   * It retrieves the provider identifier as contained in the CMDB.
   * @return String with the identifier as defined in CMDB
   */
  public String getProviderId() {
    return providerId;
  }
  
  /**
   * It retrieves the Openstack Nova endpoint.
   * @return Openstack Nova endpoint as [URL/IP:Port]
   */
  public String getNovaEndpoint() {
    return novaEndpoint;
  }
  
  /**
   * It retrieves the Keystone endpoint.
   * @return Keystone endpoint as [URL/IP:Port]
   */
  public String getKeystoneEndpoint() {
    return keystoneEndpoint;
  }
  
  /**
   * It retrieves the cloud type.
   * @return 0 if OpenStack, 1 if Open Nebula
   */
  public int getCloudType() {
    return cloudType;
  }
  
  /**
   * It retrieves the flag indicating whether the provider is monitored.
   * @return True if it is monitored. False if not.
   */
  public boolean getIsMonitored() {
    return isMonitored;
  }
  
  /**
   * It retrieves the flag indicating whether the provider platform is in beta version.
   * @return True if it is beta. False if not.
   */
  public boolean getIsBeta() {
    return isBeta;
  }
  
  /**
   * It retrieves the flag indicating whether the provider platform is in production.
   * @return True if it is in production. False if not.
   */
  public boolean getIsProduction() {
    return isProduction;
  }
}