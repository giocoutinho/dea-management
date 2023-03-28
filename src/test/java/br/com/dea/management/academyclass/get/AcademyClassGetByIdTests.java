package br.com.dea.management.academyclass.get;

import br.com.dea.management.academyclass.AcademyTestUtils;
import br.com.dea.management.academyclass.ClassType;
import br.com.dea.management.academyclass.domain.AcademyClass;
import br.com.dea.management.academyclass.repository.AcademyClassRepository;
import br.com.dea.management.employee.repository.EmployeeRepository;
import br.com.dea.management.student.repository.StudentRepository;
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

import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class AcademyClassGetByIdTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AcademyClassRepository academyClassRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AcademyTestUtils academyClassTestUtils;

    @BeforeEach
    void beforeEach() {
        log.info("Before each test in " + AcademyClassGetByIdTests.class.getSimpleName());
    }

    @BeforeAll
    void beforeSuiteTest() {
        log.info("Before all tests in " + AcademyClassGetByIdTests.class.getSimpleName());
    }

    @Test
    void whenRequestingAnExistentAcademyClassById_thenReturnTheAcademyClassSuccessfully() throws Exception {
        this.academyClassRepository.deleteAll();
        this.studentRepository.deleteAll();
        this.employeeRepository.deleteAll();

        LocalDate startDate = LocalDate.of(2023, Month.JANUARY, 1);
        LocalDate endDate = LocalDate.of(2024, Month.DECEMBER, 20);
        this.academyClassTestUtils.createFakeClass(1, startDate, endDate, ClassType.DEVELOPER);

        AcademyClass academyClass = this.academyClassRepository.findAll().get(0);

        mockMvc.perform(get("/academy-class/" + academyClass.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.startDate", is("2023-01-01")))
                .andExpect(jsonPath("$.endDate", is("2024-12-20")))
                .andExpect(jsonPath("$.instructor.name", is("name 0")));

    }

    @Test
    void whenRequestingByIdAndIdIsNotANumber_thenReturnTheBadRequestError() throws Exception {

        mockMvc.perform(get("/academy-class/xx"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(1)));
    }

    @Test
    void whenRequestingAnNonExistentAcademyClassById_thenReturnTheNotFoundError() throws Exception {

        mockMvc.perform(get("/academy-class/5000"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(1)));

    }

}
