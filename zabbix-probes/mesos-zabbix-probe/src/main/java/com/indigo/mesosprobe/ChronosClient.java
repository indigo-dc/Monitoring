package com.indigo.mesosprobe;

import com.google.gson.JsonObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by jose on 18/08/16.
 */
@Path("scheduler")
public interface ChronosClient {

  @GET
  @Path("jobs")
  @Produces(MediaType.APPLICATION_JSON)
  JsonObject listJobs();

}
