package com.jambit.rest;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/easterEgg")
public class EasterEgg {

  @GET
  @Path("hello")
  public Response hello(@DefaultValue("Cherry") @QueryParam("name") String name) {
    return Response.status(200).entity("hello " + name).build();
  }
}
