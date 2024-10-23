package com.vash.entel.repository;

import com.vash.entel.model.entity.WaitingQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingQueueRepository extends JpaRepository<WaitingQueue, Integer> {
}
