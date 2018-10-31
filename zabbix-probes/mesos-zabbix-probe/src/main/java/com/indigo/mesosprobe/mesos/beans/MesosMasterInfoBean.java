package com.indigo.mesosprobe.mesos.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jose on 22/09/16.
 */
public class MesosMasterInfoBean extends MesosClientRequest {

  public class MasterInfoType {
    private String hostname;

    public String getHostname() {
      return hostname;
    }

    public void setHostname(String hostname) {
      this.hostname = hostname;
    }
  }

  public class GetMasterType {
    @SerializedName("master_info")
    private MasterInfoType masterInfo;

    public MasterInfoType getMasterInfo() {
      return masterInfo;
    }

    public void setMasterInfo(MasterInfoType masterInfo) {
      this.masterInfo = masterInfo;
    }
  }

  public MesosMasterInfoBean() {
    super(RequestType.GET_MASTER);
  }

  @SerializedName("get_master")
  private GetMasterType getMaster;

  public GetMasterType getGetMaster() {
    return getMaster;
  }

  public void setGetMaster(GetMasterType getMaster) {
    this.getMaster = getMaster;
  }
}
