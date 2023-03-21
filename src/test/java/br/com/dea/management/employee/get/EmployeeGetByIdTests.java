package br.com.dea.management.employee.get;

import br.com.dea.management.employee.EmployeeTestUtils;
import br.com.dea.management.employee.domain.Employee;
import br.com.dea.management.employee.repository.EmployeeRepository;
import br.com.dea.management.position.repository.PositionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class EmployeeGetByIdTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeTestUtils employeeTestUtils;

    @Autowired
    private PositionRepository positionRepository;

    @BeforeEach
    void beforeEach() {
        log.info("Before each test in " + EmployeeGetByIdTests.class.getSimpleName());
    }

    @BeforeAll
    void beforeSuiteTest() {
        log.info("Before all tests in " + EmployeeGetByIdTests.class.getSimpleName());
    }

    @Test
    void whenRequestingAnExistentEmployeeById_thenReturnTheEmployeeSuccessfully() throws Exception {
        this.employeeRepository.deleteAll();
        this.positionRepository.deleteAll();

        this.employeeTestUtils.createFakeEmployees(10);

        Employee employee = this.employeeRepository.findAll().get(0);

        mockMvc.perform(get("/employee/" + employee.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(employee.getUser().getName())))
                .andExpect(jsonPath("$.email", is(employee.getUser().getEmail())))
                .andExpect(jsonPath("$.linkedin", is(employee.getUser().getLinkedin())))
                .andExpect(jsonPath("$.employeeType", is(employee.getEmployeeType().name())))
                .andExpect(jsonPath("$.position.description", is(employee.getPosition().getDescription())))
                .andExpect(jsonPath("$.position.seniority", is(employee.getPosition().getSeniority())));

    }

    @Test
    void whenRequestingByIdAndIdIsNotANumber_thenReturnTheBadRequestError() throws Exception {

        mockMvc.perform(get("/employee/xx"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(1)));
    }

    @Test
    void whenRequestingAnNonExistentEmployeeById_thenReturnTheNotFoundError() throws Exception {

        mockMvc.perform(get("/employee/5000"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(1)));

    }

}
