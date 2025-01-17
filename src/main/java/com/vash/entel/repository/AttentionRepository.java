package com.vash.entel.repository;

import com.vash.entel.model.entity.Attention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface AttentionRepository extends JpaRepository<Attention, Integer> {
    @Query(value = "SELECT * FROM get_attention_report()", nativeQuery = true)
    List<Object[]> getAttentionReport();
}
