package br.com.dea.management.student.domain;

import br.com.dea.management.user.domain.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String university;

    @Column
    private String graduation;

    @Column
    private LocalDate finishDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
