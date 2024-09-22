package com.vash.entel.repository;

import com.vash.entel.model.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository <Service, Integer> {
}
