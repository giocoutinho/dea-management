package br.com.dea.management.academyclass.update;

import br.com.dea.management.academyclass.AcademyTestUtils;
import br.com.dea.management.academyclass.ClassType;
import br.com.dea.management.academyclass.domain.AcademyClass;
import br.com.dea.management.academyclass.repository.AcademyClassRepository;
import br.com.dea.management.employee.EmployeeTestUtils;
import br.com.dea.management.employee.EmployeeType;
import br.com.dea.management.employee.domain.Employee;
import br.com.dea.management.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AcademyClassUpdateSuccessCaseTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AcademyClassRepository academyClassRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AcademyTestUtils academyTestUtils;
    @Autowired
    private EmployeeTestUtils employeeTestUtils;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));



    @Test
    void whenRequestingAcademyClassUpdateWithAValidPayload_thenUpdateAcadmyClassSuccessfully() throws Exception {
        this.academyClassRepository.deleteAll();
        this.employeeRepository.deleteAll();

        this.academyTestUtils.createFakeClass(1, LocalDate.of(2010, 3, 7), LocalDate.of(2011, 3, 7), ClassType.DEVELOPER);
        AcademyClass academyClass = this.academyClassRepository.findAll().get(0);

        this.employeeTestUtils.createFakeEmployees(1);
        Employee instructor = this.employeeRepository.findAll().get(0);

        String payload = "{" +
                "\"startDate\": \"2023-02-27\"," +
                "\"endDate\": \"2024-02-27\"," +
                "\"classType\": \"DEVELOPER\"," +
                "\"instructorId\": " + instructor.getId() +
                "}";

        mockMvc.perform(put("/academy-class/" + academyClass.getId())
                        .contentType(APPLICATION_JSON_UTF8).content(payload))
                .andExpect(status().isOk());

        AcademyClass updatedAcademyClass = this.academyClassRepository.findAll().get(0);
        assertThat(updatedAcademyClass.getStartDate()).isEqualTo("2023-02-27");
        assertThat(updatedAcademyClass.getEndDate()).isEqualTo("2024-02-27");
        assertThat(updatedAcademyClass.getClassType()).isEqualTo(ClassType.DEVELOPER);
    }
}