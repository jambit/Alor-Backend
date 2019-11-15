package com.jambit;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/mdm")
public class MoodMeter {

  @GET
  @Path("avg")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAvg(@DefaultValue("4") @QueryParam("t") String time) {
    return Response.status(200).entity(time).build();
  }

  @GET
  @Path("dist")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getDist(@DefaultValue("4") @QueryParam("t") String time) {
    return Response.status(200).entity(time).build();
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
