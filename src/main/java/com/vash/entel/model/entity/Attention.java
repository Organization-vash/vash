package com.vash.entel.model.entity;

import com.vash.entel.model.enums.AttentionStatus;
import com.vash.entel.model.enums.SuccessStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "attentions")
public class Attention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalTime hour;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;

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
}
