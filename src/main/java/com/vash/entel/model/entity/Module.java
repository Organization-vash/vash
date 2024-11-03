package com.vash.entel.model.entity;

import com.vash.entel.model.enums.ModuleStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "modules")
public class Module {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "module_status", nullable = false)
    private ModuleStatus moduleStatus;
 
}
