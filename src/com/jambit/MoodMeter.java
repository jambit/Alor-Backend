package com.jambit;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/MoodMeter")
public class MoodMeter {

  @GET
  @Path("avg")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAvg(@DefaultValue("4") @QueryParam("t") String time) {

    return Response.status(200).entity(null).build();
  }

  @GET
  @Path("distro")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getDistribution(@DefaultValue("4") @QueryParam("t") String time) {
    return Response.status(200).entity(null).build();
  }

  @POST
  @Path("create")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response returnData(String track) {
    String result = track;
    return Response.status(201).entity(result).build();
  }
}
