package com.indigo.mesosprobe;

import com.google.gson.JsonObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by jose on 12/08/16.
 */
@Path("metrics")
public interface MesosClient {

  @GET
  @Path("snapshot")
  @Produces(MediaType.APPLICATION_JSON)
  JsonObject getMetrics();

}
