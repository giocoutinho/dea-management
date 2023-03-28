package br.com.dea.management.academyclass;

import br.com.dea.management.academyclass.ClassType;
import br.com.dea.management.academyclass.domain.AcademyClass;
import br.com.dea.management.academyclass.repository.AcademyClassRepository;
import br.com.dea.management.employee.EmployeeTestUtils;
import br.com.dea.management.employee.domain.Employee;
import br.com.dea.management.employee.repository.EmployeeRepository;
import br.com.dea.management.student.StudentTestUtils;
import br.com.dea.management.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AcademyTestUtils {

    @Autowired
    private AcademyClassRepository academyClassRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeTestUtils employeeTestUtils;

    @Autowired
    private StudentTestUtils studentTestUtils;

    public void createFakeClass(int amount, LocalDate startDate, LocalDate endDate, ClassType classType) {

        this.employeeTestUtils.createFakeEmployees(1);
        this.studentTestUtils.createFakeStudents(10);

        Employee employee = this.employeeRepository.findAll().get(0);

        for (int i = 0; i < amount; i++) {
            AcademyClass academyClass = new AcademyClass();
            academyClass.setStartDate(startDate);
            academyClass.setEndDate(endDate);
            academyClass.setClassType(classType);
            academyClass.setInstructor(employee);

            this.academyClassRepository.save(academyClass);
        }
    }

}
