package com.vash.entel.repository;

import com.vash.entel.model.entity.Derivate;
import com.vash.entel.model.enums.AttentionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DerivateRepository extends JpaRepository<Derivate, Integer> {
    Derivate findFirstByAttentionStatusOrderByCreatedAtAsc(AttentionStatus status);

    Optional<Derivate> findFirstByAttentionStatusOrderByCreatedAtDesc(AttentionStatus transferred);
}
