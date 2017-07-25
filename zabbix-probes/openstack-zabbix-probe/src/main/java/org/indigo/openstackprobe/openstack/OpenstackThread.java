package org.indigo.openstackprobe.openstack;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.indigo.zabbix.utils.ProbeThread;

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

	public static void main(String[] args) {
		collectors = ProviderSearch.getCollectorResults();
		for (countColls = 0; countColls < collectors.size(); countColls++) {
			try{
			new OpenstackThread().run(OpenstackProbeTags.CONFIG_FILE);
			}catch(Exception exc){
				if(countColls==collectors.size()){
					log.error(exc.getMessage());
					break;
					}else continue;
				
			}
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
