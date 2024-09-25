package com.vash.entel.service.impl;

import com.vash.entel.model.entity.Module;
import com.vash.entel.model.enums.ModuleStatus;
import com.vash.entel.repository.ModuleRepository;
import com.vash.entel.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Override
    public boolean canAcceptTicket(Module module) {
        if (module.getModuleStatus() == ModuleStatus.ACTIVE) {
            return true;
        } else if (module.getModuleStatus() == ModuleStatus.RECESS) {
            if (module.getUpdatedAt() != null) {
                if (module.getUpdatedAt().plusMinutes(15).isBefore(LocalDateTime.now())) {
                    module.setModuleStatus(ModuleStatus.INACTIVE);
                    moduleRepository.save(module);
                    return false;
                }
                return false;
            }
        }
        return false;
    }


    @Override
    public Module findById(Integer id) {
        return moduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Módulo no encontrado"));
    }

    @Override
    public void save(Module module) {
        moduleRepository.save(module);  
    }
}
