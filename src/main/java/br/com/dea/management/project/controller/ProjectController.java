package br.com.dea.management.project.controller;

import br.com.dea.management.project.domain.Project;
import br.com.dea.management.project.dto.ProjectDto;
import br.com.dea.management.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Tag(name = "Project", description = "The Project API")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @Operation(summary = "Load the list of project paginated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Page or Page Size params not valid"),
            @ApiResponse(responseCode = "500", description = "Error fetching academyClass list"),
    })
    @GetMapping("/project")
    public Page<ProjectDto> getProject(@RequestParam(required = true) Integer page,
                                       @RequestParam(required = true) Integer pageSize) {

        log.info(String.format("Fetching project : page : %s : pageSize", page, pageSize));

        Page<Project> projectPaged = this.projectService.findAllProjectPaginated(page, pageSize);
        Page<ProjectDto> projects = projectPaged.map(project -> ProjectDto.fromProject(project));

        log.info(String.format("Projects loaded successfully : Project : %s : pageSize", projects.getContent().size()));

        return projects;
    }

    @Operation(summary = "Load the project by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Project Id invalid"),
            @ApiResponse(responseCode = "404", description = "Project Not found"),
            @ApiResponse(responseCode = "500", description = "Error fetching academyClass list"),
    })
    @GetMapping("/project/{id}")
    public ProjectDto getProject(@PathVariable Long id) {

        log.info(String.format("Fetching project by id : Id : %s", id));

        ProjectDto project = ProjectDto.fromProject(this.projectService.findProjectById(id));

        log.info(String.format("Project loaded successfully : Project : %s", project));

        return project;
    }
}