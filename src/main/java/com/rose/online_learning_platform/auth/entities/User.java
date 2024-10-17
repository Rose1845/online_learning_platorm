package com.rose.online_learning_platform.auth.entities;
import com.rose.online_learning_platform.instructor.entity.Instructor;
import com.rose.online_learning_platform.student.entity.Student;
import de.huxhorn.sulky.ulid.ULID;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Slf4j
@Table(name= "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {
    @Id
    private String id;

    @Column(nullable = false)
    @NotBlank(message = "Full name is required")
    @NotNull
    private String fullName;

    @Column(nullable = false, unique = true)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @NotNull
    private String email;

    @Column(nullable = false)
    @NotNull
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100 , message = "Password is not strong")
    private String password;

    @Column(nullable = false)
    private Boolean  acceptTerms = true;

    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isActive;

    @JoinColumn
    @ManyToOne
    private AuthProvider provider;

    @ManyToMany(fetch = FetchType.EAGER,cascade ={CascadeType.MERGE,CascadeType.REFRESH})
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
    @OneToOne(mappedBy = "user")
    private Student student;
    @OneToOne(mappedBy = "user")
    private Instructor instructor;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @PrePersist
    public void prePersist() {
        this.isActive = true;
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.id = new ULID().nextULID().toLowerCase(Locale.ROOT);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var userRoles = this.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toString()))
                .toList();
        log.info("User roles: {}", userRoles);
        return userRoles;
    }

    @Override
    @Cacheable(value = "user", key = "#authService")
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }


}
