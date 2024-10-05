package com.vash.entel.service;

import com.vash.entel.model.entity.Agency;
import com.vash.entel.model.entity.Service;

public interface TicketCodeService {
    String generateTicketCode(String documentNumber, String fullname, Service service, Agency agency);
}
