package org.indigo.occiprobe.openstack;

public class OCCIProbeResult 
{

	private int globalAvailability;
	private int globalResult;
	private long globalResponseTime;
		
	private CreateVMResult createVMElement;
	private InspectVMResult inspectVMElement;
	private DeleteVMResult deleteVMElement;
	
	public OCCIProbeResult ()
	{}
	
	public void addGlobalInfo (int availability, int result, long responseTime)
	{
		globalAvailability = availability;
		globalResult = result;
		globalResponseTime = responseTime;
	}
	
	public void addCreateVMInfo (CreateVMResult result)
	{
		createVMElement = result;
	}
	
	public void addInspectVMInfo (InspectVMResult result)
	{
		inspectVMElement = result;
	}
	
	public void addDeleteVMInfo (DeleteVMResult result)
	{
		deleteVMElement = result;
	}
	
	public int getGlobalAvailability ()
	{
		return globalAvailability;
	}
	
	public int getGlobalResult ()
	{
		return globalResult;
	}
	
	public long getGlobalResponseTime ()
	{
		return globalResponseTime;
	}
	
	public CreateVMResult getCreateVMElement()
	{
		return createVMElement;
	}
	
	public InspectVMResult getInspectVMElement()
	{
		return inspectVMElement;
	}
	
	public DeleteVMResult getDeleteVMElement()
	{
		return deleteVMElement;
	}
}
