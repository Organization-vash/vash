package com.vash.entel.service;

import java.util.List;
import java.util.Optional;

import com.vash.entel.dto.SearchCodeDTO;
import com.vash.entel.dto.TicketHistoryDTO;
import com.vash.entel.model.entity.Agency;
import com.vash.entel.model.entity.Service;
import com.vash.entel.model.entity.Ticket_code;
import com.vash.entel.model.enums.AttentionStatus;

public interface TicketCodeService {
    String generateTicketCode(Long documentNumber, String fullname, Service service, Agency agency);
    List<SearchCodeDTO> searchTicketsByCode(String seachCode);
    List<TicketHistoryDTO> getTodayTicketsByModule(Integer moduleId);
    Optional<Ticket_code> getTicketByIdAndStatus(Integer ticketCodeId, AttentionStatus status);
    void transferTicketToModule(Integer ticketCodeId, Integer moduleId);
}
