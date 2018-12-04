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

package org.indigo.occiprobe.openstack;

import com.indigo.zabbix.utils.CloudProviderInfo;
import com.indigo.zabbix.utils.KeystoneClient;
import com.indigo.zabbix.utils.LifecycleCollector;
import com.indigo.zabbix.utils.PropertiesManager;
import com.indigo.zabbix.utils.ZabbixMetrics;
import com.indigo.zabbix.utils.beans.AppOperation;
import cz.cesnet.cloud.occi.Model;
import cz.cesnet.cloud.occi.api.EntityBuilder;
import cz.cesnet.cloud.occi.api.exception.CommunicationException;
import cz.cesnet.cloud.occi.api.exception.EntityBuildingException;
import cz.cesnet.cloud.occi.api.http.HTTPClient;
import cz.cesnet.cloud.occi.core.Entity;
import cz.cesnet.cloud.occi.core.Mixin;
import cz.cesnet.cloud.occi.core.Resource;
import cz.cesnet.cloud.occi.exception.AmbiguousIdentifierException;
import cz.cesnet.cloud.occi.exception.InvalidAttributeValueException;
import cz.cesnet.cloud.occi.infrastructure.IPNetworkInterface;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * It takes care of the interactions to be performed with Cloud Providers whose base platform is
 * OpenStack, since it requires additional interactions with the Keystone component, in order to get
 * authorization tokens. This class is able to perform basic operations on VMs: create, inspect and
 * delete.
 *
 * @author ATOS
 */
public class OpenStackOcciClient extends LifecycleCollector {

  private static final Log logger = LogFactory.getLog(OpenStackOcciClient.class);

  public static final String OCCI_OS = "http://schemas.openstack.org/template/os#";
  public static final String OCCI_RESOURCE = "http://schemas.openstack.org/template/resource#";
  public static final String OCCI_NETWORK = "http://schemas.ogf.org/occi/infrastructure/network#";

  private cz.cesnet.cloud.occi.api.Client occiClient;

  private String providerId;

  private URI vmId;

  private CloudProviderInfo configuration;

  private OcciProbeResult creationFailResults;

  // private IOSClientBuilder.V3 myKeystoneClient = null;

  /**
   * Main constructor of the OpenStackOcciClient class. It retrieves some information from the
   * properties files in order to create and configure the client which will connect to the remote
   * OCCI API of a cloud provider.
   *
   * @param configuration Monitoring configuration of the provider.
   */
  public OpenStackOcciClient(String accessToken, CloudProviderInfo configuration) {

    this.configuration = configuration;
    creationFailResults = null;

    // Retrieve properties
    String project = PropertiesManager.getProperty(OcciProbeTags.OCCI_OS_PROJECT);

    String occiToken =
        new KeystoneClient(configuration.getKeystoneEndpoint())
            .getScopedToken(
                accessToken,
                configuration.getIdentityProvider(),
                configuration.getProtocol(),
                project);

    try {
      occiClient =
          new HTTPClient(URI.create(configuration.getOcciEndpoint()), new TokenOcciAuth(occiToken));
    } catch (CommunicationException e) {

      int code = 400;
      boolean availability = true;

      creationFailResults = new OcciProbeResult(availability, code, 0);
    }

    this.providerId = configuration.getProviderId();
  }

  /**
   * Constructor to be used for automatic testing purposes only.
   *
   * @param mockClient Mock of the Jersey Client class, for simulating.
   */
  public OpenStackOcciClient(
      cz.cesnet.cloud.occi.api.Client mockClient, CloudProviderInfo configuration) {
    occiClient = mockClient;
    this.configuration = configuration;
  }

  @Override
  public String getHostName() {
    return providerId;
  }

  @Override
  protected AppOperation clear() {
    return new AppOperation(AppOperation.Operation.CLEAR, true, 200, 0);
  }

  @Override
  protected AppOperation create() {
    // Call to OCCI API

    Model model = occiClient.getModel();

    EntityBuilder builder = new EntityBuilder(model);

    long startTime = System.currentTimeMillis();

    int httpCode = 200;
    boolean availability = true;
    URI vmId = null;
    try {

      List<URI> list = occiClient.list();

      Mixin os = model.findMixin(configuration.getImageId());

      Mixin flavour = null;
      if (configuration.getOsFlavour() != null) {
        flavour = model.findMixin(configuration.getOsFlavour());
      }

      Resource resource = builder.getResource("compute");
      resource.addMixin(os);
      if (flavour != null) {
        resource.addMixin(flavour);
      }

      if (configuration.getNetworkId() != null) {
        IPNetworkInterface link = builder.getIPNetworkInterface();

        link.setTarget(configuration.getNetworkId());

        resource.addLink(link);
      }

      this.vmId = occiClient.create(resource);
    } catch (CommunicationException e) {
      logger.error("Error creating occi vm", e);
      httpCode = 500;
      availability = false;
    } catch (EntityBuildingException e) {
      logger.error("Error creating occi vm", e);
      httpCode = 400;
      availability = false;
    } catch (AmbiguousIdentifierException e) {
      logger.error("Error creating occi vm", e);
      httpCode = 400;
      availability = false;
    } catch (InvalidAttributeValueException e) {
      httpCode = 400;
      availability = false;
    }

    long responseTime = System.currentTimeMillis() - startTime;

    // System.out.println("Created VM: " + vmId);

    // Feed monitoring info
    AppOperation monitoredInfo =
        new AppOperation(AppOperation.Operation.CREATE, availability, httpCode, responseTime);

    return monitoredInfo;
  }

  @Override
  protected AppOperation retrieve() {
    // Invoke the OCCI service and measure response time
    int httpCode = 200;
    boolean availability = true;
    long startTime = System.currentTimeMillis();
    try {
      List<Entity> entities = occiClient.describe(vmId);
      if (entities == null || entities.isEmpty()) {
        httpCode = 400;
      }
    } catch (CommunicationException e) {
      logger.error("Error getting info of resource " + vmId, e);
      httpCode = 500;
      availability = false;
    }
    long responseTime = System.currentTimeMillis() - startTime;

    // Feed monitoring info
    AppOperation monitoredInfo =
        new AppOperation(AppOperation.Operation.RUN, availability, httpCode, responseTime);

    return monitoredInfo;
  }

  @Override
  protected AppOperation delete() {
    // Invoke the OCCI service and measure response time
    int httpCode = 200;
    boolean availability = true;
    long startTime = System.currentTimeMillis();
    try {
      if (!occiClient.delete(vmId)) {
        httpCode = 400;
      }
    } catch (CommunicationException e) {
      logger.error("Error deleting vm " + vmId, e);
      httpCode = 500;
      availability = false;
    }
    long responseTime = System.currentTimeMillis() - startTime;

    // Feed monitoring info
    AppOperation monitoredInfo =
        new AppOperation(AppOperation.Operation.DELETE, availability, httpCode, responseTime);

    return monitoredInfo;
  }

  @Override
  public ZabbixMetrics getMetrics() {

    ZabbixMetrics metrics = new ZabbixMetrics();
    metrics.setHostName(getHostName());
    metrics.setTimestamp(System.currentTimeMillis());

    if (creationFailResults != null) {
      metrics.setMetrics(creationFailResults.getMetrics());
    } else {

      long startTime = System.currentTimeMillis();
      Map<AppOperation.Operation, AppOperation> operations = super.executeLifecycle();
      long responseTime = System.currentTimeMillis() - startTime;

      final OcciProbeResult global = new OcciProbeResult(true, 200, responseTime);

      boolean globalAvailability = true;
      int globalResult = 200;

      this.result
          .entrySet()
          .forEach(
              entry -> {
                AppOperation value = entry.getValue();
                global.setGlobalAvailability(global.getGlobalAvailability() && value.isResult());
                global.setGlobalResult(Math.max(global.getGlobalResult(), value.getStatus()));

                if (!AppOperation.Operation.CLEAR.equals(value.getOperation())) {
                  String prefix = null;
                  switch (entry.getKey()) {
                    case CREATE:
                      prefix = "occi.createvm";
                      break;
                    case RUN:
                      prefix = "occi.inspectvm";
                      break;
                    case DELETE:
                      prefix = "occi.deletevm";
                      break;
                    default:
                      break;
                  }

                  metrics
                      .getMetrics()
                      .put(
                          prefix + "[availability]",
                          BooleanUtils.toIntegerObject(value.isResult()).toString());

                  metrics
                      .getMetrics()
                      .put(prefix + "[responseTime]", Long.toString(value.getResponseTime()));

                  metrics
                      .getMetrics()
                      .put(prefix + "[result]", Integer.toString(value.getStatus()));
                }
              });

      metrics.getMetrics().putAll(global.getMetrics());
    }

    return metrics;
  }
}
