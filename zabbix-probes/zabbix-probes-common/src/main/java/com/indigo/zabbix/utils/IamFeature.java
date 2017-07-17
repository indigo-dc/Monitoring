package com.indigo.zabbix.utils;
import com.indigo.zabbix.utils.PropertiesManager;

public class IamFeature {
	private String iamLocation;
	private String IamUsername;
	private String IamPassword;

	public String getIamLocation() {
		return iamLocation;
	}

	public void setIamLocation(String iamLocation) {
		this.iamLocation = iamLocation;
	}

	public String getIamUsername() {
		return IamUsername;
	}

	public void setIamUsername(String iamUsername) {
		IamUsername = iamUsername;
	}

	public String getIamPassword() {
		return IamPassword;
	}

	public void setIamPassword(String iamPassword) {
		IamPassword = iamPassword;
	}

}
