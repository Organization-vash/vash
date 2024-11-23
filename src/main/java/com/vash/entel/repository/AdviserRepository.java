package com.vash.entel.repository;

import com.vash.entel.model.entity.Adviser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdviserRepository extends JpaRepository<Adviser, Integer> {
    Optional<Adviser> findByNameAndLastName(String name, String lastName);

    boolean existsByName(String name);

    //Metodo para verificar si ya existe un creador con el mismo nombre y apellido, excepto el usuario actual
    boolean existsByNameAndLastNameAndUserIdNot(String name, String lastname, InternalError userId);

    Adviser findByUserId(Integer userId);

    Optional<Adviser> findByNumberDoc(Integer numberDoc);
}
