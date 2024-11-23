package com.vash.entel.model.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "account")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Adviser adviser;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Supervisor supervisor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName ="id" )
    private Role role;
}

