package org.indigo.openstackprobe.openstack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.indigo.zabbix.utils.ProbeThread;

public class OpenstackThread extends ProbeThread<OpenstackCollector> {

	private CmdbClient cmdbClient = new CmdbClient();
	private static final Logger log = LogManager.getLogger(OpenStackClient.class);
	String providerId;
	String providerUrl;
	String keystoneUrl;
	static int countColls = 0;
	private static List<OpenstackCollector> collectors = ProviderSearch.getCollectorResults();

	/**
	 * Constructor for initializing the the the implementation of the thread.
	 */
	protected OpenstackThread() {
		super("IaaS", "Cloud_Providers", "TemplateOpenstack");
	}

	public static void main(String[] args) {
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
