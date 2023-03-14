package br.com.dea.management.student.get;

import br.com.dea.management.student.domain.Student;
import br.com.dea.management.student.repository.StudentRepository;
import br.com.dea.management.user.domain.User;
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
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class StudentGetByIdTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void beforeEach() {
        log.info("Before each test in " + StudentGetByIdTests.class.getSimpleName());
    }

    @BeforeAll
    void beforeSuiteTest() {
        log.info("Before all tests in " + StudentGetByIdTests.class.getSimpleName());
    }

    @Test
    void whenRequestingAnExistentStudentById_thenReturnTheStudentSuccessfully() throws Exception {
        this.studentRepository.deleteAll();
        this.createFakeStudents(10);

        Student student = this.studentRepository.findAll().get(0);

        mockMvc.perform(get("/student/" + student.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(student.getUser().getName())))
                .andExpect(jsonPath("$.email", is(student.getUser().getEmail())))
                .andExpect(jsonPath("$.linkedin", is(student.getUser().getLinkedin())))
                .andExpect(jsonPath("$.university", is(student.getUniversity())))
                .andExpect(jsonPath("$.graduation", is(student.getGraduation())));

    }

    @Test
    void whenRequestingByIdAndIdIsNotANumber_thenReturnTheBadRequestError() throws Exception {

        mockMvc.perform(get("/student/xx"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(1)));
    }

    @Test
    void whenRequestingAnNonExistentStudentById_thenReturnTheNotFoundError() throws Exception {

        mockMvc.perform(get("/student/5000"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(1)));
    }

    private void createFakeStudents(int amount) {
        for (int i = 0; i < amount; i++) {
            User u = new User();
            u.setEmail("email " + i);
            u.setName("name " + i);
            u.setLinkedin("linkedin " + i);
            u.setPassword("pwd " + i);

            Student student = Student.builder()
                    .university("UNI " + i)
                    .graduation("Grad " + i)
                    .finishDate(LocalDate.now())
                    .user(u)
                    .id(Long.parseLong(String.valueOf(i)))
                    .build();

            this.studentRepository.save(student);
        }
        List<Student> s = this.studentRepository.findAll();
    }

}
