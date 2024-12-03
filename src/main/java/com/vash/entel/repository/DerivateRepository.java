package com.vash.entel.repository;

import com.vash.entel.model.entity.Derivate;
import com.vash.entel.model.enums.AttentionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DerivateRepository extends JpaRepository<Derivate, Integer> {
    Derivate findFirstByAttentionStatusOrderByCreatedAtAsc(AttentionStatus status);
}
