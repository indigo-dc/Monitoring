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
import org.openstack4j.api.OSClient;
import org.openstack4j.api.exceptions.ConnectionException;
import org.openstack4j.api.exceptions.ResponseException;
import org.openstack4j.model.compute.Server;

import com.indigo.zabbix.utils.LifecycleCollector;
import com.indigo.zabbix.utils.beans.AppOperation;

public class OpenstackCollector extends LifecycleCollector {

	// private OpenStackClient openstackClient;
	public String provider;
	public String keystoneEndpoint;

	private static final Logger log = LogManager.getLogger(OpenstackCollector.class);
	protected OpenstackProbeResult probeResult; 
//	= new OpenstackProbeResult(provider);
	private static final String INSTANCE_NAME = OpenstackProbeTags.INSTANCE_NAME;
	OpenStackClient openstackClient;
	OpenstackComponent openstackComponent = new OpenstackComponent();

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
	protected OpenstackCollector(String providerId, String keystoneUrl) throws IllegalArgumentException {
		provider = providerId;
		keystoneEndpoint = keystoneUrl;

		try {
			openstackClient = new OpenStackClient(keystoneEndpoint, provider);
//			probeResult = openstackClient.getOpenstackMonitoringInfo();
//			getResult(probeResult);
			// probeResult = openstackClient.getOpenstackMonitoringInfo();
			// getResult(probeResult);
		} catch (ResponseException re) {
			log.debug("Unable to connect to openstack " + re.getMessage());
//		} catch (TimeoutException | InterruptedException te) {
//			log.debug("Unable to get the information about the provider " + provider + "" + te);
		}
		keystoneEndpoint = keystoneUrl;
	}
	
//	private OpenstackCollector(){
//		try {
//			OpenstackProbeResult probeResult = openstackClient.getOpenstackMonitoringInfo();
//			getResult(probeResult);
//		} catch (TimeoutException | InterruptedException te) {
//			log.debug("Unable to get the information about the provider " + provider + "" + te);
//		}
//	}

	private OpenstackProbeResult getOSProbeResult() {
		if (probeResult==null) 
		try {
			probeResult = openstackClient.getOpenstackMonitoringInfo();
//			getResult(probeResult);
			return probeResult;
		} catch (TimeoutException | InterruptedException te) {
			log.debug("Unable to get the information about the provider " + provider + "" + te);
		}
		return probeResult;
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
			createdProbe = getOSProbeResult().getCreateVmElement();
		} catch (ConnectionException ce) {
			return getResultForConnectionException(ce, currentTime);
		} catch (Exception ex) {
			long respTime = new Date().getTime() - currentTime;
			return new AppOperation(AppOperation.Operation.CREATE, false, 404, respTime);
		}
		return new AppOperation(AppOperation.Operation.CREATE, true, 200, createdProbe.getCreateVmResponseTime());
	}

	@Override
	protected AppOperation retrieve() {

		long currentTime = new Date().getTime();
		InspectVmResult vmappRetrieved = null;
		try {
			vmappRetrieved = getOSProbeResult().getInspectVmElement();
			if (vmappRetrieved != null) {
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
			deleteProbe = getOSProbeResult().getDeleteVmElement();
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

	protected OpenstackProbeResult getResult(OpenstackProbeResult probeResultParam) {
		this.probeResult = probeResultParam;
		return getProbe(); 
	}
	
	private OpenstackProbeResult getProbe(){
		return probeResult;
	} 

	@Override
	protected AppOperation clear() {
		long currentTime = new Date().getTime();
		try {
//			probeResult = openstackClient.getOpenstackMonitoringInfo();
//			getResult(probeResult);
			List<? extends Server> instancesList = getOSProbeResult().getOsInstanceList();
			for (Server server : instancesList) {
				if (server.getName().contains(INSTANCE_NAME)) {
					log.debug(
							"Still the instance " + server.getName() + " is listed or is about to be totally removed");

					// openstackClient.deleteVm(server.getId());
					long respTime = new Date().getTime() - currentTime;
					log.info("Collected clear parameters in: " +  String.valueOf(respTime));
					return new AppOperation(AppOperation.Operation.CLEAR, true, 204, respTime);
				}
			}
			
//		} catch (InterruptedException | TimeoutException exc) {
//			log.debug("Unable to get information from provider: " + provider + exc.getMessage());
//			long respTime = new Date().getTime() - currentTime;
//			return new AppOperation(AppOperation.Operation.CLEAR, true, 404, respTime);
		} catch (ConnectionException ce) {
			return getResultForConnectionException(ce, currentTime);
		}

		long respTime = new Date().getTime() - currentTime;
		return new AppOperation(AppOperation.Operation.CLEAR, true, 200, respTime);
	}

}
