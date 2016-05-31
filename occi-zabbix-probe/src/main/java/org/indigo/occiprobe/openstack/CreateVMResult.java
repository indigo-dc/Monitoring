package org.indigo.occiprobe.openstack;

public class CreateVMResult 
{
	private int createVMAvailability;
	private int createVMResult;
	private long createVMResponseTime;
	private String vmId;
	
	public CreateVMResult (int availability, int result, long responseTime, String id)
	{
		createVMAvailability = availability;
		createVMResult = result;
		createVMResponseTime = responseTime;
		vmId = id;
	}
	
	public int getCreateVMAvailability ()
	{
		return createVMAvailability;
	}
	
	public int getCreateVMResult ()
	{
		return createVMResult;
	}
	
	public long getCreateVMResponseTime ()
	{
		return createVMResponseTime;
	}
	
	public String getVmId()
	{
		return vmId;
	}
}
