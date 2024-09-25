package com.vash.entel.api;

import com.vash.entel.model.entity.Module;
import com.vash.entel.model.enums.ModuleStatus;
import com.vash.entel.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/modules")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;


    @GetMapping("/{moduleId}/accept-ticket")
    public String acceptTicket(@PathVariable Integer moduleId) {
        Module module = moduleService.findById(moduleId);

        
        if (module.getModuleStatus() == ModuleStatus.INACTIVE) {
            return "El módulo está inactivo y no puede aceptar tickets.";
        }

        if (moduleService.canAcceptTicket(module)) {
            return "El módulo puede aceptar tickets.";
        } else if (module.getModuleStatus() == ModuleStatus.RECESS) {
            return "El módulo está en receso y no puede aceptar tickets en este momento.";
        } else {
            return "El módulo se ha desactivado automáticamente por haber estado en receso por más de 15 minutos.";
        }
    }

    @PutMapping("/{moduleId}/change-status")
    public String changeModuleStatus(@PathVariable Integer moduleId, @RequestParam ModuleStatus newStatus) {
        Module module = moduleService.findById(moduleId);
        module.setModuleStatus(newStatus);
        module.setUpdatedAt(LocalDateTime.now());  
        moduleService.save(module);  

        return "El estado del módulo ha sido cambiado a " + newStatus.name();
    }
}
