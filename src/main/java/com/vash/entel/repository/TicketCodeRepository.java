package com.vash.entel.repository;

import com.vash.entel.model.entity.Agency;
import com.vash.entel.model.entity.Service;
import com.vash.entel.model.entity.Ticket_code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketCodeRepository extends JpaRepository<Ticket_code, Integer> {
    Ticket_code findTopByServiceAndAgencyOrderByCreatedDesc(Service service, Agency agency);
    Optional<Object> findById(Optional<Integer> lastAcceptedTicketId);

    @Query("SELECT t FROM Ticket_code t " +
           "WHERE t.module.id = :moduleId AND t.created BETWEEN :startOfDay AND :endOfDay")
    List<Ticket_code> findTicket_codesByModuleIdAndDate(Integer moduleId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
