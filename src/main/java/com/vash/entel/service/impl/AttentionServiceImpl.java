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
import com.vash.entel.service.AttentionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttentionServiceImpl implements AttentionService {
    private final WaitingQueueRepository waitingQueueRepository;
    private final ModuleRepository moduleRepository;
    private final TicketCodeRepository ticketCodeRepository;
    private final AttentionRepository attentionRepository;

    private Integer lastQueriedTicketCodeId;

    @Override
    public Optional<NextPendingTicketResponseDTO> getNextPendingTicket(Integer moduleId){
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
                    ticketCode.getCustomer().getFullname()
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


    private void updateWaitingQueueStatus(WaitingQueue waitingQueue, AttentionStatus status) {
        waitingQueue.setAttentionStatus(status);
        waitingQueueRepository.save(waitingQueue);
    }
}
