package com.vash.entel.api;

import com.vash.entel.model.entity.Module;
import com.vash.entel.model.enums.ModuleStatus;
import com.vash.entel.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/modules")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    // Endpoint para verificar si el módulo puede aceptar tickets (cuando está en estado ACTIVE)
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
            return "El módulo se ha desactivado automáticamente.";
        }
    }

    // Endpoint para cambiar el estado del módulo
    @PutMapping("/{moduleId}/change-status")
    public String changeModuleStatus(@PathVariable Integer moduleId, @RequestParam ModuleStatus newStatus) {
        Module module = moduleService.findById(moduleId);

        if (newStatus == ModuleStatus.RECESS) {
            if (!moduleService.canActivateRecess()) {
                return "No puedes activar el estado de RECESS fuera del rango permitido (12:00 a 15:00).";
            }
            moduleService.activateRecess(module);
            return "El módulo ha sido cambiado a RECESS por una hora y media.";
        } else if (newStatus == ModuleStatus.INACTIVE) {
            // Manejo de la doble confirmación
            return moduleService.deactivateModule(module);
        } else {
            module.setModuleStatus(newStatus);
            moduleService.save(module);
            return "El estado del módulo ha sido cambiado a " + newStatus.name();
        }
    }

    // Endpoint para crear un nuevo módulo
    @PostMapping("/create")
    public String createModule(@RequestBody Module module) {
        // Inicializar el campo createdAt si no se ha inicializado
        if (module.getCreatedAt() == null) {
            module.setCreatedAt(LocalDateTime.now());
        }
        // Inicializar el campo updatedAt con la fecha y hora actuales
        module.setUpdatedAt(LocalDateTime.now());
        // El estado inicial siempre será INACTIVE
        module.setModuleStatus(ModuleStatus.INACTIVE);
        moduleService.save(module);
        return "Módulo creado con éxito con estado: " + module.getModuleStatus();
    }

    // Obtener todos los módulos
    @GetMapping("/all")
    public Iterable<Module> getAllModules() {
        return moduleService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Module> findById(@PathVariable("id") Integer id){
        Module module = moduleService.findById(id);
        return new ResponseEntity<Module>(module, HttpStatus.OK);
    }
}
