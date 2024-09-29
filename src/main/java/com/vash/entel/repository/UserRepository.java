package com.vash.entel.repository;

import com.vash.entel.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByNameAndLastName(String name, String lastName);
    Optional<User> findByEmail(String email);
    Optional<User> findByNumberDoc(Integer numberDoc);
    Optional<User> findByModuleId(Integer moduleId);
}
