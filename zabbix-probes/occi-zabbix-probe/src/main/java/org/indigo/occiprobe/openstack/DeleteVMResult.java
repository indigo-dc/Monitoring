package org.indigo.occiprobe.openstack;

public class DeleteVMResult 
{
	private int deleteVMAvailability;
	private int deleteVMResult;
	private long deleteVMResponseTime;
	
	public DeleteVMResult (int availability, int result, long responseTime)
	{
		deleteVMAvailability = availability;
		deleteVMResult = result;
		deleteVMResponseTime = responseTime;
	}
	
	public int getDeleteVMAvailability ()
	{
		return deleteVMAvailability;
	}
	
	public int getDeleteVMResult ()
	{
		return deleteVMResult;
	}
	
	public long getDeleteVMResponseTime ()
	{
		return deleteVMResponseTime;
	}
	
}
