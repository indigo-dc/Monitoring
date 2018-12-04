package com.indigo.mesosprobe.mesos.beans;

public class MesosClientRequest {

    public enum RequestType{
        GET_METRICS,
        GET_MASTER
    }

    private RequestType type;

    public MesosClientRequest(RequestType type) {
        this.type = type;
    }
}
