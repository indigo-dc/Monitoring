package org.indigo.occiprobe.openstack.test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import com.indigo.zabbix.utils.CloudProviderInfo;
import com.indigo.zabbix.utils.CmdbClient;
import com.indigo.zabbix.utils.CmdbFeignClient;
import com.indigo.zabbix.utils.beans.CmdbResponse;
import com.indigo.zabbix.utils.beans.ServiceType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.util.List;

/**
 * Created by jose on 19/04/17.
 */
public class CmdbClientTest {

  private CmdbFeignClient mockClient = Mockito.mock(CmdbFeignClient.class);

  @Before
  public void prepare() {

    String services = "{\"total_rows\":9,\"offset\":5,\"rows\":[\n" +
        "{\"id\":\"INCD-OpenNebula\",\"key\":[\"service\"],\"value\":{\"sitename\":\"INCD\",\"provider_id\":\"provider-INCD-OpenNebula\",\"hostname\":\"incd-one\",\"type\":\"compute\"},\"doc\":{\"_id\":\"INCD-OpenNebula\",\"_rev\":\"1-fe43900ec406f2cff758dffc6c791101\",\"type\":\"service\",\"data\":{\"service_type\":\"eu.egi.cloud.vm-management.occi\",\"endpoint\":\"https://incd-one:9443\",\"provider_id\":\"provider-INCD-OpenNebula\",\"type\":\"compute\",\"hostname\":\"incd-one\",\"sitename\":\"INCD\"}}},\n" +
        "{\"id\":\"INCD-Openstack\",\"key\":[\"service\"],\"value\":{\"sitename\":\"INCD\",\"provider_id\":\"provider-INCD\",\"hostname\":\"incd-os\",\"type\":\"compute\"},\"doc\":{\"_id\":\"INCD-Openstack\",\"_rev\":\"3-366e7631edbfe9a2326b044b64d24990\",\"type\":\"service\",\"data\":{\"service_type\":\"eu.egi.cloud.vm-management.openstack\",\"endpoint\":\"https://incd-os:5000/v3\",\"provider_id\":\"provider-INCD\",\"hostname\":\"incd-os\",\"sitename\":\"INCD\",\"type\":\"compute\"}}},\n" +
        "{\"id\":\"T-Systems-Openstack\",\"key\":[\"service\"],\"value\":{\"sitename\":\"T-Systems\",\"provider_id\":\"provider-T-Systems-Openstack\",\"hostname\":\"tsystem-os\",\"type\":\"compute\"},\"doc\":{\"_id\":\"T-Systems-Openstack\",\"_rev\":\"1-eeafe39e1aa860ee9498e2753b0b20ac\",\"type\":\"service\",\"data\":{\"service_type\":\"eu.egi.cloud.vm-management.openstack\",\"endpoint\":\"https://tystem-os:443/v3\",\"provider_id\":\"provider-T-Systems-Openstack\",\"type\":\"compute\",\"hostname\":\"tsystem-os\",\"sitename\":\"T-Systems\"}}},\n" +
        "{\"id\":\"TEST-Openstack\",\"key\":[\"service\"],\"value\":{\"sitename\":\"TEST\",\"provider_id\":\"provider-TEST\",\"hostname\":\"provider-test\",\"type\":\"compute\"},\"doc\":{\"_id\":\"TEST-Openstack\",\"_rev\":\"2-aede69c9adba724912a28c2f6403fcce\",\"type\":\"service\",\"data\":{\"service_type\":\"eu.egi.cloud.vm-management.openstack\",\"endpoint\":\"https://provider-test:5000/v3\",\"provider_id\":\"provider-TEST\",\"hostname\":\"provider-test\",\"sitename\":\"TEST\",\"type\":\"compute\"}}}\n" +
        "]}";

    String provider = "{\"total_rows\":5,\"offset\":2,\"rows\":[\n" +
        "{\"id\":\"T-Systems-Openstack\",\"key\":[\"provider-T-Systems-Openstack\",\"services\"],\"value\":{\"sitename\":\"T-Systems\",\"provider_id\":\"provider-T-Systems-Openstack\",\"hostname\":\"tsystem-os\",\"type\":\"compute\"},\"doc\":{\"_id\":\"T-Systems-Openstack\",\"_rev\":\"1-eeafe39e1aa860ee9498e2753b0b20ac\",\"type\":\"service\",\"data\":{\"service_type\":\"eu.egi.cloud.vm-management.openstack\",\"endpoint\":\"https://tsystem-os:443/v3\",\"provider_id\":\"provider-T-Systems-Openstack\",\"type\":\"compute\",\"hostname\":\"tsystem-os\",\"sitename\":\"T-Systems\"}}}\n" +
        "]}";

    CmdbResponse<ServiceType> serviceResponse = new Gson().fromJson(services, new TypeToken<CmdbResponse<ServiceType>>(){}.getType());

    Mockito.when(mockClient.services()).thenReturn(serviceResponse);

    Mockito.when(mockClient.providerInfo(Matchers.anyString())).thenReturn(new Gson().fromJson(provider, JsonElement.class));

  }

  @Test
  public void testCmdb() {
    CmdbClient client = new CmdbClient(mockClient);

    List<CloudProviderInfo> response = client.getFeasibleProvidersInfo();

    assert response != null;
    assert response.isEmpty();
  }

}
