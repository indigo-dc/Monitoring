package org.indigo.openstackprobe.openstack;

import com.indigo.zabbix.utils.beans.AppOperation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openstack4j.api.exceptions.ConnectionException;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;

import java.util.concurrent.TimeoutException;

public class OpenstackCollectorTest {

  private VmResultCreation createVmMocked;
  private VmResultDeletion deleteVmMocked;
  private OpenStackProbeResult probeMocked;
  private OpenStackClient osMocked;
  private ServerCreate serverMocked;
  private AppOperation appMocked;
  private VmResultInspection inspectVmResult;
  Server server;
  private OpenstackCollector oscollector;

  @Before
  public void prepareCollector()
      throws TimeoutException, InterruptedException, ConnectionException {
    osMocked = Mockito.mock(OpenStackClient.class);
    probeMocked = Mockito.mock(OpenStackProbeResult.class);

    serverMocked = Mockito.mock(ServerCreate.class);

    createVmMocked = Mockito.mock(VmResultCreation.class);
    deleteVmMocked = Mockito.mock(VmResultDeletion.class);
    inspectVmResult = Mockito.mock(VmResultInspection.class);
    probeMocked.addDeleteVmInfo(deleteVmMocked);
    probeMocked.addCreateVmInfo(createVmMocked);
    probeMocked.addGlobalInfo(1, 200, 2L);

    Mockito.when(osMocked.getOpenstackMonitoringInfo("cloud.recas.ba.infn.it"))
        .thenReturn(probeMocked);

    // Mockito.when(osMocked.tokenId).thenReturn("tokenId");
    Mockito.when(osMocked.createOsServer("vmNameTest")).thenReturn(serverMocked);

    appMocked = Mockito.mock(AppOperation.class);
    Mockito.when(createVmMocked.getCreateVmAvailability()).thenReturn(200);
    Mockito.when(probeMocked.getDeleteVmElement()).thenReturn(deleteVmMocked);
    Mockito.when(probeMocked.getCreateVmElement()).thenReturn(createVmMocked);
    Mockito.when(probeMocked.getInspectVmElement()).thenReturn(inspectVmResult);

    Mockito.when(probeMocked.getGlobalResult()).thenReturn(2000);

    // Failure
    // Mockito.when(probeMocked.getDeleteVmElement()).thenThrow(new
    // ConnectException());
    // Mockito.when(probeMocked.getCreateVmElement()).thenThrow(new
    // ConnectException());
  }

  @Test
  public void getCreateTest() {
    new AppOperation(
        AppOperation.Operation.RUN,
        true,
        probeMocked.getDeleteVmElement().getDeleteVmAvailability(),
        2L);
    probeMocked.getDeleteVmElement();
    probeMocked.getCreateVmElement();
  }

  // @Test
  // public void createCollectorAndEnsureCloudProviderAuthenticationWorks() throws Exception {
  // PropertiesManager.loadProperties(OpenstackProbeTags.CONFIG_FILE);
  //
  // OAuthJSONAccessTokenResponse response = IamClient.getAccessToken();
  // String accessToken = response.getAccessToken();
  // List<CloudProviderInfo> providers = new ArrayList<>();
  // providers.add(new CloudProviderInfo("provider-RECAS-BARI",
  // "","https://cloud.recas.ba.infn.it:5000/v3", 0, true,
  // false, true));
  // providers.add(
  // new CloudProviderInfo("NCG-INGRID-PT", "","https://nimbus.ncg.ingrid.pt:5000/v3", 0, true,
  // false, true));
  // for (CloudProviderInfo provider : providers) {
  //
  // new OpenstackCollector(accessToken, "provider-RECAS-BARI",
  // "https://cloud.recas.ba.infn.it:5000/v3");
  // }
  // }

}
