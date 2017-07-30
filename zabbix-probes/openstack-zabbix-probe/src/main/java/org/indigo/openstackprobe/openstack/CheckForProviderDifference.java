package org.indigo.openstackprobe.openstack;

import com.indigo.zabbix.utils.ProbesTags;
import com.indigo.zabbix.utils.PropertiesManager;

import org.openstack4j.model.image.Image;

import java.util.ArrayList;
import java.util.List;



/* This class is intended to be temporary until */
public class CheckForProviderDifference {

  private OpenstackConfiguration osconfig;
  private String project = null;
  private Image image;

  protected String checkForOpenstackProject(String providerName) {
    if (Boolean
        .parseBoolean(PropertiesManager.getProperty(OpenstackProbeTags.PROVIDERS_EXCEPTIONS))) {
      for (CloudProvidersZone provider : getProviders()) {
        project = (providerName.toLowerCase().contains("recas")) ? "INDIGO_DEMO"
            : PropertiesManager.getProperty(ProbesTags.OPENSTACK_PROJECT);
      }
    }
    return project;
  }

  /**
   * Get the list of providers.
   * 
   * @return the list of Providers listed into zones.yml
   */
  protected List<CloudProvidersZone> getProviders() {
    if (OpenstackConfiguration.zone != null && OpenstackConfiguration.zone.contains("test")) {
      osconfig = new OpenstackConfiguration("testoszone.yml");
    } else {
      osconfig = new OpenstackConfiguration();
    }
    List<CloudProvidersZone> providerzones = new ArrayList<>();
    for (CloudProvidersZone zone : osconfig.getMonitoringZones().getCloudProvidersZones()) {
      providerzones.add(zone);
    }
    return providerzones;
  }



}
