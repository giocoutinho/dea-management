package br.com.dea.management.student.update;

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

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StudentUpdatePayloadValidationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Test
    void whenPayloadHasRequiredFieldsMissing_thenReturn400AndTheErrors() throws Exception {
        String payload = "{}";
        mockMvc.perform(put("/student/1")
                        .contentType(APPLICATION_JSON_UTF8).content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(3)))
                .andExpect(jsonPath("$.details[*].field", hasItem("name")))
                .andExpect(jsonPath("$.details[*].errorMessage", hasItem("Name could not be null")))
                .andExpect(jsonPath("$.details[*].field", hasItem("email")))
                .andExpect(jsonPath("$.details[*].errorMessage", hasItem("Email could not be null")))
                .andExpect(jsonPath("$.details[*].field", hasItem("password")))
                .andExpect(jsonPath("$.details[*].errorMessage", hasItem("Password could not be null")));
    }

    @Test
    void whenEditingAStudentThatDoesNotExists_thenReturn404() throws Exception {
        this.studentRepository.deleteAll();

        String payload = "{" +
                "\"name\": \"name\"," +
                "\"email\": \"email@email.com\"," +
                "\"linkedin\": \"linkedin\"," +
                "\"university\": \"university\"," +
                "\"graduation\": \"graduation\"," +
                "\"finishDate\": \"2023-02-27\"," +
                "\"password\": \"password\"" +
                "}";
        mockMvc.perform(put("/student/1")
                        .contentType(APPLICATION_JSON_UTF8).content(payload))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(1)));
    }

    @Test
    void whenPayloadHasNotValidEmail_thenReturn400AndTheErrors() throws Exception {
        String payload = "{" +
                "\"name\": \"name\"," +
                "\"email\": \"email\"," +
                "\"linkedin\": \"linkedin\"," +
                "\"university\": \"university\"," +
                "\"graduation\": \"graduation\"," +
                "\"password\": \"password\"," +
                "\"finishDate\": \"2023-02-27\"" +
                "}";
        mockMvc.perform(post("/student")
                        .contentType(APPLICATION_JSON_UTF8).content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(1)))
                .andExpect(jsonPath("$.details[*].field", hasItem("email")))
                .andExpect(jsonPath("$.details[*].errorMessage", hasItem("must be a well-formed email address")));
    }
}