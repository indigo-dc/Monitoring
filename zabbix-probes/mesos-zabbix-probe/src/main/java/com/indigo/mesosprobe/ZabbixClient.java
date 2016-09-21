package com.indigo.mesosprobe;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by jose on 16/08/16.
 */
@Path("/monitoring/adapters/zabbix/zones/indigo/types/service/groups/")
public interface ZabbixClient {

  @PUT
  @Path("Containers/hosts/{hostName}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response registerContainerHost(
      @PathParam("hostName")
      String hostName,
      ZabbixHost host
  );


  @PUT
  @Path("Pods/hosts/{hostName}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response registerPodHost(
      @PathParam("hostName")
      String hostName,
      ZabbixHost host
  );

  @GET
  @Path("Containers/hosts/{hostName}")
  @Produces(MediaType.APPLICATION_JSON)
  Response isContainerRegistered(
      @PathParam("hostName")
      String hostName
  );


  @GET
  @Path("Pods/hosts/{hostName}")
  @Produces(MediaType.APPLICATION_JSON)
  Response isPodRegistered(
      @PathParam("hostName")
      String hostName
  );
}
