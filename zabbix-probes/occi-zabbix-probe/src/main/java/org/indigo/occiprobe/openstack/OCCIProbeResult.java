/**
Copyright 2016 ATOS SPAIN S.A.

Licensed under the Apache License, Version 2.0 (the License);
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Authors Contact:
Francisco Javier Nieto. Atos Research and Innovation, Atos SPAIN SA
@email francisco.nieto@atos.net
**/

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
	
	public OCCIProbeResult (int availability, int result, long responseTime)
	{
		globalAvailability = availability;
		globalResult = result;
		globalResponseTime = responseTime;
	}
	
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
