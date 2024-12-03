package com.vash.entel.service.impl;

import com.vash.entel.dto.SearchCodeDTO;
import com.vash.entel.mapper.SearchCodeMapper;
import com.vash.entel.dto.TicketHistoryDTO;
import com.vash.entel.model.entity.Agency;
import com.vash.entel.model.entity.Customer;
import com.vash.entel.model.entity.Ticket_code;
import com.vash.entel.repository.CustomerRepository;
import com.vash.entel.repository.TicketCodeRepository;
import com.vash.entel.service.TicketCodeService;
import com.vash.entel.service.WaitingQueueService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketCodeServiceImpl implements TicketCodeService {
    @Autowired
    private SearchCodeMapper searchCodeMapper;

    @Autowired
    private TicketCodeRepository ticketCodeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WaitingQueueService waitingQueueService;

    @Override
    public String generateTicketCode(Long documentNumber, String fullname, com.vash.entel.model.entity.Service service, Agency agency){
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

        Customer customer = customerRepository.findByDocNumber(documentNumber)
                        .orElseThrow();

        ticketCode.setCustomer(customer);

        ticketCodeRepository.save(ticketCode);

        waitingQueueService.addTicketToQueue(ticketCode);

        return newCode;
    }

    @Override
    public List<TicketHistoryDTO> getTodayTicketsByModule(Integer moduleId){
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        List<Ticket_code> tickets = ticketCodeRepository.findTicket_codesByModuleIdAndDate(moduleId, startOfDay, endOfDay);
        
        return tickets.stream().map(ticket -> new TicketHistoryDTO(
            ticket.getCode(),
                ticket.getService().getName(),
                ticket.getCustomer().getFullname()
        )).collect(Collectors.toList());
    }

    private boolean isNewMonth(Ticket_code lastCode) {
        LocalDateTime now = LocalDateTime.now();
        return now.getMonth() != lastCode.getCreated().getMonth();
    }

    private String generateCode(com.vash.entel.model.entity.Service service, int currentNumber) {
        char letter = Character.toUpperCase(service.getName().charAt(0));
        String formattedNumber = String.format("%03d", currentNumber);
        return letter + formattedNumber;
    }

    private int extractNumber(String code) {
        return Integer.parseInt(code.substring(1)); 
    }

    @Override
    public List<SearchCodeDTO> searchTicketsByCode(String code) {
        List<Object[]> results = ticketCodeRepository.findByCode(code);
        return searchCodeMapper.toDtoList(results);
    }
}
