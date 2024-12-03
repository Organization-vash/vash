package com.vash.entel.model.entity;

import com.vash.entel.model.enums.AttentionStatus;
import com.vash.entel.model.enums.SuccessStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList; // Import necesario para inicializar la lista
import java.util.List;

@Data
@Entity
@Table(name = "attentions")
public class Attention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime time;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;

    @Column(name = "update_at")
    private LocalDateTime updated_at;

    @Enumerated(EnumType.STRING)
    private AttentionStatus attentionStatus;

    @Enumerated(EnumType.STRING)
    private SuccessStatus successStatus;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_attention_user"))
    private Customer user;

    @OneToOne
    @JoinColumn(name = "survey_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_attention_survey"))
    private Survey survey;

    @ManyToMany
    @JoinTable(
        name = "attention_services",
        joinColumns = @JoinColumn(name = "attention_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<com.vash.entel.model.entity.Service> services = new ArrayList<>(); // Inicialización de la lista
}
