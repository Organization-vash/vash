package com.vash.entel.repository;

import com.vash.entel.model.entity.Agency;
import com.vash.entel.model.entity.Service;
import com.vash.entel.model.entity.Ticket_code;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketCodeRepository extends JpaRepository<Ticket_code, Integer> {
    Ticket_code findTopByServiceAndAgencyOrderByCreatedDesc(Service service, Agency agency);
}
