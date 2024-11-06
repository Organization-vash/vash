package com.vash.entel.service.impl;

import com.vash.entel.dto.NextPendingTicketResponseDTO;
import com.vash.entel.model.entity.Attention;
import com.vash.entel.model.entity.Module;
import com.vash.entel.model.entity.Ticket_code;
import com.vash.entel.model.entity.WaitingQueue;
import com.vash.entel.model.enums.AttentionStatus;
import com.vash.entel.model.enums.ModuleStatus;
import com.vash.entel.repository.AttentionRepository;
import com.vash.entel.repository.ModuleRepository;
import com.vash.entel.repository.TicketCodeRepository;
import com.vash.entel.repository.WaitingQueueRepository;
import com.vash.entel.service.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WaitingQueueServiceImpl implements WaitingQueueService {
    private final WaitingQueueRepository waitingQueueRepository;
    private final ModuleRepository moduleRepository;
    private final TicketCodeRepository ticketCodeRepository;
    private final AttentionRepository attentionRepository;

    private Integer lastQueriedTicketCodeId;
    private Integer lastAcceptedTicketId;

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

    @Override
    public ResponseEntity<Map<String, String>> acceptTicket() {
        if (lastQueriedTicketCodeId == null) {
            return ResponseEntity.ok(Map.of("message", "No se solicitó próximo ticket en cola"));
        }

        Ticket_code ticketCode = ticketCodeRepository.findById(lastQueriedTicketCodeId)
                .orElseThrow(() -> new RuntimeException("Ticket code not found"));

        Attention attention = new Attention();
        attention.setUser(ticketCode.getCustomer());
        attention.setCreated_at(LocalDateTime.now());
        attention.setAttentionStatus(AttentionStatus.ATTENDING);

        Attention savedAttention = attentionRepository.save(attention);

        ticketCode.setAttention(savedAttention);
        ticketCodeRepository.save(ticketCode);

        WaitingQueue waitingQueue = waitingQueueRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new RuntimeException("No se encontró la cola de espera para este ticket"));

        updateWaitingQueueStatus(waitingQueue, AttentionStatus.ATTENDING);
        lastAcceptedTicketId = lastQueriedTicketCodeId;

        lastQueriedTicketCodeId = null;

        return ResponseEntity.ok(Map.of("message", "Ticket accepted"));
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

    // Obtener el último ID del ticket aceptado
    public Optional<Integer> getLastAcceptedTicketId() {
        return Optional.ofNullable(lastAcceptedTicketId);
    }

    private void updateWaitingQueueStatus(WaitingQueue waitingQueue, AttentionStatus status) {
        waitingQueue.setAttentionStatus(status);
        waitingQueueRepository.save(waitingQueue);
    }

    // Almacenar el último ticket consultado en lastAcceptedTicketId antes de llamar a acceptTicket
    public void storeLastAcceptedTicketId() {
        lastAcceptedTicketId = lastQueriedTicketCodeId;
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
}
