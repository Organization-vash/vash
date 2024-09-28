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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "module_status", nullable = false)
    private ModuleStatus moduleStatus;

    // Nueva bandera para manejar la confirmación de desactivación
    @Column(name = "confirm_deactivation", nullable = false)
    private boolean confirmDeactivation = false;
}
