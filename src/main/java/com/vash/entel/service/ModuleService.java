package com.vash.entel.service;

import com.vash.entel.model.entity.Module;
import com.vash.entel.model.enums.ModuleStatus;

import java.util.List;

public interface ModuleService {
    boolean canAcceptTicket(Module module);  // Verificar si el módulo puede aceptar tickets
    Module findById(Integer id);             // Buscar módulo por ID
    void save(Module module);                // Guardar o actualizar el módulo
    List<Module> getAll();                   // Obtener todos los módulos
    void activateRecess(Module module);      // Activar el estado RECESS y limitar a 1.5 horas
    boolean canActivateRecess();             // Verificar si se puede activar el estado RECESS
    String deactivateModule(Module module);  // Desactivar el módulo con doble confirmación
}
