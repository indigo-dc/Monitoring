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

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;

import javax.ws.rs.client.Client;

/**
 * The class IamClient is in charge of the interactions between the OCCI probe
 * and the Indigo Identity and Access Management (IAM) Server. Basically, it
 * logs in the configured user and it retrieves the required credentials for
 * accessing the authorized Cloud Providers OCCI APIs.
 * @author ATOS
 *
 */
public class IamClient {
  private Client client = null;
  private String baseIamUrl;  
  private String iamUser = "";
  private String iamPwd = "";
  
  /**
   * Main constructor of the IamClient class. It retrieves some information
   * from the properties files in order to create and configure the client which 
   * will connect to the remote REST API of the IAM.
   * @param iamLocation String with the full location of the IAM [IP:Port]  
   */
  public IamClient(String iamLocation) {
    // Retrieve properties
    PropertiesManager myProp = new PropertiesManager();
    iamUser = myProp.getProperty(PropertiesManager.OPENSTACK_USER);
    iamPwd = myProp.getProperty(PropertiesManager.OPENSTACK_PASSWORD);
    
    // Disable issue with SSL Handshake in Java 7 and indicate certificates keystore
    System.setProperty("jsse.enableSNIExtension", "false");
    String certificatesTrustStorePath = myProp.getProperty(PropertiesManager.JAVA_KEYSTORE);
    System.setProperty("javax.net.ssl.trustStore", certificatesTrustStorePath);
    
    // Create the Client
    ClientConfig cc = new ClientConfig();
    client = JerseyClientBuilder.newClient(cc);
    
    // Prepare access URLs
    baseIamUrl = iamLocation;             
  }
    
  /**
   * Constructor to be used for automatic testing purposes only.
   * @param mockClient Mock of the Jersey Client class, for simulating.
   */
  public IamClient(Client mockClient) {
    client = mockClient;
  }
}
