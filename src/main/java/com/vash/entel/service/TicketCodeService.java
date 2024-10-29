package com.vash.entel.service;

import com.vash.entel.model.entity.Agency;
import com.vash.entel.model.entity.Service;

public interface TicketCodeService {
    String generateTicketCode(Long documentNumber, String fullname, Service service, Agency agency);

}
