package com.vash.entel.service.impl;

import com.vash.entel.model.entity.Agency;
import com.vash.entel.model.entity.Customer;
import com.vash.entel.model.entity.Ticket_code;
import com.vash.entel.repository.TicketCodeRepository;
import com.vash.entel.service.TicketCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TicketCodeServiceImpl implements TicketCodeService {
    @Autowired
    private TicketCodeRepository ticketCodeRepository;

    @Override
    public String generateTicketCode(String documentNumber, String fullname, com.vash.entel.model.entity.Service service, Agency agency){
        Ticket_code lastcode = ticketCodeRepository.findTopByServiceAndAgencyOrderByCreatedDesc(service, agency);

        int currentNumber = (lastcode == null || isNewMonth(lastcode)) ? 0 : extractNumber(lastcode.getCode()) + 1;

        if(currentNumber > 999){
            currentNumber = 0;
        }

        String newCode = generateCode(service, currentNumber);

        Ticket_code ticketCode = new Ticket_code();
        ticketCode.setCode(newCode);
        ticketCode.setService(service);
        ticketCode.setCreated(LocalDateTime.now());
        ticketCode.setAgency(agency);

        ticketCodeRepository.save(ticketCode);

        return newCode;
    }

    private boolean isNewMonth(Ticket_code lastCode) {
        LocalDateTime now = LocalDateTime.now();
        return now.getMonth() != lastCode.getCreated().getMonth();
    }

    private String generateCode(com.vash.entel.model.entity.Service service, int currentNumber) {
        char letter = service.getName().charAt(0);
        String formattedNumber = String.format("%03d", currentNumber);
        return letter + formattedNumber;
    }

    private int extractNumber(String code) {
        return Integer.parseInt(code.substring(1)); // Extraemos los números después de la letra
    }
}
