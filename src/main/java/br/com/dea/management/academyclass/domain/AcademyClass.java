package br.com.dea.management.academyclass.domain;

import br.com.dea.management.academyclass.ClassType;
import br.com.dea.management.employee.domain.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AcademyClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private ClassType classType;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Employee instructor;

}
