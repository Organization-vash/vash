package com.vash.entel.model.entity;

import com.vash.entel.model.enums.DocumentType;
import com.vash.entel.model.enums.Role;
import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "account")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Column(name = "number_doc", nullable = false, unique = true)
    private Integer numberDoc;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    private String email;

    private String password;

    @Column(name = "username")
    private String username;


    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn(name = "module_id",  nullable = false)
    private Module module;
}
