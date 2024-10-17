package com.rose.online_learning_platform.auth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "auth_tokens")
@EntityListeners(AuditingEntityListener.class)
public class AuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auth_token_seq")
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(min = 64, max = 512)
    private String token;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User userId;

    @Column(nullable = false)
    private Date issuedAt;

    // Expiry date in milliseconds
    private Date expiryDate;

    private Boolean isExpired;
    private Boolean isRevoked;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

}
