package com.rose.online_learning_platform.auth.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AuthProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auth_provider_seq")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String clientId;

    private String clientSecret;

    private String redirectUri;

    @ElementCollection
    @CollectionTable(name="scopes")
    private ArrayList<String> scopes = new ArrayList<String>();

    @ElementCollection
    @CollectionTable(name="grant_types")
    private ArrayList<String> grantTypes = new ArrayList<String>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
