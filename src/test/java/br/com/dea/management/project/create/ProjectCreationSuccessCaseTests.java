package br.com.dea.management.project.create;

import br.com.dea.management.academyclass.repository.AcademyClassRepository;
import br.com.dea.management.employee.EmployeeTestUtils;
import br.com.dea.management.employee.domain.Employee;
import br.com.dea.management.employee.repository.EmployeeRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class ProjectCreationSuccessCaseTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AcademyClassRepository academyClassRepository;

    @Autowired
    private EmployeeTestUtils employeeTestUtils;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @BeforeEach
    void beforeEach() {
        log.info("Before each test in " + ProjectCreationSuccessCaseTests.class.getSimpleName());
    }

    @BeforeAll
    void beforeSuiteTest() {
        log.info("Before all tests in " + ProjectCreationSuccessCaseTests.class.getSimpleName());
    }

    @Test
    void whenRequestingProjectCreationWithAValidPayload_thenCreateAProjectSuccessfully() throws Exception {
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
        mockMvc.perform(post("/project")
                        .contentType(APPLICATION_JSON_UTF8).content(payload))
                .andExpect(status().isOk());

        Project project = this.projectRepository.findAll().get(0);

        assertThat(project.getName()).isEqualTo("name");
        assertThat(project.getClient()).isEqualTo("client");
        assertThat(project.getExternalProductManager()).isEqualTo("externalProductManager");
        assertThat(project.getStartDate()).isEqualTo("2023-02-27");
        assertThat(project.getEndDate()).isEqualTo("2024-02-27");
        assertThat(project.getProductOwner().getId()).isEqualTo(productOwner.getId());
        assertThat(project.getScrumMaster().getId()).isEqualTo(scrumMaster.getId());
    }
}