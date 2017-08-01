/*
 * Copyright 2016 ATOS SPAIN S.A.
 * 
 * Licensed under the Apache License, Version 2.0 (the License); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Authors Contact: Francisco Javier Nieto. Atos Research and Innovation, Atos SPAIN SA
 * 
 * @email francisco.nieto@atos.net
 **/

package com.indigo.zabbix.utils;

/**
 * This class holds information about a Cloud provider which was found by getting data from the
 * CMDB. It contains the main fields for the probe (provider identifier, OCCI endpoint and Keystone
 * endpoint) and also includes other fields that can be used for filtering those providers to
 * monitor.
 * 
 * @author ATOS
 *
 */
public class CloudProviderInfo {

  public static final int OPENSTACK = 0;
  public static final int OPENNEBULA = 1;

  private String occiEndpoint;
  private String keystoneEndpoint;
  private int cloudType;
  private boolean isMonitored;
  private boolean isBeta;
  private boolean isProduction;
  private String providerId;

  // TODO: Figure out a way to extract this OCCI specific information outside
  private String identityProvider;
  private String protocol;
  private String imageId;
  private String osFlavour;
  private String networkId;

  /**
   * This is the constructor of the class. It just fills in the internal fields with the information
   * gathered from the CMDB.
   * 
   * @param provider String with the provider identifier
   * @param occi OCCI endpoint as [URL/IP:Port]
   * @param keystone Keystone endpoint as [URL/IP:Port]
   * @param type Integer indicating the Cloud platform, according to the constants
   * @param monitored Boolean indicating whether the provider is monitored
   * @param beta Boolean indicating whether the provider uses a beta version
   * @param production Boolean indicating whether the provider platform is in production
   */
  public CloudProviderInfo(String provider, String occi, String keystone, int type,
      boolean monitored, boolean beta, boolean production) {
    providerId = provider;
    occiEndpoint = occi;
    keystoneEndpoint = keystone;
    cloudType = type;
    isMonitored = monitored;
    isBeta = beta;
    isProduction = production;
  }

  /**
   * This is the constructor of the class. It just fills in the internal fields with the information
   * gathered from the CMDB.
   * 
   * @param provider String with the provider identifier
   * @param occi OCCI endpoint as [URL/IP:Port]
   * @param keystone Keystone endpoint as [URL/IP:Port]
   * @param type Integer indicating the Cloud platform, according to the constants
   * @param monitored Boolean indicating whether the provider is monitored
   * @param beta Boolean indicating whether the provider uses a beta version
   * @param production Boolean indicating whether the provider platform is in production
   * @param identityProvider Identity provider for IAM integration
   * @param protocol Protocol to use to get keystone tokens
   * @param imageId Image ID to use for monitoring
   * @param osFlavour Flavour to use for monitoring
   * @param networkId Network ID to use when necessary
   */
  public CloudProviderInfo(String provider, String occi, String keystone, int type,
      boolean monitored, boolean beta, boolean production, String identityProvider, String protocol,
      String imageId, String osFlavour, String networkId) {


    this(provider, occi, keystone, type, monitored, beta, production);
    this.identityProvider = identityProvider;
    this.protocol = protocol;
    this.imageId = imageId;
    this.osFlavour = osFlavour;
    this.networkId = networkId;
  }

  /**
   * It retrieves the provider identifier as contained in the CMDB.
   * 
   * @return String with the identifier as defined in CMDB
   */
  public String getProviderId() {
    return providerId;
  }

  /**
   * It retrieves the OCCI endpoint.
   * 
   * @return OCCI endpoint as [URL/IP:Port]
   */
  public String getOcciEndpoint() {
    return occiEndpoint;
  }

  /**
   * It retrieves the Keystone endpoint.
   * 
   * @return Keystone endpoint as [URL/IP:Port]
   */
  public String getKeystoneEndpoint() {
    return keystoneEndpoint;
  }

  /**
   * It retrieves the cloud type.
   * 
   * @return 0 if OpenStack, 1 if Open Nebula
   */
  public int getCloudType() {
    return cloudType;
  }

  /**
   * It retrieves the flag indicating whether the provider is monitored.
   * 
   * @return True if it is monitored. False if not.
   */
  public boolean getIsMonitored() {
    return isMonitored;
  }

  /**
   * It retrieves the flag indicating whether the provider platform is in beta version.
   * 
   * @return True if it is beta. False if not.
   */
  public boolean getIsBeta() {
    return isBeta;
  }

  /**
   * It retrieves the flag indicating whether the provider platform is in production.
   * 
   * @return True if it is in production. False if not.
   */
  public boolean getIsProduction() {
    return isProduction;
  }

  /**
   * Returns the identity provider for IAM integration.
   * 
   * @return The identity provider ID.
   */
  public String getIdentityProvider() {
    return identityProvider;
  }

  /**
   * Sets the identity provider for IAM integration.
   * 
   * @param identityProvider The identity provider ID.
   */
  public void setIdentityProvider(String identityProvider) {
    this.identityProvider = identityProvider;
  }

  /**
   * Retrieves the protocol to get keystone tokens.
   * 
   * @return The protocol name.
   */
  public String getProtocol() {
    return protocol;
  }

  /**
   * Sets the protocol to get keystone tokens.
   * 
   * @param protocol The protocol name.
   */
  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  /**
   * Get the image ID for monitoring.
   * 
   * @return The image ID.
   */
  public String getImageId() {
    return imageId;
  }

  /**
   * Gets the OS flavour to use for monitoring.
   * 
   * @return The OS flavour.
   */
  public String getOsFlavour() {
    return osFlavour;
  }

  /**
   * Gets the network id to use for monitoring.
   * 
   * @return The network id.
   */
  public String getNetworkId() {
    return networkId;
  }
}
