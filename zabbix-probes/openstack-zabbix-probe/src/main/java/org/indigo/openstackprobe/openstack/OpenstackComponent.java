package org.indigo.openstackprobe.openstack;

import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;

import org.openstack4j.api.OSClient;
import org.openstack4j.api.client.IOSClientBuilder.V2;
import org.openstack4j.api.compute.ComputeService;
import org.openstack4j.api.compute.FlavorService;
import org.openstack4j.api.compute.ServerService;
import org.openstack4j.api.image.ImageService;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.identity.Token;
import org.openstack4j.model.image.Image;

// TODO: Auto-generated Javadoc
/**
 * The Class OpenstackComponent.
 */
public class OpenstackComponent {

	/** The mock client. */
	private Client mockClient;
	
	/** The os client mocked. */
	private OSClient osClientMocked;
	
	/** The mock keystone. */
	private V2 mockKeystone;
	
	/** The token moked. */
	private Token tokenMoked;
	
	/** The token Id moked. */
	private String tokenId;
	
	/** The computeservice mocked. */
	private ComputeService computeserviceMocked;
	
	/** The flavor service mocked. */
	private FlavorService flavorServiceMocked;
	
	/** The image service mocked. */
	private ImageService imageServiceMocked;
	
	/** The flavors mocked. */
	private List<Flavor> flavorsMocked;
	
	/** The images mocked. */
	private List<Image> imagesMocked;
	
	/** The mock flavor. */
	private Flavor mockFlavor;
	
	/** The mock image. */
	private Image mockImage;
	
	/** The base keystone url mocked. */
	private String baseKeystoneUrlMocked;
	
	/** The open stack user mocked. */
	private String openStackUserMocked;
	
	/** The open stack pwd mocked. */
	private String openStackPwdMocked;
	
	/** The tenant name moked. */
	private String tenantNameMoked;
	
	/** The server creation mocked. */
	private Map<Server.Status, Server> serverCreationMocked;
	
	/** The server service mocked. */
	private ServerService serverServiceMocked;
	
	/** The server mocked. */
	private Server serverMocked;
	
	/** The result map mocked. */
	private Map<String, Integer> resultMapMocked;
	
	/** The servers mocks. */
	private List<Server> serversMocks;

	/**
	 * Gets the mock client.
	 *
	 * @return the mock client
	 */
	public Client getMockClient() {
		return mockClient;
	}

	/**
	 * Sets the mock client.
	 *
	 * @param mockClient the new mock client
	 */
	public void setMockClient(Client mockClient) {
		this.mockClient = mockClient;
	}

	/**
	 * Gets the os client mocked.
	 *
	 * @return the os client mocked
	 */
	public OSClient getOsClientMocked() {
		return osClientMocked;
	}

	/**
	 * Sets the os client mocked.
	 *
	 * @param osClientMocked the new os client mocked
	 */
	public void setOsClientMocked(OSClient osClientMocked) {
		this.osClientMocked = osClientMocked;
	}

	/**
	 * Gets the mock keystone.
	 *
	 * @return the mock keystone
	 */
	public V2 getMockKeystone() {
		return mockKeystone;
	}

	/**
	 * Sets the mock keystone.
	 *
	 * @param mockKeystone the new mock keystone
	 */
	public void setMockKeystone(V2 mockKeystone) {
		this.mockKeystone = mockKeystone;
	}
	
	/**
	 * Sets the token moked.
	 *
	 * @param tokenMoked the new token moked
	 */
	public void setTokenMoked(Token tokenMoked) {
		this.tokenMoked = tokenMoked;
	}

	/**
	 * Gets the token moked.
	 *
	 * @return the token moked
	 */
	public Token getTokenMoked() {
		return tokenMoked;
	}

	/**
	 * Sets the token moked.
	 *
	 * @param tokenMoked the new token moked
	 */
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	
	
	/**
	 * Gets the token moked.
	 *
	 * @return the token moked
	 */
	public String getTokenId() {
		return tokenId;
	}

	

	/**
	 * Gets the computeservice mocked.
	 *
	 * @return the computeservice mocked
	 */
	public ComputeService getComputeserviceMocked() {
		return computeserviceMocked;
	}

	/**
	 * Sets the computeservice mocked.
	 *
	 * @param computeserviceMocked the new computeservice mocked
	 */
	public void setComputeserviceMocked(ComputeService computeserviceMocked) {
		this.computeserviceMocked = computeserviceMocked;
	}

	/**
	 * Gets the flavor service mocked.
	 *
	 * @return the flavor service mocked
	 */
	public FlavorService getFlavorServiceMocked() {
		return flavorServiceMocked;
	}

	/**
	 * Sets the flavor service mocked.
	 *
	 * @param flavorServiceMocked the new flavor service mocked
	 */
	public void setFlavorServiceMocked(FlavorService flavorServiceMocked) {
		this.flavorServiceMocked = flavorServiceMocked;
	}

	/**
	 * Gets the image service mocked.
	 *
	 * @return the image service mocked
	 */
	public ImageService getImageServiceMocked() {
		return imageServiceMocked;
	}

	/**
	 * Sets the image service mocked.
	 *
	 * @param imageServiceMocked the new image service mocked
	 */
	public void setImageServiceMocked(ImageService imageServiceMocked) {
		this.imageServiceMocked = imageServiceMocked;
	}

	/**
	 * Gets the flavors mocked.
	 *
	 * @return the flavors mocked
	 */
	public List<Flavor> getFlavorsMocked() {
		return flavorsMocked;
	}

	/**
	 * Sets the flavors mocked.
	 *
	 * @param flavorsMocked the new flavors mocked
	 */
	public void setFlavorsMocked(List<Flavor> flavorsMocked) {
		this.flavorsMocked = flavorsMocked;
	}

	/**
	 * Gets the images mocked.
	 *
	 * @return the images mocked
	 */
	public List<Image> getImagesMocked() {
		return imagesMocked;
	}

	/**
	 * Sets the images mocked.
	 *
	 * @param imagesMocked the new images mocked
	 */
	public void setImagesMocked(List<Image> imagesMocked) {
		this.imagesMocked = imagesMocked;
	}

	/**
	 * Gets the mock flavor.
	 *
	 * @return the mock flavor
	 */
	public Flavor getMockFlavor() {
		return mockFlavor;
	}

	/**
	 * Sets the mock flavor.
	 *
	 * @param mockFlavor the new mock flavor
	 */
	public void setMockFlavor(Flavor mockFlavor) {
		this.mockFlavor = mockFlavor;
	}

	/**
	 * Gets the mock image.
	 *
	 * @return the mock image
	 */
	public Image getMockImage() {
		return mockImage;
	}

	/**
	 * Sets the mock image.
	 *
	 * @param mockImage the new mock image
	 */
	public void setMockImage(Image mockImage) {
		this.mockImage = mockImage;
	}

	/**
	 * Gets the base keystone url mocked.
	 *
	 * @return the base keystone url mocked
	 */
	public String getBaseKeystoneUrlMocked() {
		return baseKeystoneUrlMocked;
	}

	/**
	 * Sets the base keystone url mocked.
	 *
	 * @param baseKeystoneUrlMocked the new base keystone url mocked
	 */
	public void setBaseKeystoneUrlMocked(String baseKeystoneUrlMocked) {
		this.baseKeystoneUrlMocked = baseKeystoneUrlMocked;
	}

	/**
	 * Gets the open stack user mocked.
	 *
	 * @return the open stack user mocked
	 */
	public String getOpenStackUserMocked() {
		return openStackUserMocked;
	}

	/**
	 * Sets the open stack user mocked.
	 *
	 * @param openStackUserMocked the new open stack user mocked
	 */
	public void setOpenStackUserMocked(String openStackUserMocked) {
		this.openStackUserMocked = openStackUserMocked;
	}

	/**
	 * Gets the open stack pwd mocked.
	 *
	 * @return the open stack pwd mocked
	 */
	public String getOpenStackPwdMocked() {
		return openStackPwdMocked;
	}

	/**
	 * Sets the open stack pwd mocked.
	 *
	 * @param openStackPwdMocked the new open stack pwd mocked
	 */
	public void setOpenStackPwdMocked(String openStackPwdMocked) {
		this.openStackPwdMocked = openStackPwdMocked;
	}

	/**
	 * Gets the tenant name moked.
	 *
	 * @return the tenant name moked
	 */
	public String getTenantNameMoked() {
		return tenantNameMoked;
	}

	/**
	 * Sets the tenant name moked.
	 *
	 * @param tenantNameMoked the new tenant name moked
	 */
	public void setTenantNameMoked(String tenantNameMoked) {
		this.tenantNameMoked = tenantNameMoked;
	}

	/**
	 * Gets the server creation mocked.
	 *
	 * @return the server creation mocked
	 */
	public Map<Server.Status, Server> getServerCreationMocked() {
		return serverCreationMocked;
	}

	/**
	 * Sets the server creation mocked.
	 *
	 * @param serverCreationMocked the server creation mocked
	 */
	public void setServerCreationMocked(Map<Server.Status, Server> serverCreationMocked) {
		this.serverCreationMocked = serverCreationMocked;
	}

	/**
	 * Gets the server service mocked.
	 *
	 * @return the server service mocked
	 */
	public ServerService getServerServiceMocked() {
		return serverServiceMocked;
	}

	/**
	 * Sets the server service mocked.
	 *
	 * @param serverServiceMocked the new server service mocked
	 */
	public void setServerServiceMocked(ServerService serverServiceMocked) {
		this.serverServiceMocked = serverServiceMocked;
	}

	/**
	 * Gets the server mocked.
	 *
	 * @return the server mocked
	 */
	public Server getServerMocked() {
		return serverMocked;
	}

	/**
	 * Sets the server mocked.
	 *
	 * @param serverMocked the new server mocked
	 */
	public void setServerMocked(Server serverMocked) {
		this.serverMocked = serverMocked;
	}

	/**
	 * Gets the result map mocked.
	 *
	 * @return the result map mocked
	 */
	public Map<String, Integer> getResultMapMocked() {
		return resultMapMocked;
	}

	/**
	 * Sets the result map mocked.
	 *
	 * @param resultMapMocked the result map mocked
	 */
	public void setResultMapMocked(Map<String, Integer> resultMapMocked) {
		this.resultMapMocked = resultMapMocked;
	}

	/**
	 * Gets the servers mocks.
	 *
	 * @return the servers mocks
	 */
	public List<Server> getServersMocks() {
		return serversMocks;
	}

	/**
	 * Sets the servers mocks.
	 *
	 * @param serversMocks the new servers mocks
	 */
	public void setServersMocks(List<Server> serversMocks) {
		this.serversMocks = serversMocks;
	}

}
