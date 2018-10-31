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
package org.indigo.occiprobe.openstack.test;

import com.indigo.zabbix.utils.CloudProviderInfo;
import com.indigo.zabbix.utils.ZabbixMetrics;
import cz.cesnet.cloud.occi.Model;
import cz.cesnet.cloud.occi.api.exception.CommunicationException;
import cz.cesnet.cloud.occi.core.Entity;
import cz.cesnet.cloud.occi.core.Kind;
import cz.cesnet.cloud.occi.core.Mixin;
import cz.cesnet.cloud.occi.core.Resource;
import org.indigo.occiprobe.openstack.OpenStackOcciClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.openstack4j.api.client.IOSClientBuilder;

import javax.ws.rs.client.Client;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MonitoringTest {

  private static final String INFRA_KIND = "http://schemas.ogf.org/occi/infrastructure#";

  private enum MixinType {
    OS,
    RESOURCE
  }

  private Client mockClient;
  private Client mockClientFailure;
  private IOSClientBuilder.V3 keystoneMock;
  private cz.cesnet.cloud.occi.api.Client occiMock =
      Mockito.mock(cz.cesnet.cloud.occi.api.Client.class);

  private List<URI> vms = new ArrayList<>();

  private Model createServerModel() {
    Model result = new Model();

    result.addMixin(createMixin(MixinType.OS, "Ubuntu"));
    result.addMixin(createMixin(MixinType.OS, "Debian"));

    result.addMixin(createMixin(MixinType.RESOURCE, "small"));
    result.addMixin(createMixin(MixinType.RESOURCE, "large"));

    result.addKind(
        new Kind(
            URI.create(INFRA_KIND),
            "compute",
            "compute",
            URI.create("http://test_system/compute"),
            new ArrayList<>()));

    return result;
  }

  private Mixin createMixin(MixinType type, String title) {
    String schema = "";
    switch (type) {
      case OS:
        schema = OpenStackOcciClient.OCCI_OS;
        break;
      case RESOURCE:
        schema = OpenStackOcciClient.OCCI_RESOURCE;
        break;
    }
    return new Mixin(
        URI.create(schema),
        title,
        title,
        URI.create("http://test_system/" + title),
        new ArrayList<>());
  }

  @Before
  public void prepareMockOCCIServer() throws CommunicationException {

    Model serverModel = createServerModel();
    Mockito.when(occiMock.getModel()).thenReturn(serverModel);

    Mockito.when(occiMock.create(Matchers.any(Resource.class)))
        .thenAnswer(
            new Answer<URI>() {

              @Override
              public URI answer(InvocationOnMock invocation) {
                URI result = URI.create("http://test_system/" + UUID.randomUUID().toString());
                vms.add(result);
                return result;
              }
            });

    Mockito.when(occiMock.describe(Matchers.any(URI.class)))
        .thenAnswer(
            new Answer<List<Entity>>() {
              @Override
              public List<Entity> answer(InvocationOnMock invocation) {
                List<Entity> result = new ArrayList<>();

                URI parameter = invocation.getArgumentAt(0, URI.class);
                if (parameter != null) {
                  if (vms.contains(parameter)) {
                    result.add(Mockito.mock(Entity.class));
                  }
                }

                return result;
              }
            });

    Mockito.when(occiMock.delete(Matchers.any(URI.class)))
        .thenAnswer(
            new Answer<Boolean>() {

              @Override
              public Boolean answer(InvocationOnMock invocation) {
                URI uri = invocation.getArgumentAt(0, URI.class);
                return vms.remove(uri);
              }
            });
  }

  @Test
  public void monitoringOperationsShouldReturnCompleteResult() {
    OpenStackOcciClient client =
        new OpenStackOcciClient(
            occiMock,
            new CloudProviderInfo(
                "provider",
                "http://test/occi",
                "http://keystone",
                0,
                true,
                false,
                true,
                "indigo-dc",
                "oidc",
                "Ubuntu",
                "small",
                null));

    ZabbixMetrics metrics = client.getMetrics();
    metrics
        .getMetrics()
        .entrySet()
        .forEach(
            entry -> {
              assert !entry.getKey().contains("result") || "200".equals(entry.getValue());

              assert !entry.getKey().contains("availability") || "1".equals(entry.getValue());

              assert !entry.getKey().contains("responseTime") || new Long(entry.getValue()) >= 0;
            });
  }

  /*@Test
  public void monitoringOperationShouldReturnPartialResult()
  {
  	// Run the OCCI monitoring process and retrieve the result
  	OpenStackOcciClient myClient = new OpenStackOcciClient(occiMock);
  	OcciProbeResult result = myClient.getOcciMonitoringInfo();

  	// Check Results
  	Assert.assertNotNull("The result must contain information.", result);
  	Assert.assertEquals("The createVM invocation should reflect 404 status.", 404, result.getCreateVmElement().getCreateVmResult());
  	Assert.assertNull("The inspectVM result should not exist.", result.getInspectVmElement());
  	Assert.assertEquals("The general availability result should be 0", 0, result.getGlobalAvailability());
  }*/

}
