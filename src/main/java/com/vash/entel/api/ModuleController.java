package com.vash.entel.api;

import com.vash.entel.dto.ModuleDTO;
import com.vash.entel.mapper.ModuleMapper;
import com.vash.entel.model.entity.Module;
import com.vash.entel.model.enums.ModuleStatus;
import com.vash.entel.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/modules")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ModuleMapper moduleMapper;

    // Endpoint para verificar si el módulo puede aceptar tickets (cuando está en estado ACTIVE)
    @GetMapping("/accept-ticket/{moduleId}")
    public ResponseEntity<Map<String, Object>> acceptTicket(@PathVariable Integer moduleId) {
        Module module = moduleService.findById(moduleId);
        Map<String, Object> response = new HashMap<>();
        response.put("moduleId", moduleId);
        response.put("status", module.getModuleStatus().name());

        if (module.getModuleStatus() == ModuleStatus.INACTIVE) {
            response.put("message", "El módulo está inactivo y no puede aceptar tickets.");
        } else if (moduleService.canAcceptTicket(module)) {
            response.put("message", "El módulo puede aceptar tickets.");
        } else if (module.getModuleStatus() == ModuleStatus.RECESS) {
            response.put("message", "El módulo está en receso y no puede aceptar tickets en este momento.");
        } else {
            response.put("message", "El módulo se ha desactivado automáticamente.");
        }

        return ResponseEntity.ok(response);
    }

    // Endpoint para cambiar el estado del módulo usando un body JSON
    @PutMapping("/change-status")
public ResponseEntity<Map<String, Object>> changeModuleStatus(@RequestBody ModuleDTO moduleDTO) {
    Module module = moduleService.findById(moduleDTO.getId());
    Map<String, Object> response = new HashMap<>();
    response.put("moduleId", moduleDTO.getId());
    response.put("userId", moduleDTO.getUserId());    // Incluir UserID en la respuesta
    response.put("userName", moduleDTO.getUserName()); // Incluir nombre del usuario en la respuesta

    if (module == null) {
        response.put("message", "El módulo no existe.");
        return ResponseEntity.badRequest().body(response);
    }

    if (moduleDTO.getModuleStatus() == ModuleStatus.RECESS) {
        if (!moduleService.canActivateRecess()) {
            response.put("message", "No puedes activar el estado de RECESS fuera del rango permitido (12:00 a 15:00).");
            return ResponseEntity.badRequest().body(response);
        }
        moduleService.activateRecess(module);
        response.put("message", "El módulo ha sido cambiado a RECESS por una hora y media.");
    } else if (moduleDTO.getModuleStatus() == ModuleStatus.INACTIVE) {
        response.put("message", moduleService.deactivateModule(module));
    } else {
        module.setModuleStatus(moduleDTO.getModuleStatus());
        moduleService.save(module);
        response.put("message", "El estado del módulo ha sido cambiado a " + moduleDTO.getModuleStatus().name());
    }
    response.put("status", module.getModuleStatus().name());

    return ResponseEntity.ok(response);
}


    // Endpoint para crear un nuevo módulo
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createModule(@RequestBody ModuleDTO moduleDTO) {
        Map<String, Object> response = new HashMap<>();
        response.put("moduleId", moduleDTO.getId());

        if (moduleService.existsById(moduleDTO.getId())) {
            response.put("message", "El ID del módulo ya existe. Por favor, elige otro ID.");
            return ResponseEntity.badRequest().body(response);
        }

        Module module = moduleMapper.toEntity(moduleDTO);

        if (module.getCreatedAt() == null) {
            module.setCreatedAt(LocalDateTime.now());
        }
        module.setUpdatedAt(LocalDateTime.now());
        module.setModuleStatus(ModuleStatus.INACTIVE);
        moduleService.save(module);

        response.put("message", "Módulo creado con éxito con estado: " + module.getModuleStatus().name());
        response.put("status", module.getModuleStatus().name());

        return ResponseEntity.ok(response);
    }

    // Obtener todos los módulos
    @GetMapping("/all")
    public ResponseEntity<Iterable<Module>> getAllModules() {
        return ResponseEntity.ok(moduleService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Module> findById(@PathVariable("id") Integer id){
        Module module = moduleService.findById(id);
        return new ResponseEntity<Module>(module, HttpStatus.OK);
    }
}
