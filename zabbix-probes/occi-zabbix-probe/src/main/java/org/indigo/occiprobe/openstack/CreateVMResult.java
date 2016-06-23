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
