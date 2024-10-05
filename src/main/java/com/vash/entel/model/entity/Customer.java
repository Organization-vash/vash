package com.vash.entel.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customer")

public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "doc_number", nullable = false)
    private Long doc_number;

    @Column(name = "fullname", nullable = false)
    private String fullname;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}
