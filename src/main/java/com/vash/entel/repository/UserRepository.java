package com.vash.entel.repository;

import com.vash.entel.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u " +
            "LEFT JOIN u.adviser a " +
            "LEFT JOIN u.supervisor s " +
            "WHERE (a.numberDoc = :numberDoc OR s.numberDoc = :numberDoc)")
    Optional<User> findByNumberDoc(@Param("numberDoc") Integer numberDoc);


    @Query("SELECT u FROM User u LEFT JOIN u.adviser a LEFT JOIN u.supervisor s WHERE a.module.id = :moduleId OR s.module.id = :moduleId")
    Optional<User> findByModuleId(@Param("moduleId") Integer moduleId);


    @Query("SELECT u FROM User u " +
            "LEFT JOIN u.adviser a " +
            "LEFT JOIN u.supervisor s " +
            "WHERE (a.numberDoc = :numberDoc OR s.numberDoc = :numberDoc) " +
            "OR (a.name = :name OR s.name = :name)")
    List<User> findByNumberDocOrName(@Param("numberDoc") Integer numberDoc, @Param("name") String name);

    boolean existsByEmail(String email);
    //Metodo para buscar un usuario por email (sera usado en la autenticacion)
    boolean existsByEmailAndIdNot(String email, Integer id);
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u " +
            "LEFT JOIN u.adviser a " +
            "LEFT JOIN u.supervisor s " +
            "WHERE (a.name = :name AND a.lastName = :lastName) OR (s.name = :name AND s.lastName = :lastName)")
    boolean existsByNameAndLastName(@Param("name") String name, @Param("lastName") String lastName);

}
