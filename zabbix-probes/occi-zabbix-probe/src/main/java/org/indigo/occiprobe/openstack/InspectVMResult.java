package org.indigo.occiprobe.openstack;

public class InspectVMResult 
{
	private int inspectVMAvailability;
	private int inspectVMResult;
	private long inspectVMResponseTime;
	
	public InspectVMResult (int availability, int result, long responseTime)
	{
		inspectVMAvailability = availability;
		inspectVMResult = result;
		inspectVMResponseTime = responseTime;
	}
	
	public int getInspectVMAvailability ()
	{
		return inspectVMAvailability;
	}
	
	public int getInspectVMResult ()
	{
		return inspectVMResult;
	}
	
	public long getInspectVMResponseTime ()
	{
		return inspectVMResponseTime;
	}
}
