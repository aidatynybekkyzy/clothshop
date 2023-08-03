package com.aidatynybekkyzy.clothshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends AbstractEntity<Long> implements UserDetails {
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
    @Transient // чтобы не сохранять это поле в бд (не сериализовать)
    @JsonIgnore
    private String confirmPassword;
    @Column(name = "phone", length = 20, nullable = false, unique = true)
    private String phone;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Order> orders = new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @JsonIgnore
    private Set<Role> role = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        HashSet<GrantedAuthority> authorities = new HashSet<>(role.size());
        for (Role role : role)
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }
    public void add(Order order) {

        if(order != null) {
            orders.add(order);
            order.setUser(this);
        }
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
