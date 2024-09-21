package com.vash.entel.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;

    @ManyToOne
    @JoinColumn(name = "service_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_ticket_service"))
    private Service service;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_ticket_customer"))
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "module_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_ticket_module"))
    private Module module;

    @ManyToOne
    @JoinColumn(name = "agency_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_ticket_agency"))
    private  Agency agency;

    @OneToOne
    @JoinColumn(name = "attention_id", referencedColumnName ="id", foreignKey = @ForeignKey(name = "fk_ticket_attention"))
    private Attention attention;
}
