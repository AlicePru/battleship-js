package lv.ctco.javaschool.auth.entity.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private Integer move = 0;
    @Enumerated(EnumType.STRING)
    private Role role;

}
