package br.com.dea.management.project;

import br.com.dea.management.employee.EmployeeTestUtils;
import br.com.dea.management.employee.domain.Employee;
import br.com.dea.management.employee.repository.EmployeeRepository;
import br.com.dea.management.project.domain.Project;
import br.com.dea.management.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ProjectTestUtils {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeTestUtils employeeTestUtils;

    public void createFakeProject(int amount) {
        this.employeeTestUtils.createFakeEmployees(2);

        Employee projectOwner = this.employeeRepository.findAll().get(0);
        Employee scrumMaster = this.employeeRepository.findAll().get(1);

        for (int i = 0; i < amount; i++) {
            Project project = new Project();
            project.setName("name " + i);
            project.setClient("client " + i);
            project.setStartDate(LocalDate.of(2023,4, 12));
            project.setEndDate(LocalDate.of(2024,4, 12));
            project.setProductOwner(projectOwner);
            project.setScrumMaster(scrumMaster);
            project.setExternalProductManager("external product manager " + i);

            this.projectRepository.save(project);
        }
    }
}