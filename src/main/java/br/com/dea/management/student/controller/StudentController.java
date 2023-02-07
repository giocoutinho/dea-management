package br.com.dea.management.student.controller;

import br.com.dea.management.student.domain.Student;
import br.com.dea.management.student.dto.StudentDto;
import br.com.dea.management.student.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class StudentController {

    @Autowired
    StudentService studentService;

    @RequestMapping(value = "/student/all", method = RequestMethod.GET)
    public List<Student> getStudentsAllRaw() {
        return this.studentService.findAllStudents();
    }

    @RequestMapping(value = "/student/without-pagination", method = RequestMethod.GET)
    public List<StudentDto> getStudentsWithOutPagination() {
        List<Student> students = this.studentService.findAllStudents();
        return StudentDto.fromStudents(students);
    }

    @GetMapping("/student")
    public Page<StudentDto> getStudents(@RequestParam(required = true) Integer page,
                                        @RequestParam(required = true) Integer pageSize) {

        log.info(String.format("Fetching students : page : %s : pageSize", page, pageSize));

        Page<Student> studentsPaged = this.studentService.findAllStudentsPaginated(page, pageSize);
        Page<StudentDto> students = studentsPaged.map(student -> StudentDto.fromStudent(student));

        log.info(String.format("Students loaded successfully : Students : %s : pageSize", students.getContent()));

        return students;

    }

    @GetMapping("/student/{id}")
    public StudentDto getStudents(@PathVariable Long id) {

        log.info(String.format("Fetching student by id : Id : %s", id));

        StudentDto student = StudentDto.fromStudent(this.studentService.findStudentById(id));

        log.info(String.format("Student loaded successfully : Student : %s", student));

        return student;

    }

}
