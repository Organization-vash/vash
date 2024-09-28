package com.vash.entel.service.impl;

import com.vash.entel.model.entity.Module;
import com.vash.entel.model.enums.ModuleStatus;
import com.vash.entel.repository.ModuleRepository;
import com.vash.entel.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        moduleRepository.save(module);
    }

    @Override
    public List<Module> getAll() {
        return moduleRepository.findAll();
    }

    @Override
    public boolean canActivateRecess() {
        LocalTime now = LocalTime.now();
        return now.isAfter(LocalTime.of(12, 0)) && now.isBefore(LocalTime.of(15, 0));
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
        }, 90*60 * 1000);  // 1hr y media
    }

    @Override
    public String deactivateModule(Module module) {
        if (!module.isConfirmDeactivation()) {
            // Primera solicitud: solicitar confirmación
            module.setConfirmDeactivation(true);  // Cambiar la bandera
            save(module);  // Guardar el cambio en la base de datos
            return "¿Seguro que quiere desactivar?";
        } else {
            // Segunda solicitud: desactivar el módulo
            module.setModuleStatus(ModuleStatus.INACTIVE);
            module.setUpdatedAt(LocalDateTime.now());
            module.setConfirmDeactivation(false);  // Reiniciar la bandera de confirmación
            save(module);
            return "Módulo desactivado.";
        }
    }
}
