package com.vash.entel.model.entity;

import com.vash.entel.model.enums.AttentionStatus;
import com.vash.entel.model.enums.SuccessStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "adviser_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_attention_adviser"))
    private User adviser;
}
