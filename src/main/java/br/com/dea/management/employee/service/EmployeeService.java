package br.com.dea.management.employee.service;

import br.com.dea.management.employee.domain.Employee;
import br.com.dea.management.employee.dto.EmployeeDto;
import br.com.dea.management.employee.dto.UpdateEmployeeRequestDto;
import br.com.dea.management.employee.dto.CreateEmployeeRequestDto;
import br.com.dea.management.employee.repository.EmployeeRepository;
import br.com.dea.management.exceptions.NotFoundException;
import br.com.dea.management.user.domain.User;
import br.com.dea.management.position.domain.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> findAllEmployees() {
        return this.employeeRepository.findAll();
    }

    public Page<Employee> findAllEmployeesPaginated(Integer page, Integer pageSize) {
        return this.employeeRepository.findAllPaginated(PageRequest.of(page, pageSize, Sort.by("user.name").ascending()));
    }

    public Employee findEmployeeById(Long id) {
        return this.employeeRepository.findById(id).orElseThrow(() -> new NotFoundException(Employee.class, id));
    }

    public Employee createEmployee(CreateEmployeeRequestDto createEmployeeRequestDto) {
        User user = User.builder()
                .name(createEmployeeRequestDto.getName())
                .email(createEmployeeRequestDto.getEmail())
                .password(createEmployeeRequestDto.getPassword())
                .linkedin(createEmployeeRequestDto.getLinkedin())
                .build();

        Employee employee = Employee.builder()
                .user(user)
                .employeeType(createEmployeeRequestDto.getEmployeeType())
                .position(createEmployeeRequestDto.getPositionDto())
                .build();

        return this.employeeRepository.save(employee);
    }

    public Employee updateStudent(Long employeeId, UpdateEmployeeRequestDto updateEmployeeRequestDto) {
        Employee employee = this.findEmployeeById(employeeId);
        User user = employee.getUser();

        user.setName(updateEmployeeRequestDto.getName());
        user.setEmail(updateEmployeeRequestDto.getEmail());
        user.setPassword(updateEmployeeRequestDto.getPassword());
        user.setLinkedin(updateEmployeeRequestDto.getLinkedin());

        employee.setUser(user);
        employee.setEmployeeType(updateEmployeeRequestDto.getEmployeeType());
        employee.setPosition(updateEmployeeRequestDto.getPositionDto());

        return this.employeeRepository.save(employee);
    }

    public void deleteEmployee(Long employeeId) {
        Employee employee = this.findEmployeeById(employeeId);
        this.employeeRepository.delete(employee);
    }

}
