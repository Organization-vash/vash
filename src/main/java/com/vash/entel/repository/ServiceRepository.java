package com.vash.entel.repository;

import com.vash.entel.model.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository <Service, Integer> {
    Optional<Service> findByNameAndType(String name, String type);
}
