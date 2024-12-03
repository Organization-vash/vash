package com.vash.entel.service.impl;

import com.vash.entel.dto.NextPendingTicketResponseDTO;
import com.vash.entel.model.entity.Attention;
import com.vash.entel.model.entity.*;
import com.vash.entel.model.entity.Module;
import com.vash.entel.model.entity.Ticket_code;
import com.vash.entel.model.entity.WaitingQueue;
import com.vash.entel.model.enums.AttentionStatus;
import com.vash.entel.model.enums.ModuleStatus;
import com.vash.entel.model.enums.Role;
import com.vash.entel.model.enums.SuccessStatus;
import com.vash.entel.repository.AttentionRepository;
import com.vash.entel.repository.ModuleRepository;
import com.vash.entel.repository.TicketCodeRepository;
import com.vash.entel.repository.WaitingQueueRepository;
import com.vash.entel.service.AuthService;
import com.vash.entel.repository.*;
import com.vash.entel.service.WaitingQueueService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WaitingQueueServiceImpl implements WaitingQueueService {
    private final WaitingQueueRepository waitingQueueRepository;
    private final DerivateRepository derivateRepository;
    private final ModuleRepository moduleRepository;
    private final TicketCodeRepository ticketCodeRepository;
    private final AttentionRepository attentionRepository;
    private final SurveyRepository surveyRepository;
    private Integer lastQueriedTicketCodeId;
    private Integer lastAcceptedTicketId;
    private final AuthService authService; // Inyectamos AuthService
    private final HttpServletRequest request; // Inyección de HttpServletRequest

    @Override
    public Optional<NextPendingTicketResponseDTO> getNextPendingTicket(Integer moduleId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        if (!module.getModuleStatus().equals(ModuleStatus.ACTIVE)) {
            return Optional.empty();
        }

        WaitingQueue waitingQueue = waitingQueueRepository.findFirstByAttentionStatusOrderByCreatedAtAsc(AttentionStatus.WAITING);

        if (waitingQueue != null) {
            Ticket_code ticketCode = waitingQueue.getTicketCode();

            lastQueriedTicketCodeId = ticketCode.getId();

            NextPendingTicketResponseDTO responseDTO = new NextPendingTicketResponseDTO(
                    ticketCode.getCode(),
                    ticketCode.getService().getName(),
                    ticketCode.getCustomer().getDocNumber(),
                    ticketCode.getCustomer().getFullname(),
                    ticketCode.getId()
            );

            updateWaitingQueueStatus(waitingQueue, AttentionStatus.ATTENDING);
            return Optional.of(responseDTO);
        }
        else {
            return Optional.empty();
        }
    }

    @Transactional
    @Scheduled(fixedRate = 3000)
    public Optional<NextPendingTicketResponseDTO> checkTransferredTickets() {
        Derivate waitingQueue = derivateRepository.findFirstByAttentionStatusOrderByCreatedAtAsc(AttentionStatus.TRANSFERRED);

        if (waitingQueue != null) {
            Ticket_code ticketCode = waitingQueue.getTicketCode();

            NextPendingTicketResponseDTO responseDTO = new NextPendingTicketResponseDTO(
                    ticketCode.getCode(),
                    ticketCode.getService().getName(),
                    ticketCode.getCustomer().getDocNumber(),
                    ticketCode.getCustomer().getFullname(),
                    ticketCode.getId()
            );

            return Optional.of(responseDTO);
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> acceptTicket(Integer moduleId) {
        if (lastQueriedTicketCodeId == null) {
            return ResponseEntity.ok(Map.of("message", "No se solicitó próximo ticket en cola"));
        }

        User currentAdviser = authService.getAuthenticatedUser(request);
        if (currentAdviser.getRole() != Role.ADVISER) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Solo los asesores pueden aceptar tickets"));
        }

        // Buscar el ticket consultado previamente
        Ticket_code ticketCode = ticketCodeRepository.findById(lastQueriedTicketCodeId)
                .orElseThrow(() -> new RuntimeException("Ticket code not found"));

        // Buscar el módulo asociado
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        ticketCode.setModule(module);

        // Crear una nueva atención
        Attention attention = new Attention();
        attention.setUser(ticketCode.getCustomer());
        attention.setAdviser(currentAdviser);        // Asesor autenticado

        attention.setCreated_at(LocalDateTime.now());
        attention.setAttentionStatus(AttentionStatus.ATTENDING);

        // Guardar la atención en la base de datos
        Attention savedAttention = attentionRepository.save(attention);

        // Asociar el servicio principal del ticket a la atención
        com.vash.entel.model.entity.Service service = ticketCode.getService(); // Servicio principal del ticket
        if (service == null) {
            throw new RuntimeException("El ticket no tiene un servicio asociado");
        }
        savedAttention.getServices().add(service); // Asociar el servicio a la atención
        attentionRepository.save(savedAttention); // Guardar la relación en attention_services

        // Actualizar el ticket con la atención creada
        ticketCode.setAttention(savedAttention);
        ticketCodeRepository.save(ticketCode);

        // Actualizar el estado del ticket en la cola de espera
        WaitingQueue waitingQueue = waitingQueueRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new RuntimeException("No se encontró la cola de espera para este ticket"));
        updateWaitingQueueStatus(waitingQueue, AttentionStatus.ATTENDING);

        // Actualizar el último ID de ticket aceptado
        lastAcceptedTicketId = lastQueriedTicketCodeId;
        lastQueriedTicketCodeId = null;

        // Devuelve el ticketId y el attentionId
        return ResponseEntity.ok(Map.of(
                "message", "Ticket accepted y servicio registrado en attention_services",
                "ticketId", String.valueOf(ticketCode.getId()),
                "attentionId", String.valueOf(savedAttention.getId()) // Agregar el attentionId
        ));
    }

    @Override
    public ResponseEntity<Map<String, String>> rejectTicket() {
        if (lastQueriedTicketCodeId == null){
            return ResponseEntity.ok(Map.of("message", "No se solicitó próximo ticket en cola"));
        }
        Ticket_code ticketCode = ticketCodeRepository.findById(lastQueriedTicketCodeId)
                .orElseThrow(() -> new RuntimeException("Ticket code not found"));

        WaitingQueue waitingQueue = waitingQueueRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new RuntimeException("Ticket not found in the queue"));

        updateWaitingQueueStatus(waitingQueue, AttentionStatus.WAITING);

        lastQueriedTicketCodeId = null;

        return ResponseEntity.ok(Map.of("message", "Ticket rejected"));
    }

    @Override
    public void addTicketToQueue(Ticket_code ticketCode) {
        WaitingQueue waitingQueue = new WaitingQueue();
        waitingQueue.setTicketCode(ticketCode);
        waitingQueue.setCustomer(ticketCode.getCustomer());
        waitingQueue.setCreatedAt(LocalDateTime.now());
        waitingQueue.setAttentionStatus(AttentionStatus.WAITING);

        waitingQueueRepository.save(waitingQueue);
    }

    @Override
    public void addDerivateToQueue(Ticket_code ticketCode) {
        Derivate derivate = new Derivate();
        derivate.setTicketCode(ticketCode);
        derivate.setCustomer(ticketCode.getCustomer());
        derivate.setCreatedAt(LocalDateTime.now());
        derivate.setAttentionStatus(AttentionStatus.TRANSFERRED);
        derivate.setAttention(ticketCode.getAttention());

        derivateRepository.save(derivate);
    }

    // Obtener el último ID del ticket aceptado
    public Optional<Integer> getLastAcceptedTicketId() {
        return Optional.ofNullable(lastAcceptedTicketId);
    }

    private void updateWaitingQueueStatus(WaitingQueue waitingQueue, AttentionStatus status) {
        waitingQueue.setAttentionStatus(status);
        waitingQueueRepository.save(waitingQueue);
    }

    // Actualizar el estado de atención a ATTEND o NOT_ATTENDING
    public ResponseEntity<Map<String, String>> updateAttentionStatus(AttentionStatus status) {
        Optional<Integer> lastAcceptedTicketId = getLastAcceptedTicketId();

        if (lastAcceptedTicketId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No se encontró atención para el último ticket aceptado"));
        }

        Attention attention = attentionRepository.findById(lastAcceptedTicketId.get())
                .orElseThrow(() -> new RuntimeException("No se encontró atención para el ticket aceptado"));

        Ticket_code ticketCode = (Ticket_code) ticketCodeRepository.findById(lastAcceptedTicketId)
                .orElseThrow(() -> new RuntimeException("Ticket code not found"));

        attention.setAttentionStatus(status);
        attentionRepository.save(attention);
        WaitingQueue waitingQueue = waitingQueueRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new RuntimeException("No se encontró la cola de espera para el ticket aceptado"));

        waitingQueue.setAttentionStatus(status);
        waitingQueueRepository.save(waitingQueue);

        String message = (status == AttentionStatus.ATTEND) ? "Attention marked as ATTEND" : "Attention marked as NOT_ATTENDING";
        return ResponseEntity.ok(Map.of("message", message));
    }

    public ResponseEntity<Map<String, String>> updateSuccesStatus(SuccessStatus status) {
        Optional<Integer> lastAcceptedTicketId = getLastAcceptedTicketId();

        if (lastAcceptedTicketId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No se encontró atención para el último ticket aceptado"));
        }

        Attention attention = attentionRepository.findById(lastAcceptedTicketId.get())
                .orElseThrow(() -> new RuntimeException("No se encontró atención para el ticket aceptado"));

        Ticket_code ticketCode = (Ticket_code) ticketCodeRepository.findById(lastAcceptedTicketId)
                .orElseThrow(() -> new RuntimeException("Ticket code not found"));

        attention.setSuccessStatus(status);
        attentionRepository.save(attention);

        String message = (status == SuccessStatus.SUCCESSFUL) ? "Attention marked as SUCCESSFUL" : "Attention marked as NOT_SUCCESSFUl";
        return ResponseEntity.ok(Map.of("message", message));
    }

    public ResponseEntity<Map<String, String>> registerSurveyForLastAcceptedTicket(Integer surveyValue) {
        // Obtener el último ticket aceptado
        Optional<Integer> lastTicketIdOptional = getLastAcceptedTicketId();
        if (lastTicketIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No se encontró un ticket aceptado previamente"));
        }
        // Obtener la atención asociada al ticket aceptado
        Integer lastTicketId = lastTicketIdOptional.get();
        Attention attention = attentionRepository.findById(lastTicketId)
                .orElseThrow(() -> new RuntimeException("No se encontró atención asociada al ticket"));
        // Verificar si ya tiene una encuesta
        // Verificar si ya tiene una encuesta
        if (attention.getSurvey() != null) {
            // Si ya tiene una encuesta, actualiza su valor
            Survey existingSurvey = attention.getSurvey();
            existingSurvey.setValue(surveyValue);
            surveyRepository.save(existingSurvey); // Actualizar la encuesta existente
            return ResponseEntity.ok(Map.of("message", "Survey value updated successfully"));
        }
        // Validar el valor de la encuesta
        if (surveyValue < 1 || surveyValue > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Survey value must be between 1 and 5"));
        }
        // Crear y guardar la encuesta primero
        Survey survey = new Survey();
        survey.setValue(surveyValue);
        survey.setCreated_at(LocalDateTime.now());
        Survey savedSurvey = surveyRepository.save(survey);
        // Asociar la encuesta guardada a la atención
        attention.setSurvey(savedSurvey);
        attentionRepository.save(attention);
        return ResponseEntity.ok(Map.of("message", "Survey registered successfully"));
    }
}
