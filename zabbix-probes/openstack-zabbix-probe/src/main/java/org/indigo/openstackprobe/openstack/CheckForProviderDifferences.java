package org.indigo.openstackprobe.openstack;

import com.google.common.base.Strings;

import com.indigo.zabbix.utils.ProbesTags;
import com.indigo.zabbix.utils.PropertiesManager;

import java.util.ArrayList;
import java.util.List;

import org.openstack4j.model.image.Image;


/* This class is intended to be temporary until */
public class CheckForProviderDifferences {

  private OpenStackConfiguration osconfig;
  private Image image;

  protected String checkForOpenstackProject(String providerName) {
    if (Boolean
        .parseBoolean(PropertiesManager.getProperty(OpenStackProbeTags.PROVIDERS_EXCEPTIONS))) {
      for (CloudProviderZone provider : getProviders()) {
        if (providerName.equals(provider.getName()) &&
            Strings.emptyToNull(provider.getTenant()) != null) {
          return provider.getTenant();
        }
      }
    }
    return PropertiesManager.getProperty(ProbesTags.OPENSTACK_PROJECT);
  }

  /**
   * Get the list of providers.
   * 
   * @return the list of Providers listed into zones.yml
   */
  protected List<CloudProviderZone> getProviders() {
    if (OpenStackConfiguration.zone != null && OpenStackConfiguration.zone.contains("test")) {
      osconfig = new OpenStackConfiguration("testoszone.yml");
    } else {
      osconfig = new OpenStackConfiguration();
    }
    List<CloudProviderZone> providerzones = new ArrayList<>();
    for (CloudProviderZone zone : osconfig.getMonitoringZones().getCloudProvidersZones()) {
      providerzones.add(zone);
    }
    return providerzones;
  }



}
