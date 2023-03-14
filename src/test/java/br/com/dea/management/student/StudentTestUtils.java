package br.com.dea.management.student;

import br.com.dea.management.student.domain.Student;
import br.com.dea.management.student.repository.StudentRepository;
import br.com.dea.management.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class StudentTestUtils {

    @Autowired
    private StudentRepository studentRepository;

    public void createFakeStudents(int amount) {
        for (int i = 0; i < amount; i++) {
            User u = new User();
            u.setEmail("email " + i);
            u.setName("name " + i);
            u.setLinkedin("linkedin " + i);
            u.setPassword("password " + i);

            Student student = Student.builder()
                    .university("university " + i)
                    .graduation("graduation " + i)
                    .finishDate(LocalDate.now())
                    .user(u)
                    .build();

            this.studentRepository.save(student);
        }
    }

}