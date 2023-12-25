package LonaShop.model;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")

public class User {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    @Email
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    @Column
    private String address;

    @Column
    private String age;

    @Column
    private String gender;

    @Column (nullable = false)
    private boolean activeStatus = true;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    private List<Role> roles = new ArrayList<>();
}
