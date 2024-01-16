package com.JacobArthurs.ExpenseTracker.model;

import com.JacobArthurs.ExpenseTracker.enumerator.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "expense_tracker_user")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 200)
    private String password;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "email", nullable = false, length = 200, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private UserRole role = UserRole.DEFAULT;

    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate;

    @Column(name = "last_updated_date", nullable = false)
    private Timestamp lastUpdatedDate;

    public User(String username, String password, String name, String email, UserRole role, Timestamp createdDate, Timestamp lastUpdatedDate) {
        this.username = username;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.getRole().name()));
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
