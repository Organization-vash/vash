package com.vash.entel.model.entity;

import com.vash.entel.model.enums.AttentionStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "derivate")
public class Derivate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "ticket_code", nullable = false)
    private Ticket_code ticketCode;

    @Enumerated(EnumType.STRING)
    private AttentionStatus attentionStatus;

    @ManyToOne
    @JoinColumn(name = "customer")
    private Customer customer;
}
