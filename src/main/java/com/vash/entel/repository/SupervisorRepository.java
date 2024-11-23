package com.vash.entel.repository;

import com.vash.entel.model.entity.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupervisorRepository extends JpaRepository<Supervisor, Integer> {
    Optional<Supervisor> findByNameAndLastName(String name, String lastname);

    boolean existsByName(String name);

    //Metodo para verificar si ya existe un creador con el mismo nombre y apellido, excepto el usuario actual
    boolean existsByNameAndLastNameAndUserIdNot(String name, String lastname, InternalError userId);

    Supervisor findByUserId(Integer userId);
    Optional<Supervisor> findByNumberDoc(Integer numberDoc);
}

