package com.vash.entel.repository;

import com.vash.entel.model.entity.Attention;
import com.vash.entel.model.enums.AttentionStatus;
import com.vash.entel.model.enums.SuccessStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttentionRepository extends JpaRepository<Attention, Integer> {
    Optional<Attention> findBySuccessStatusAndAttentionStatus(SuccessStatus successStatus, AttentionStatus attentionStatus);
}
