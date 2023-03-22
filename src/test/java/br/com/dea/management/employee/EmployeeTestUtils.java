package br.com.dea.management.employee;

import br.com.dea.management.employee.domain.Employee;
import br.com.dea.management.employee.repository.EmployeeRepository;
import br.com.dea.management.position.domain.Position;
import br.com.dea.management.position.repository.PositionRepository;
import br.com.dea.management.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class EmployeeTestUtils {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PositionRepository positionRepository;

    public void createFakeEmployees(int amount) {

        Position position = Position.builder()
                .description("Dev")
                .seniority("Senior")
                .build();

        this.positionRepository.save(position);

        for (int i = 0; i < amount; i++) {
            User u = new User();
            u.setEmail("email " + i);
            u.setName("name " + i);
            u.setLinkedin("linkedin " + i);
            u.setPassword("password " + i);

            Employee employee = Employee.builder()
                    .employeeType(EmployeeType.DEVELOPER)
                    .user(u)
                    .position(position)
                    .build();

            this.employeeRepository.save(employee);

        }
    }

    public Position createFakePosition(String description, String seniority) {

        Position position = Position.builder()
                .description(description)
                .seniority(seniority)
                .build();

        return this.positionRepository.save(position);

    }

}
