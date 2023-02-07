package br.com.dea.management.user.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dea_user")
@NamedQuery(name = "myQuery", query = "SELECT u FROM User u where u.name = :name")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String linkedin;
}
