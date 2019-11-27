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
  public Response getAvg(@DefaultValue("4") @QueryParam("t") String time)
      throws IOException, SQLException {
    MoodMeterAverageService moodMeterAverageService = MoodMeterAverageService.getInstance();
    moodMeterAverageService.setTime(Float.parseFloat(time));
    Float data = moodMeterAverageService.run();
    return Response.status(200).entity(data).build();
  }

  @GET
  @Path("distro")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getDistribution(@DefaultValue("4") @QueryParam("t") String time)
      throws SQLException {
    MoodMeterDistributionService moodMeterDistributionService =
        MoodMeterDistributionService.getInstance();
    moodMeterDistributionService.setTime(Float.parseFloat(time));
    return Response.status(200).entity(moodMeterDistributionService.run()).build();
  }

  @POST
  @Path("create")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  public Response returnData(String vote) throws SQLException {
    DatabaseConnection db = DatabaseConnection.getInstance();
    MoodEntry input = db.writeMoodEntry(new MoodEntry(0, Integer.parseInt(vote), 123));
    System.out.println("New Vote: " + input.vote);
    return Response.status(201).entity(input).build();
  }
}
