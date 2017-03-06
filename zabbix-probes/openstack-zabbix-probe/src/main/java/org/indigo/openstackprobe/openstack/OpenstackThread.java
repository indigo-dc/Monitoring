package org.indigo.openstackprobe.openstack;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.indigo.zabbix.utils.ProbeThread;

public class OpenstackThread extends ProbeThread<OpenstackCollector> {

	private static final Logger log = LogManager.getLogger(OpenStackClient.class);
	String providerId;
	String providerUrl;
	String keystoneUrl;
	static int countColls = 0;
	private static List<OpenstackCollector> collectors;

	/**
	 * Constructor for initializing the the the implementation of the thread.
	 */
	protected OpenstackThread() {
		super("IaaS", "Cloud_Providers", "TemplateOpenstack");
	}

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
		return null;
	}
}
