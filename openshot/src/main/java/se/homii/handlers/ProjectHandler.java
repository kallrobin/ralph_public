package se.homii.handlers;

import org.apache.commons.io.FileUtils;
import se.homii.ClientHelper;
import se.homii.models.request.Export;
import se.homii.models.request.Project;
import se.homii.models.response.ExportResponse;
import se.homii.models.response.ProjectResponse;

import javax.inject.Inject;
import java.io.IOException;

public class ProjectHandler {

  @Inject
  private ClientHelper clientHelper;


  public ProjectResponse initializeProject(String projectName) {

    if (projectName == null) {
      projectName = "test123";
    }

    // here i make the project that holds all the clips
    Project project = Project.builder()
        .name(projectName)
        .json("{}")
        .build();

    ProjectResponse projectResponse = clientHelper.buildClientWithHeader()
        .buildPost(clientHelper.buildEntity(project))
        .invoke()
        .readEntity(ProjectResponse.class);

    clientHelper.setProjectUrl(projectResponse.getUrl());

    return projectResponse;
  }

  public ExportResponse initializeProjectExport() {

    // Here i export the project with the set settings for audio and video

    Export export = Export.builder()
        .exportType("video")
        .videoFormat("mp4")
        .videoCodec("libx264")
        .videoBitrate(15000000)
        .audioCodec("libmp3lame")
        .audioBitrate(1920000)
        .startFrame(1)
        .endFrame(0)
        .project(clientHelper.getProjectUrl())
        .webhook("http://89.253.94.163/webhook") // TODO Acutal webhook
        .status("pending")
        .json("{}")
        .build();

    return clientHelper.buildClientWithHeader("exports/", null)
        .buildPost(clientHelper.buildEntity(export))
        .invoke()
        .readEntity(ExportResponse.class);
  }

  public void loadTemplateProject() {

    String jsonString = null;
    try {
      jsonString = FileUtils.readFileToString(new java.io.File(
              "/Users/robin.granstrom/Development/Homer/GIT/ralph/openshot/src/main/resources/TestyProject.json"),
          "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
    }
    clientHelper.buildClientWithHeader("/load", null)
        .buildPost(clientHelper.buildEntity(jsonString))
        .invoke();

  }
}
