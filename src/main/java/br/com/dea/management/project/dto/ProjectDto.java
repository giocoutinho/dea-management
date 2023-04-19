package br.com.dea.management.project.dto;

import br.com.dea.management.employee.dto.EmployeeDto;
import br.com.dea.management.project.domain.Project;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProjectDto {

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private String client;

    private String externalProductManager;

    @JsonProperty("productOwner")
    private EmployeeDto productOwner;

    @JsonProperty("scrumMaster")
    private EmployeeDto scrumMaster;

    public static ProjectDto fromProject(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName(project.getName());
        projectDto.setStartDate(project.getStartDate());
        projectDto.setEndDate(project.getEndDate());
        projectDto.setClient(project.getClient());
        projectDto.setExternalProductManager(project.getExternalProductManager());
        projectDto.setProductOwner(EmployeeDto.fromEmployee(project.getProductOwner()));
        projectDto.setScrumMaster(EmployeeDto.fromEmployee(project.getScrumMaster()));
        return projectDto;
    }
}