package com.vash.entel.repository;

import com.vash.entel.model.entity.Agency;
import com.vash.entel.model.entity.Service;
import com.vash.entel.model.entity.Ticket_code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketCodeRepository extends JpaRepository<Ticket_code, Integer> {
    Ticket_code findTopByServiceAndAgencyOrderByCreatedDesc(Service service, Agency agency);
    Optional<Object> findById(Optional<Integer> lastAcceptedTicketId);
    Optional<Ticket_code> findTopByOrderByCreatedDesc();
}
