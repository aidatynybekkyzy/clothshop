package com.aidatynybekkyzy.clothshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="users_seq_gen")
    @SequenceGenerator(name="users_seq_gen", sequenceName="users_sequence", allocationSize = 1)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;
    @Column(length = 100, nullable = false)
    private String username;

    @Size(min = 4, max = 50)
    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Size(min = 4, max = 50)
    @Column(name = "lastname", nullable = false)
    private String lastName;
    @Column(name = "email", unique = true, nullable = false)
    @Email
    @NotEmpty
    private String email;

    @Size(min = 7, max = 60, message = "{validation.user.passwordSize}")
    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;
    @Column(name = "phone", length = 20, nullable = false, unique = true)
    private String phone;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Order> orders = new HashSet<>();
}
