package com.vash.entel.dto;

import com.vash.entel.model.enums.ModuleStatus;
import lombok.Data;

@Data
public class ModuleDTO {
    private Integer id;               // ID del módulo
    private ModuleStatus moduleStatus; // Nuevo estado
}
