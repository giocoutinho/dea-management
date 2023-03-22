package br.com.dea.management.employee.controller;

import br.com.dea.management.employee.domain.Employee;
import br.com.dea.management.employee.dto.CreateEmployeeRequestDto;
import br.com.dea.management.employee.dto.EmployeeDto;
import br.com.dea.management.employee.dto.UpdateEmployeeRequestDto;
import br.com.dea.management.employee.service.EmployeeService;
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
@Tag(name = "Employee", description = "The Employee API")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @Operation(summary = "Load the list of employees paginated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Page or Page Size params not valid"),
            @ApiResponse(responseCode = "500", description = "Error fetching employee list"),
    })
    @GetMapping("/employee")
    public Page<EmployeeDto> getEmployees(@RequestParam(required = true) Integer page,
                                          @RequestParam(required = true) Integer pageSize) {

        log.info(String.format("Fetching employees : page : %s : pageSize", page, pageSize));

        Page<Employee> employeesPaged = this.employeeService.findAllEmployeesPaginated(page, pageSize);
        Page<EmployeeDto> employees = employeesPaged.map(employee -> EmployeeDto.fromEmployee(employee));

        log.info(String.format("Employees loaded successfully : Employees : %s : pageSize", employees.getContent()));

        return employees;

    }

    @Operation(summary = "Load the employee by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Employee Id invalid"),
            @ApiResponse(responseCode = "404", description = "Employee Not found"),
            @ApiResponse(responseCode = "500", description = "Error fetching employee list"),
    })
    @GetMapping("/employee/{id}")
    public EmployeeDto getEmployees(@PathVariable Long id) {

        log.info(String.format("Fetching employee by id : Id : %s", id));

        EmployeeDto employee = EmployeeDto.fromEmployee(this.employeeService.findEmployeeById(id));

        log.info(String.format("Employee loaded successfully : Employee : %s", employee));

        return employee;

    }

    @Operation(summary = "Create a new Employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Payload not valid"),
            @ApiResponse(responseCode = "500", description = "Error creating employee"),
    })
    @PostMapping("/employee")
    public void createEmployee(@Valid @RequestBody CreateEmployeeRequestDto createEmployeeRequestDto) {
        log.info(String.format("Creating Employee : Payload : %s", createEmployeeRequestDto));

        Employee employee = employeeService.createEmployee(createEmployeeRequestDto);

        log.info(String.format("Employee created successfully : id : %s", employee.getId()));
    }

    @Operation(summary = "Update a Employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Payload not valid"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "500", description = "Error updating employee"),
    })
    @PutMapping("/employee/{employeeId}")
    public void updateEmployee(@PathVariable Long employeeId, @Valid @RequestBody UpdateEmployeeRequestDto updateEmployeeRequestDto) {
        log.info(String.format("Updating Employee : Payload : %s", updateEmployeeRequestDto));

        Employee employee = employeeService.updateEmployee(employeeId, updateEmployeeRequestDto);

        log.info(String.format("Employee updated successfully : id : %s", employee.getId()));
    }

    @Operation(summary = "Delete a Employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Payload not valid"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "500", description = "Error deleting employee"),
    })
    @DeleteMapping("/employee/{employeeId}")
    public void deleteEmployee(@PathVariable Long employeeId) {
        log.info(String.format("Removing Employee : Id : %s", employeeId));

        employeeService.deleteEmployee(employeeId);

        log.info(String.format("Employee removed successfully : id : %s", employeeId));
    }

}
