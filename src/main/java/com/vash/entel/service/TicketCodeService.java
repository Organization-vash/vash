package com.vash.entel.service;

import java.util.List;

import com.vash.entel.dto.SearchCodeDTO;
import com.vash.entel.dto.TicketHistoryDTO;
import com.vash.entel.model.entity.Agency;
import com.vash.entel.model.entity.Service;

public interface TicketCodeService {
    String generateTicketCode(Long documentNumber, String fullname, Service service, Agency agency);
    List<SearchCodeDTO> searchTicketsByCode(String seachCode);
    List<TicketHistoryDTO> getTodayTicketsByModule(Integer moduleId);
}
