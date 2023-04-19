package br.com.dea.management.project.update;

import br.com.dea.management.academyclass.repository.AcademyClassRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class ProjectUpdateSuccessCaseTests {
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

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @BeforeEach
    void beforeEach() {
        log.info("Before each test in " + ProjectUpdateSuccessCaseTests.class.getSimpleName());
    }

    @BeforeAll
    void beforeSuiteTest() {
        log.info("Before all tests in " + ProjectUpdateSuccessCaseTests.class.getSimpleName());
    }

    @Test
    void whenRequestingProjectUpdateWithAValidPayload_thenUpdateProjectSuccessfully() throws Exception {
        this.academyClassRepository.deleteAll();
        this.projectRepository.deleteAll();
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
                "\"scrumMasterId\": " + project.getScrumMaster().getId() +
                "}";

        mockMvc.perform(put("/project/" + project.getId())
                        .contentType(APPLICATION_JSON_UTF8).content(payload))
                .andExpect(status().isOk());

        project = this.projectRepository.findAll().get(0);

        assertThat(project.getName()).isEqualTo("name");
        assertThat(project.getClient()).isEqualTo("client");
        assertThat(project.getStartDate().toString()).isEqualTo("2023-02-27");
        assertThat(project.getEndDate().toString()).isEqualTo("2024-02-27");
        assertThat(project.getProductOwner().getId()).isEqualTo(project.getProductOwner().getId());
        assertThat(project.getScrumMaster().getId()).isEqualTo(project.getScrumMaster().getId());
        assertThat(project.getExternalProductManager()).isEqualTo("externalProductManager");
    }
}