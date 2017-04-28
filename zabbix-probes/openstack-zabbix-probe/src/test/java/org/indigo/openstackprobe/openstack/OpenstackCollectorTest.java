package org.indigo.openstackprobe.openstack;

import java.util.List;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.exceptions.ConnectionException;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;

import com.indigo.zabbix.utils.beans.AppOperation;

public class OpenstackCollectorTest {
	
	private CreateVmResult createVmMocked;
	private DeleteVmResult deleteVmMocked;
	private OpenstackProbeResult probeMocked;
	private OpenStackClient osMocked;
	private ServerCreate serverMocked;
	private AppOperation appMocked;
	private InspectVmResult inspectVmResult;
	Server server;
	
	@Before
	public void prepareCollector() throws TimeoutException, InterruptedException, ConnectionException{
		osMocked = Mockito.mock(OpenStackClient.class);
		probeMocked = Mockito.mock(OpenstackProbeResult.class);
		
		serverMocked = Mockito.mock(ServerCreate.class);
		
		createVmMocked = Mockito.mock(CreateVmResult.class);
		deleteVmMocked =Mockito.mock(DeleteVmResult.class);
		inspectVmResult = Mockito.mock(InspectVmResult.class);
		probeMocked.addDeleteVmInfo(deleteVmMocked);
		probeMocked.addCreateVmInfo(createVmMocked);
		probeMocked.addGlobalInfo(1, 200, 2L);

		Mockito.when(osMocked.getOpenstackMonitoringInfo()).thenReturn(probeMocked);
		
//		Mockito.when(osMocked.tokenId).thenReturn("tokenId");
		Mockito.when(osMocked.createOsServer("vmNameTest")).thenReturn(serverMocked);
		
		appMocked = Mockito.mock(AppOperation.class);
		Mockito.when(createVmMocked.getCreateVmAvailability()).thenReturn(200);
		Mockito.when(probeMocked.getDeleteVmElement()).thenReturn(deleteVmMocked);
		Mockito.when(probeMocked.getCreateVmElement()).thenReturn(createVmMocked);
		Mockito.when(probeMocked.getInspectVmElement()).thenReturn(inspectVmResult);

		Mockito.when(probeMocked.getGlobalResult()).thenReturn(2000);
		
		//Failure 
//				Mockito.when(probeMocked.getDeleteVmElement()).thenThrow(new ConnectException());
//				Mockito.when(probeMocked.getCreateVmElement()).thenThrow(new ConnectException());
	}
	
	@Test
	public void getCreateTest(){
		new AppOperation(appMocked.getOperation().RUN, true, probeMocked.getDeleteVmElement().getDeleteVmAvailability(), 2L);
		probeMocked.getDeleteVmElement();
		probeMocked.getCreateVmElement();
	}
	
}
