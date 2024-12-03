package com.vash.entel.repository;

import com.vash.entel.model.entity.Agency;
import com.vash.entel.model.entity.Service;
import com.vash.entel.model.entity.Ticket_code;
import com.vash.entel.model.enums.AttentionStatus;
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

    @Query(value = "SELECT t.code AS ticketCode, t.created AS created, " +
            "m.id AS moduleName, u.name AS advisorName, c.fullname AS customerName, " +
            "a.created_at AS attentionTime, a.attention_status AS attentionStatus, a.success_status AS successStatus, " +
            "s.value AS surveyDetails " +
            "FROM tickets_code t " +
            "JOIN modules m ON t.module_id = m.id " +
            "JOIN customer c ON t.customer_id = c.id " +
            "LEFT JOIN attentions a ON t.attention_id = a.id " +
            "LEFT JOIN account u ON a.user_id = u.id " +
            "LEFT JOIN surveys s ON a.survey_id = s.id " +
            "WHERE t.code LIKE %:code%", 
    nativeQuery = true)
    List<Object[]> findByCode(String code);

    @Query("SELECT t FROM Ticket_code t " +
           "WHERE t.module.id = :moduleId AND t.created BETWEEN :startOfDay AND :endOfDay")
    List<Ticket_code> findTicket_codesByModuleIdAndDate(Integer moduleId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    Optional<Ticket_code> findTopByOrderByCreatedDesc();

    Optional<Ticket_code> findByIdAndAttention_AttentionStatus(Integer ticketId, AttentionStatus attentionStatus);
}
