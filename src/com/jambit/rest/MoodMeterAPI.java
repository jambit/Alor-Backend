package com.jambit.rest;

import com.jambit.services.moodmeter.MoodMeterAverageService;
import com.jambit.services.moodmeter.MoodMeterDistributionService;
import java.io.IOException;
import java.sql.SQLException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/MoodMeter")
public class MoodMeterAPI {

  @GET
  @Path("avg")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAvg(@DefaultValue("4") @QueryParam("t") String time) {
    MoodMeterAverageService moodMeterAverageService = MoodMeterAverageService.getInstance();
    moodMeterAverageService.setTime(Float.parseFloat(time));
    return Response.status(200).entity(moodMeterAverageService.run()).build();
  }

  @GET
  @Path("distro")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getDistribution(@DefaultValue("4") @QueryParam("t") String time) {
    MoodMeterDistributionService moodMeterDistributionService =
        MoodMeterDistributionService.getInstance();
    moodMeterDistributionService.setTime(Float.parseFloat(time));

    try {
      return Response.status(200).entity(moodMeterDistributionService.run()).build();
    } catch (IOException | SQLException e) {
      e.printStackTrace();
      return Response.status(404).entity(e).build();
    }
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
