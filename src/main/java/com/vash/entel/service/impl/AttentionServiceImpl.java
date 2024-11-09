package com.vash.entel.service.impl;

import com.vash.entel.model.entity.Attention;
import com.vash.entel.model.entity.Ticket_code;
import com.vash.entel.model.enums.AttentionStatus;
import com.vash.entel.repository.AttentionRepository;
import com.vash.entel.repository.TicketCodeRepository;
import com.vash.entel.service.AttentionService;
import com.vash.entel.service.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AttentionServiceImpl implements AttentionService {

    private final TicketCodeRepository ticketCodeRepository;
    private final AttentionRepository attentionRepository;
    private final WaitingQueueService waitingQueueService;


    @Override
    public Optional<Attention> getLastAcceptedAttention() {
        Optional<Integer> lastAcceptedTicketId = waitingQueueService.getLastAcceptedTicketId();

        if (lastAcceptedTicketId.isEmpty()) {
            return Optional.empty();
        }

        Ticket_code ticketCode = ticketCodeRepository.findById(lastAcceptedTicketId.get())
                .orElseThrow(() -> new RuntimeException("No se encontró el ticket aceptado"));

        return Optional.ofNullable(ticketCode.getAttention());
    }

    @Override
    public ResponseEntity<Map<String, String>> updateAttentionStatus(AttentionStatus status) {
        Optional<Integer> lastAcceptedTicketId = waitingQueueService.getLastAcceptedTicketId();

        if (lastAcceptedTicketId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No se encontró atención para el último ticket aceptado"));
        }

        Attention attention = attentionRepository.findById(lastAcceptedTicketId.get())
                .orElseThrow(() -> new RuntimeException("No se encontró atención para el ticket aceptado"));

        attention.setAttentionStatus(status);
        attentionRepository.save(attention);

        String message = (status == AttentionStatus.ATTEND) ? "Attention marked as ATTEND" : "Attention marked as NOT_ATTENDING";
        return ResponseEntity.ok(Map.of("message", message));
    }

}
