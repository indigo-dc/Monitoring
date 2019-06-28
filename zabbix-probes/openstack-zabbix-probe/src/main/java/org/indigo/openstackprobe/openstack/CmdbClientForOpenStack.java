package org.indigo.openstackprobe.openstack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.indigo.zabbix.utils.CmdbFeignClient;
import com.indigo.zabbix.utils.ProbeClientFactory;
import com.indigo.zabbix.utils.ProbesTags;
import com.indigo.zabbix.utils.PropertiesManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.client.Client;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;



/**
 * The CmdbClient class is in charge of the interactions between the probe and the CMDB component.
 * Such component provides information about the available providers, such as their name, location,
 * list of services provided, etc...
 * 
 * @author Reply
 *
 */
public class CmdbClientForOpenStack {
  private Client client = null;

  private String cmdbUrl;
  private static final String SERVICE_TYPE = "eu.egi.cloud.vm-management.openstack";
  private static final String NOVA_DEFUALT_PORT = "8774";
  private static final String IDENTITY_DEFAULT_PORT = "5000";
  private static final String VERSION_2 = "v2.0";
  private static final String VERSION_3 = "v3";
  private static final String OCCI_DEFAULT_PORT = "8787";

  private CmdbFeignClient cmdbClient;

  private static final Logger log = LogManager.getLogger(CmdbClientForOpenStack.class);

  /**
   * FEIGN used into Cmdb.
   */
  public CmdbClientForOpenStack() {
    try {
      PropertiesManager.loadProperties(OpenStackProbeTags.CONFIG_FILE);
    } catch (IOException e) {
      log.debug("Unable to load property file: {}", OpenStackProbeTags.CONFIG_FILE, e);
    }
    cmdbUrl = PropertiesManager.getProperty(ProbesTags.CMDB_URL);
    // Create the Client
    cmdbClient = ProbeClientFactory.getClient(CmdbFeignClient.class, cmdbUrl);
  }

  /**
   * This is a constructor for unit testing purposes.
   * 
   * @param mock Mock of the Jersey Client class
   */
  public CmdbClientForOpenStack(CmdbFeignClient mock, String cmdburlMocked) {
    cmdbUrl = cmdburlMocked;
    cmdbClient = mock;
  }

  /**
   * Get the list of images.
   * 
   * @return array of strings
   */
  public String[] getImageList() {
    // Call to CMDB API
    JsonElement jelement = cmdbClient.providerImages();
    JsonObject parsedRes = jelement.getAsJsonObject();
    JsonArray listArray = parsedRes.getAsJsonArray("rows");
    if (listArray.isJsonNull() || listArray.size() == 0) {
      return null;
    }

    ArrayList<String> imageList = new ArrayList<>();
    Iterator<JsonElement> myIter = listArray.iterator();
    while (myIter.hasNext()) {
      JsonObject currentResource = myIter.next().getAsJsonObject();
      JsonObject valueObject = currentResource.getAsJsonObject("value");
      String imageJsonId = valueObject.get("image_id").getAsString();

      String imageId = imageJsonId != null ? imageJsonId : currentResource.get("id").getAsString();
      imageList.add(imageId);
    }

    // Prepare the result
    imageList.trimToSize();
    String[] resultList = new String[imageList.size()];
    imageList.toArray(resultList);

    return resultList;
  }
}
