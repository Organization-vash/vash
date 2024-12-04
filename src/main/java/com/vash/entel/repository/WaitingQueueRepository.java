package com.vash.entel.repository;

import com.vash.entel.model.entity.Ticket_code;
import com.vash.entel.model.entity.WaitingQueue;
import com.vash.entel.model.enums.AttentionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
@Repository
public interface WaitingQueueRepository extends JpaRepository<WaitingQueue, Integer> {

    Optional<WaitingQueue> findByTicketCode(Ticket_code ticketCode);
    List<WaitingQueue> findByAttentionStatus(AttentionStatus status);
    WaitingQueue findFirstByAttentionStatusOrderByCreatedAtAsc(AttentionStatus status);
}
