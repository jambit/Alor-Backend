package com.jambit.rest;

import com.jambit.DatabaseConnection;
import com.jambit.domain.MoodEntry;
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
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createMoodEntry(String vote) throws IOException, SQLException {
    MoodEntry write;
    try {
      write = new MoodEntry(Integer.parseInt(vote));
    } catch (NumberFormatException e) {
      return Response.status(422).entity(e.getMessage()).build();
    }
    MoodEntry result = DatabaseConnection.getInstance().writeMoodEntry(write);
    return Response.status(201).entity(result).build();
  }
}
