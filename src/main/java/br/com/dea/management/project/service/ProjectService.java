package br.com.dea.management.project.service;

import br.com.dea.management.employee.domain.Employee;
import br.com.dea.management.employee.repository.EmployeeRepository;
import br.com.dea.management.exceptions.NotFoundException;
import br.com.dea.management.project.domain.Project;
import br.com.dea.management.project.dto.CreateProjectRequestDto;
import br.com.dea.management.project.dto.UpdateProjectRequestDto;
import br.com.dea.management.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Page<Project> findAllProjectPaginated(Integer page, Integer pageSize) {
        return this.projectRepository.findAllPaginated(PageRequest.of(page, pageSize, Sort.by("startDate").ascending()));
    }
    public Project findProjectById(Long id) {
        return this.projectRepository.findById(id).orElseThrow(() -> new NotFoundException(Project.class, id));
    }

    public Project createProject(CreateProjectRequestDto createProjectRequestDto) {
        Employee productOwner = this.employeeRepository.findById(createProjectRequestDto.getProductOwnerId())
                .orElseThrow(() -> new NotFoundException(Project.class, createProjectRequestDto.getProductOwnerId()));
        Employee scrumMaster = this.employeeRepository.findById(createProjectRequestDto.getScrumMasterId())
                .orElseThrow(() -> new NotFoundException(Project.class, createProjectRequestDto.getScrumMasterId()));

        Project project = Project.builder()
                .name(createProjectRequestDto.getName())
                .client(createProjectRequestDto.getClient())
                .externalProductManager(createProjectRequestDto.getExternalProductManager())
                .startDate(createProjectRequestDto.getStartDate())
                .endDate(createProjectRequestDto.getEndDate())
                .productOwner(productOwner)
                .scrumMaster(scrumMaster)
                .build();

        return this.projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        Project project = this.projectRepository.findById(id).orElseThrow(() -> new NotFoundException(Project.class, id));
        this.projectRepository.delete(project);
    }

    public Project updateProject(Long id, UpdateProjectRequestDto updateProjectRequestDto) {
        Project project = this.projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Project.class, id));
        Employee productOwner = this.employeeRepository.findById(updateProjectRequestDto.getProductOwnerId())
                .orElseThrow(() -> new NotFoundException(Employee.class, updateProjectRequestDto.getProductOwnerId()));
        Employee scrumMaster = this.employeeRepository.findById(updateProjectRequestDto.getScrumMasterId())
                .orElseThrow(() -> new NotFoundException(Employee.class, updateProjectRequestDto.getProductOwnerId()));

        project.setName(updateProjectRequestDto.getName());
        project.setClient(updateProjectRequestDto.getClient());
        project.setExternalProductManager(updateProjectRequestDto.getExternalProductManager());
        project.setStartDate(updateProjectRequestDto.getStartDate());
        project.setEndDate(updateProjectRequestDto.getEndDate());
        project.setProductOwner(productOwner);
        project.setScrumMaster(scrumMaster);

        return this.projectRepository.save(project);
    }
}