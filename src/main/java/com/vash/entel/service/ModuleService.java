package com.vash.entel.service;

import com.vash.entel.model.entity.Module;

public interface ModuleService {
    boolean canAcceptTicket(Module module);  
    Module findById(Integer id);             
    void save(Module module);                
}
