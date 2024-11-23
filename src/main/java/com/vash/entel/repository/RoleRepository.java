package com.vash.entel.repository;

import com.vash.entel.model.entity.Role;
import com.vash.entel.model.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    //Buscar un rol por su nombre
    Optional<Role> findByName(ERole name);
}

