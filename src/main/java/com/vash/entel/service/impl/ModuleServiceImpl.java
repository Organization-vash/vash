package com.vash.entel.service.impl;

import com.vash.entel.model.entity.Module;
import com.vash.entel.model.enums.ModuleStatus;
import com.vash.entel.repository.ModuleRepository;
import com.vash.entel.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Override
    public boolean canAcceptTicket(Module module) {
        return module.getModuleStatus() == ModuleStatus.ACTIVE;
    }

    @Override
    public Module findById(Integer id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));
    }

    @Override
    public void save(Module module) {
        if (module.getId() == null) {
            // Solo verificamos si el ID existe cuando se está creando un nuevo módulo.
            if (moduleRepository.existsById(module.getId())) {
                throw new RuntimeException("El ID del módulo ya existe. Por favor, elige otro ID.");
            }
        }
        moduleRepository.save(module);
    }

    @Override
    public List<Module> getAll() {
        return moduleRepository.findAll();
    }

    @Override
    public boolean canActivateRecess() {
        LocalTime now = LocalTime.now();
        return now.isAfter(LocalTime.of(12, 0)) && now.isBefore(LocalTime.of(19, 0));
    }

    @Override
    public void activateRecess(Module module) {
        module.setModuleStatus(ModuleStatus.RECESS);
        module.setUpdatedAt(LocalDateTime.now());
        save(module);

        // Programar el regreso automático al estado ACTIVE después de 1 hora y media
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                module.setModuleStatus(ModuleStatus.ACTIVE);
                save(module);
                System.out.println("El módulo ha regresado al estado ACTIVE");
            }
        }, 1*60 * 1000);  // 1hr y media
    }

    @Override
    public String deactivateModule(Module module) {
            module.setModuleStatus(ModuleStatus.INACTIVE);
            module.setUpdatedAt(LocalDateTime.now());
            save(module);
            return "Módulo desactivado.";
        

    }
    @Override
    public boolean existsById(Integer id) {
        return moduleRepository.existsById(id);
    }

}
