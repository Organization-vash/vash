package com.vash.entel.dto;

import com.vash.entel.model.enums.ModuleStatus;
import lombok.Data;

@Data
public class ModuleDTO {
    private Integer id;
    private ModuleStatus moduleStatus;
    private Integer userId;   // Agregar campo para UserID
    private String userName;  // Agregar campo para el nombre del usuario
}
