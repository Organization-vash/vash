package com.vash.entel.repository;

import com.vash.entel.model.entity.Attention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttentionRepository extends JpaRepository<Attention, Integer> {

}
