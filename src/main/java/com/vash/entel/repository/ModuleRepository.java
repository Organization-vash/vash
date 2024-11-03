package com.vash.entel.repository;

import com.vash.entel.model.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {
    boolean existsById(Integer id);  
}
