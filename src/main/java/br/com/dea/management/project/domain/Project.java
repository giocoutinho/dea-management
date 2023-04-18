package br.com.dea.management.project.domain;

import br.com.dea.management.employee.domain.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column
    private String client;

    @Column
    private String externalProductManager;

    @ManyToOne
    @JoinColumn(name = "product_owner_id")
    private Employee productOwner;

    @ManyToOne
    @JoinColumn(name = "scrum_master_id")
    private Employee scrumMaster;
}