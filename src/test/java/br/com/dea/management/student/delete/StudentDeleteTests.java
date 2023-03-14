package br.com.dea.management.student.delete;

import br.com.dea.management.student.StudentTestUtils;
import br.com.dea.management.student.domain.Student;
import br.com.dea.management.student.repository.StudentRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StudentDeleteTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentTestUtils studentTestUtils;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Test
    void whenRequestingToRemoveStudent_thenRemoveAStudentSuccessfully() throws Exception {
        this.studentRepository.deleteAll();
        this.studentTestUtils.createFakeStudents(1);

        Student student = this.studentRepository.findAll().get(0);

        mockMvc.perform(delete("/student/" + student.getId())
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        List<Student> students = this.studentRepository.findAll();

        assertThat(students.size()).isEqualTo(0);
    }

    @Test
    void whenRemovingAStudentThatDoesNotExists_thenReturn404() throws Exception {
        this.studentRepository.deleteAll();

        mockMvc.perform(delete("/student/1")
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(1)));
    }
}