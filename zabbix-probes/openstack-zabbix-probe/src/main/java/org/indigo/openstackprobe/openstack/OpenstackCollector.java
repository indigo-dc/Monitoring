package org.indigo.openstackprobe.openstack;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openstack4j.api.exceptions.ClientResponseException;
import org.openstack4j.api.exceptions.ConnectionException;
import org.openstack4j.api.exceptions.ResponseException;
import org.openstack4j.model.compute.Server;

import com.indigo.zabbix.utils.LifecycleCollector;
import com.indigo.zabbix.utils.beans.AppOperation;

/**
 * 
 * @author Reply Santer. 
 *         Collects the information about cloud providers to be
 *         metered and then implements the LifeCycleCollector by managing the
 *         metrics.
 */
public class OpenstackCollector extends LifecycleCollector {

    // private OpenStackClient openstackClient;
	public String provider;
	public String keystoneEndpoint;

	private static final Logger log = LogManager.getLogger(OpenstackCollector.class);
	protected OpenstackProbeResult probeResult;
	// = new OpenstackProbeResult(provider);
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

		openstackClient = new OpenStackClient(keystoneEndpoint, provider);

		keystoneEndpoint = keystoneUrl;
	}

	/**
	 * Launches the creation, cancellation of VMs for each f the providers taken
	 * in cosideration.
	 * 
	 * @return OpenstackProbeResult
	 */
	private OpenstackProbeResult getOSProbeResult() {
		if (probeResult == null)
			try {
				probeResult = openstackClient.getOpenstackMonitoringInfo();
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
			log.info("Collected creation meters in: "
					+ String.valueOf(Math.abs(createdProbe.getCreateVmResponseTime())));
		} catch (ConnectionException ce) {
			return getResultForConnectionException(ce, currentTime);
		} catch (Exception ex) {
			long respTime = new Date().getTime() - currentTime;
			return new AppOperation(AppOperation.Operation.CREATE, false, 404, respTime);
		}
		return new AppOperation(AppOperation.Operation.CREATE, true, 200,
				Math.abs(createdProbe.getCreateVmResponseTime()));
	}

	@Override
	protected AppOperation retrieve() {

		long currentTime = new Date().getTime();
		InspectVmResult vmappRetrieved = null;
		try {
			vmappRetrieved = getOSProbeResult().getInspectVmElement();
			if (vmappRetrieved != null) {
				log.info(
						"Collected inspection meters in: " + String.valueOf(vmappRetrieved.getInspectVmResponseTime()));
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
			log.info("Collected delete meters in: " + String.valueOf(deleteProbe.getDeleteVmResponseTime()));
			return new AppOperation(AppOperation.Operation.DELETE, deleteProbe != null, 200,
					deleteProbe.getDeleteVmResponseTime());
		} catch (ConnectionException ce) {
			return getResultForConnectionException(ce, currentTime);
		} catch (Exception exc) {
			long respTime = new Date().getTime() - currentTime;
			log.error("Error deleting server instance ");
			return new AppOperation(AppOperation.Operation.DELETE, false, 404, respTime);
		}

	}

	@Override
	protected AppOperation clear() {
		long currentTime = new Date().getTime();
		try {
			List<? extends Server> instancesList = getOSProbeResult().getOsInstanceList();
			for (Server server : instancesList) {
				if (server.getName().contains(INSTANCE_NAME)) {
					log.debug(
							"Still the instance " + server.getName() + " is listed or is about to be totally removed");

					long respTime = new Date().getTime() - currentTime;
					log.info("Collected clear statistics in: " + String.valueOf(respTime));
					return new AppOperation(AppOperation.Operation.CLEAR, true, 204, respTime);
				}
			}
		} catch (ConnectionException ce) {
			return getResultForConnectionException(ce, currentTime);
		}
		long respTime = new Date().getTime() - currentTime;
		return new AppOperation(AppOperation.Operation.CLEAR, true, 200, respTime);
	}

}
