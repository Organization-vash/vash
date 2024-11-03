package com.vash.entel.mapper;

import com.vash.entel.dto.ModuleDTO;
import com.vash.entel.model.entity.Module;
import org.springframework.stereotype.Component;

@Component
public class ModuleMapper {

    public Module toEntity(ModuleDTO dto) {
        Module module = new Module();
        module.setId(dto.getId());
        module.setModuleStatus(dto.getModuleStatus());
        return module;
    }

    public ModuleDTO toDTO(Module module) {
        ModuleDTO dto = new ModuleDTO();
        dto.setId(module.getId());
        dto.setModuleStatus(module.getModuleStatus());
        return dto;
    }
}
