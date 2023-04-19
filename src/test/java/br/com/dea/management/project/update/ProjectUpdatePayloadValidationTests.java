package br.com.dea.management.project.update;

import br.com.dea.management.academyclass.repository.AcademyClassRepository;
import br.com.dea.management.employee.EmployeeTestUtils;
import br.com.dea.management.employee.domain.Employee;
import br.com.dea.management.employee.repository.EmployeeRepository;
import br.com.dea.management.project.ProjectTestUtils;
import br.com.dea.management.project.domain.Project;
import br.com.dea.management.project.repository.ProjectRepository;
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

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class ProjectUpdatePayloadValidationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AcademyClassRepository academyClassRepository;

    @Autowired
    private ProjectTestUtils projectTestUtils;

    @Autowired
    private EmployeeTestUtils employeeTestUtils;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @BeforeEach
    void beforeEach() {
        log.info("Before each test in " + ProjectUpdatePayloadValidationTests.class.getSimpleName());
    }

    @BeforeAll
    void beforeSuiteTest() {
        log.info("Before all tests in " + ProjectUpdatePayloadValidationTests.class.getSimpleName());
    }

    @Test
    void whenPayloadHasRequiredFieldsMissing_thenReturn400AndTheErrors() throws Exception {
        String payload = "{}";
        mockMvc.perform(put("/project/1")
                        .contentType(APPLICATION_JSON_UTF8).content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(7)))
                .andExpect(jsonPath("$.details[*].field", hasItem("startDate")))
                .andExpect(jsonPath("$.details[*].errorMessage", hasItem("Start date could not be null")))
                .andExpect(jsonPath("$.details[*].field", hasItem("endDate")))
                .andExpect(jsonPath("$.details[*].errorMessage", hasItem("End date could not be null")))
                .andExpect(jsonPath("$.details[*].field", hasItem("name")))
                .andExpect(jsonPath("$.details[*].errorMessage", hasItem("Name could not be null")))
                .andExpect(jsonPath("$.details[*].field", hasItem("client")))
                .andExpect(jsonPath("$.details[*].errorMessage", hasItem("Client could not be null")))
                .andExpect(jsonPath("$.details[*].field", hasItem("externalProductManager")))
                .andExpect(jsonPath("$.details[*].errorMessage", hasItem("External product manager could not be null")))
                .andExpect(jsonPath("$.details[*].field", hasItem("productOwnerId")))
                .andExpect(jsonPath("$.details[*].errorMessage", hasItem("Product owner could not be null")))
                .andExpect(jsonPath("$.details[*].field", hasItem("scrumMasterId")))
                .andExpect(jsonPath("$.details[*].errorMessage", hasItem("Scrum master could not be null")));
    }

    @Test
    void whenEditingAProjectThatDoesNotExists_thenReturn404() throws Exception {
        this.projectRepository.deleteAll();
        this.academyClassRepository.deleteAll();
        this.employeeRepository.deleteAll();

        this.employeeTestUtils.createFakeEmployees(2);
        Employee productOwner = this.employeeRepository.findAll().get(0);
        Employee scrumMaster = this.employeeRepository.findAll().get(1);

        String payload = "{" +
                "\"name\": \"name\"," +
                "\"client\": \"client\"," +
                "\"externalProductManager\": \"externalProductManager\"," +
                "\"startDate\": \"2023-02-27\"," +
                "\"endDate\": \"2024-02-27\"," +
                "\"productOwnerId\": " + productOwner.getId() + "," +
                "\"scrumMasterId\": " + scrumMaster.getId() +
                "}";

        mockMvc.perform(put("/project/1")
                        .contentType(APPLICATION_JSON_UTF8).content(payload))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(1)));
    }

    @Test
    void whenEditingAProjectWithAProductOwnerThatDoesNotExistsDoesNotExists_thenReturn404() throws Exception {
        this.projectRepository.deleteAll();
        this.academyClassRepository.deleteAll();
        this.employeeRepository.deleteAll();

        this.projectTestUtils.createFakeProject(1);
        Project project = this.projectRepository.findAll().get(0);

        String payload = "{" +
                "\"name\": \"name\"," +
                "\"client\": \"client\"," +
                "\"externalProductManager\": \"externalProductManager\"," +
                "\"startDate\": \"2023-02-27\"," +
                "\"endDate\": \"2024-02-27\"," +
                "\"productOwnerId\": 12345," +
                "\"scrumMasterId\": " + project.getScrumMaster().getId() +
                "}";

        mockMvc.perform(put("/project/" + project.getId())
                        .contentType(APPLICATION_JSON_UTF8).content(payload))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(1)));
    }

    @Test
    void whenEditingAProjectWithAScrumMasterThatDoesNotExistsDoesNotExists_thenReturn404() throws Exception {
        this.projectRepository.deleteAll();
        this.academyClassRepository.deleteAll();
        this.employeeRepository.deleteAll();

        this.projectTestUtils.createFakeProject(1);
        Project project = this.projectRepository.findAll().get(0);

        String payload = "{" +
                "\"name\": \"name\"," +
                "\"client\": \"client\"," +
                "\"externalProductManager\": \"externalProductManager\"," +
                "\"startDate\": \"2023-02-27\"," +
                "\"endDate\": \"2024-02-27\"," +
                "\"productOwnerId\": " + project.getProductOwner().getId() + "," +
                "\"scrumMasterId\": 12345" +
                "}";

        mockMvc.perform(put("/project/" + project.getId())
                        .contentType(APPLICATION_JSON_UTF8).content(payload))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(1)));
    }
}