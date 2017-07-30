package org.indigo.openstackprobe.openstack;

import com.indigo.zabbix.utils.ProbeThread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


/**
 * 
 * @author Reply Santer.
 *
 */
public class OpenstackThread extends ProbeThread<OpenstackCollector> {

  private static final Logger log = LogManager.getLogger(OpenStackClient.class);

  static int countColls = 0;
  private static List<OpenstackCollector> collectors;

  /**
   * Constructor for test purposes.
   */
  protected OpenstackThread(List<OpenstackCollector> collectorsMocked) {
    super("IaaS", "Cloud_Providers", "TemplateOpenstack");
    collectors = collectorsMocked;
  }

  /**
   * Constructor for initializing the the the implementation of the thread.
   */
  protected OpenstackThread() {
    super("IaaS", "Cloud_Providers", "TemplateOpenstack");
  }

  /**
   * The very entry point of the code.
   * @param args args
   */
  public static void main(String[] args) {
    collectors = ProviderSearch.getCollectorResults();
    for (countColls = 0; countColls < collectors.size(); countColls++) {
      new OpenstackThread().run(OpenstackProbeTags.CONFIG_FILE);
    }
  }

  @Override
  protected OpenstackCollector createCollector() {

    while (countColls < collectors.size()) {
      log.info("Collecting metrics from: " + collectors.get(countColls).provider);
      return collectors.get(countColls);
    }
    log.info("No provider for collecting metrics from");
    return null;
  }
}
