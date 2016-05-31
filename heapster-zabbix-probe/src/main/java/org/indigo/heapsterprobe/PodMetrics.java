package org.indigo.heapsterprobe;

public class PodMetrics 
{
	String podName;
	int networkTxErrors;
	float networkTxErrorsRate;
	int networkRxErrors;
	float networkRxErrorsRate;
	float memoryMajorPageFaultsRate;
	float memoryPageFaultsRate;
	int uptime;
	
	
	public PodMetrics (int txErrors, float txErrorsRate, int rxErrors, float rxErrorsRate, float majorPageFaults, float pageFaults, int theUptime)
	{
		networkTxErrors = txErrors;
		networkTxErrorsRate = txErrorsRate;
		networkRxErrors = rxErrors;
		networkRxErrorsRate = rxErrorsRate;
		memoryMajorPageFaultsRate = majorPageFaults;
		memoryPageFaultsRate = pageFaults;
		uptime = theUptime;
	}
	
	public String getPodName()
	{
		return podName;
	}
	
	public int getNetworkTxErrors()
	{
		return networkTxErrors;
	}
	
	public float getNetworkTxErrorsRate()
	{
		return networkTxErrorsRate;
	}
	
	public int getNetworkRxErrors()
	{
		return networkRxErrors;
	}
	
	public float getNetworkRxErrorsRate ()
	{
		return networkRxErrorsRate;
	}
	
	public float getMemoryMajorPageFaultsRate ()
	{
		return memoryMajorPageFaultsRate;
	}
	
	public float getMemoryPageFaultsRate ()
	{
		return memoryPageFaultsRate;
	}
	
	public int getUptime ()
	{
		return uptime;
	}
	
}
