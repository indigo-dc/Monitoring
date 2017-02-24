package org.indigo.openstackprobe.openstack;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openstack4j.model.compute.Server;

import com.indigo.zabbix.utils.LifecycleCollector;
import com.indigo.zabbix.utils.PropertiesManager;
import com.indigo.zabbix.utils.PropertiesManagerTest;
import com.indigo.zabbix.utils.beans.AppOperation;

public class OpenstackCollector extends LifecycleCollector implements Runnable {

	private OpenStackClient openstackClient;
	private static final Log logger = LogFactory.getLog(OpenstackCollector.class);
	private static final String APP_NAME = "zabbix-test-app";
	private String provider;
	private static final Logger log = LogManager.getLogger(OpenstackCollector.class);
	private CmdbClient cmdbClient = new CmdbClient();
	private final ScheduledExecutorService scheduler;
	private long initialDelay = 10;
	private int numThreads = 2;
	protected OpenstackProbeResult probeResult = new OpenstackProbeResult(provider);
	private static final String INSTANCE_NAME = OpenstackProbeTags.INSTANCE_NAME;

	/*
	 * TODO: Remove when working the popertiesManager
	 */

	private static PropertiesManagerTest propertiesManagerTest = new PropertiesManagerTest(
			OpenstackProbeTags.CONFIG_FILE);

	/**
	 * Default constructor.
	 */
	/**
	 * This is the main constructor of the class, in order to retrieve the
	 * required information for carrying out the monitoring activities.
	 * 
	 * @param providerId
	 *            String with the identifier of the provider evaluated
	 * @param providerURL
	 *            String representing the Openstack API URL
	 * @param keystoneURL
	 *            String representing the Keystone API URL
	 */
	protected OpenstackCollector(String providerId, String providerUrl, String keystoneUrl) {

		scheduler = Executors.newScheduledThreadPool(numThreads);

		provider = providerId;
		openstackClient = new OpenStackClient(keystoneUrl, providerUrl, providerId);
		try {
			probeResult = openstackClient.getOpenstackMonitoringInfo();
			getResult(probeResult);

		} catch (TimeoutException te) {
			log.debug("Unable to get the information about the provider " + provider + "" + te.getMessage());
		} catch (InterruptedException ie) {
			log.error("Unable to get the information about the provider " + provider + "" + ie.getMessage());
		} catch (NullPointerException npe) {
			log.debug("Unable to collect metrics for provider " + provider);
		}
	}

	@Override
	protected String getHostName() {
		return provider;
	}

	@Override
	protected AppOperation create() {
		long time = new Date().getTime();

		CreateVmResult createdProbe = null;

		try {
			createdProbe = probeResult.getCreateVmElement();
		} catch (Exception e) {
			long respTime = new Date().getTime() - time;
			return new AppOperation(AppOperation.Operation.CREATE, false, createdProbe.getCreateVmResult(), respTime);
		}
		long respTime = new Date().getTime() - time;
		return new AppOperation(AppOperation.Operation.CREATE, true, 200, createdProbe.getCreateVmResponseTime());
	}

	@Override
	protected AppOperation retrieve() {
		long currentTime = new Date().getTime();

		InspectVmResult vmappRetrieved = null;
		try {
			vmappRetrieved = probeResult.getInspectVmElement();
			if (vmappRetrieved != null) {
				long respTime = new Date().getTime() - currentTime;
				return new AppOperation(AppOperation.Operation.RUN, true, 200, respTime);
			} else {
				long respTime = new Date().getTime() - currentTime;
				logger.error("Can't finde application by name " + APP_NAME);
				return new AppOperation(AppOperation.Operation.RUN, false, 404, respTime);
			}
		} catch (Exception e) {
			logger.error("Error getting instance ", e);
			long respTime = new Date().getTime() - currentTime;
			return new AppOperation(AppOperation.Operation.RUN, false, vmappRetrieved.getInspectVmAvailability(),
					respTime);
		}
	}

	@Override
	protected AppOperation delete() {

		long currentTime = new Date().getTime();

		DeleteVmResult deleteProbe = null;
		try {
			deleteProbe = probeResult.getDeleteVmElement();
			long respTime = new Date().getTime() - currentTime;
			return new AppOperation(AppOperation.Operation.DELETE, deleteProbe != null, 200, probeResult.getGlobalResponseTime());
		} catch (Exception e) {
			long respTime = new Date().getTime() - currentTime;
			logger.error("Error deleting application " + APP_NAME);
			return new AppOperation(AppOperation.Operation.DELETE, false, deleteProbe.getDeleteVmAvailability(),
					respTime);
		}

	}

	@Override
	public void run() {
		//all the mothods are already ran
	}

	protected void getResult(OpenstackProbeResult probeResult) {
		this.probeResult = probeResult;
	}

	@Override
	protected AppOperation clear() {

		int status = 0;
		long currentTime = new Date().getTime();
		
			try {
				List<? extends Server> instancesList = openstackClient.getServerOsList();
				for (Server server : instancesList) {
					if (server.getName().contains(INSTANCE_NAME)) {
						openstackClient.deleteVm(server.getId());
					}
				}
			} catch (Exception e) {
				if (404 == probeResult.getGlobalResult()) {
					long respTime = new Date().getTime() - currentTime;
					return new AppOperation(AppOperation.Operation.CLEAR, true, probeResult.getGlobalResult(),
							respTime);
				} else {
					long respTime = new Date().getTime() - currentTime;
					return new AppOperation(AppOperation.Operation.CLEAR, false, probeResult.getGlobalResult(),
							respTime);
				}		
		}

		long respTime = new Date().getTime() - currentTime;
		return new AppOperation(AppOperation.Operation.CLEAR, true, 200, respTime);
	}

}
