package org.indigo.openstackprobe.openstack;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openstack4j.api.exceptions.ConnectionException;
import org.openstack4j.api.exceptions.ResponseException;
import org.openstack4j.model.compute.Server;

import com.indigo.zabbix.utils.LifecycleCollector;
import com.indigo.zabbix.utils.beans.AppOperation;

public class OpenstackCollector extends LifecycleCollector {

	private OpenStackClient openstackClient = new OpenStackClient("","");
	public String provider;
	public String providerEndpoint;
	public String keystoneEndpoint;
	
	private static final Logger log = LogManager.getLogger(OpenstackCollector.class);
	protected OpenstackProbeResult probeResult;
	private static final String INSTANCE_NAME = OpenstackProbeTags.INSTANCE_NAME;

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
	protected OpenstackCollector(String providerId, String keystoneUrl){
		provider = providerId;
		keystoneEndpoint = keystoneUrl;
		openstackClient = new OpenStackClient(keystoneUrl, providerId);
		try {
			probeResult = openstackClient.getOpenstackMonitoringInfo();
			getResult(probeResult);
		}catch(ResponseException re){
			log.debug("Unable to connect to openstack " + re.getMessage());
		} catch (TimeoutException | InterruptedException te) {
			log.debug("Unable to get the information about the provider " + provider + "" + te);
			// Terminate thread manager
		}
		keystoneEndpoint = keystoneUrl;
	}
	
	@Override
	protected String getHostName() {
		return provider;
	}

	@Override
	protected AppOperation create() {
		long currentTime = new Date().getTime();
		CreateVmResult createdProbe = null;

		try {
			createdProbe = probeResult.getCreateVmElement();
		} catch (ConnectionException ce) {
			return getResultForConnectionException(ce, currentTime);
		} catch (Exception ex) {
			long respTime = new Date().getTime() - currentTime;
			return new AppOperation(AppOperation.Operation.CREATE, false, createdProbe.getCreateVmResult(), respTime);
		}
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
				return new AppOperation(AppOperation.Operation.RUN, true, 200,
						vmappRetrieved.getInspectVmResponseTime());
			} else {
				long respTime = new Date().getTime() - currentTime;
				log.error("Can't find server instance");
				return new AppOperation(AppOperation.Operation.RUN, false, 404, respTime);
			}
		} catch (ConnectionException ce) {
			return getResultForConnectionException(ce, currentTime);
		} catch (Exception exc) {
			log.error("Error getting instance ", exc);
			long respTime = new Date().getTime() - currentTime;
			return new AppOperation(AppOperation.Operation.RUN, false, probeResult.getGlobalAvailability(), respTime);
		}
	}

	/**
	 * Manages the reponse in case of connection exception to a specific
	 * provider.
	 * 
	 * @param ce
	 *            ConnectionException
	 * @param currentTime
	 * @return AppOperation
	 */
	private AppOperation getResultForConnectionException(ConnectionException ce, long currentTime) {
		long respTime = new Date().getTime() - currentTime;
		log.debug("Unable to connect to provider: " + provider + " " + ce.getMessage());
		return new AppOperation(AppOperation.Operation.DELETE, false, 404, respTime);
	}

	@Override
	protected AppOperation delete() {

		long currentTime = new Date().getTime();

		DeleteVmResult deleteProbe = null;
		try {
			deleteProbe = probeResult.getDeleteVmElement();
			return new AppOperation(AppOperation.Operation.DELETE, deleteProbe != null, 200,
					probeResult.getGlobalResponseTime());
		} catch (ConnectionException ce) {
			return getResultForConnectionException(ce, currentTime);
		} catch (Exception exc) {
			long respTime = new Date().getTime() - currentTime;
			log.error("Error deleting server instance ");
			return new AppOperation(AppOperation.Operation.DELETE, false, 404, respTime);
		}

	}


	protected void getResult(OpenstackProbeResult probeResult) {
		this.probeResult = probeResult;
	}

	@Override
	protected AppOperation clear() {

		long currentTime = new Date().getTime();
		try {
			List<? extends Server> instancesList = openstackClient.getServerOsList();
			for (Server server : instancesList) {
				if (server.getName().contains(INSTANCE_NAME)) {
					openstackClient.deleteVm(server.getId());
				}
			}
		} catch (InterruptedException | TimeoutException exc) {
			log.debug("Unable to get information from provider: " + provider + exc.getMessage());
			long respTime = new Date().getTime() - currentTime;
			return new AppOperation(AppOperation.Operation.CLEAR, true, 404, respTime);
		} catch (ConnectionException ce) {
			return getResultForConnectionException(ce, currentTime);
		}

		long respTime = new Date().getTime() - currentTime;
		return new AppOperation(AppOperation.Operation.CLEAR, true, 200, respTime);
	}

}
