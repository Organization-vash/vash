package com.vash.entel.service.impl;

import com.vash.entel.dto.SearchCodeDTO;
import com.vash.entel.exception.ResourceNotFoundException;
import com.vash.entel.mapper.SearchCodeMapper;
import com.vash.entel.dto.TicketHistoryDTO;
import com.vash.entel.model.entity.*;
import com.vash.entel.model.entity.Module;
import com.vash.entel.model.enums.AttentionStatus;
import com.vash.entel.model.enums.ModuleStatus;
import com.vash.entel.model.enums.SuccessStatus;
import com.vash.entel.repository.*;
import com.vash.entel.service.TicketCodeService;
import com.vash.entel.service.WaitingQueueService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;
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

    @Autowired
    private final ModuleRepository moduleRepository;

    @Autowired
    private final AttentionRepository attentionRepository;

    @Autowired
    private final SurveyRepository surveyRepository;

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

    @Override
    public Optional<Ticket_code> getTicketByIdAndStatus(Integer ticketCodeId, AttentionStatus status) {
        return ticketCodeRepository.findByIdAndAttention_AttentionStatus(ticketCodeId, status);
    }

    @Override
    public void transferTicketToModule(Integer ticketCodeId, Integer moduleId) {
        Ticket_code ticket = getTicketByIdAndStatus(ticketCodeId, AttentionStatus.ATTENDING)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Attention attention = attentionRepository.findById(ticket.getAttention().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Attention not found for this ticket"));

        // Verificar que no haya campos nulos
        if (attention.getAttentionStatus() == null) {
            throw new RuntimeException("Attention status cannot be null");
        }

        // Asignar un valor a los campos necesarios
        attention.setAttentionStatus(AttentionStatus.TRANSFERRED);

        // Asegúrate de que los campos obligatorios no sean null
        if (attention.getCreated_at() == null) {
            attention.setCreated_at(LocalDateTime.now());
        }

        if (attention.getUpdated_at() == null) {
            attention.setUpdated_at(LocalDateTime.now());
        }

        // Si successStatus es obligatorio, asignar un valor predeterminado
        if (attention.getSuccessStatus() == null) {
            attention.setSuccessStatus(SuccessStatus.NOT_SUCCESSFUl); // O el valor adecuado
        }

        if (attention.getSurvey() == null) {
            Survey newSurvey = new Survey();
            newSurvey.setCreated_at(LocalDateTime.now());  // Asignar la fecha actual
            newSurvey.setValue(1);
            surveyRepository.save(newSurvey);  // Guardar el Survey en la base de datos
            attention.setSurvey(newSurvey);  // Asignarlo al objeto Attention
        }

        if (attention.getTime() == null) {
            attention.setTime(LocalDateTime.now());
        }

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        if (!module.getModuleStatus().equals(ModuleStatus.ACTIVE)) {
            throw new RuntimeException("Module is not active");
        }

        ticket.setModule(module);

        attentionRepository.save(attention);
        waitingQueueService.addDerivateToQueue(ticket);
        ticketCodeRepository.save(ticket);
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
