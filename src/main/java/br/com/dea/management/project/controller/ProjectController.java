package br.com.dea.management.project.controller;

import br.com.dea.management.project.domain.Project;
import br.com.dea.management.project.dto.CreateProjectRequestDto;
import br.com.dea.management.project.dto.ProjectDto;
import br.com.dea.management.project.dto.UpdateProjectRequestDto;
import br.com.dea.management.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    @Operation(summary = "Load the list of projects paginated.")
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

        log.info(String.format("Projects loaded with success : Project : %s : pageSize", projects.getContent().size()));

        return projects;
    }

    @Operation(summary = "Load project by ID.")
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

        log.info(String.format("Project loaded with success : Project : %s", project));

        return project;
    }

    @Operation(summary = "Create a new project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Payload not valid"),
            @ApiResponse(responseCode = "500", description = "Error creating project"),
            @ApiResponse(responseCode = "404", description = "Instructor for the project not found"),
    })
    @PostMapping("/project")
    public void createProject(@Valid @RequestBody CreateProjectRequestDto createProjectRequestDto) {
        log.info(String.format("Creating Project with : Payload : %s", createProjectRequestDto));

        Project project = projectService.createProject(createProjectRequestDto);

        log.info(String.format("Project created with success : id : %s", project.getId()));
    }

    @Operation(summary = "Delete a Project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Payload not valid"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Error deleting project"),
    })

    @DeleteMapping("/project/{id}")
    public void deleteProject(@PathVariable Long id) {
        log.info(String.format("Deleting project with : id : %s", id));

        projectService.deleteProject(id);

        log.info(String.format("Project deleted with success : id : %s", id));
    }
    @Operation(summary = "Update a Project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Payload not valid"),
            @ApiResponse(responseCode = "404", description = "Project or employee not found"),
            @ApiResponse(responseCode = "500", description = "Error updating project"),
    })

    @PutMapping("/project/{id}")
    public void updateProject(@PathVariable Long id, @Valid @RequestBody UpdateProjectRequestDto updateProjectRequestDto) {
        log.info(String.format("Updating Project with : Payload : %s", updateProjectRequestDto));

        Project project = projectService.updateProject(id, updateProjectRequestDto);

        log.info(String.format("Project updated with success : id : %s", project.getId()));
    }
}