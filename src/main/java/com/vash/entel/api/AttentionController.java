package com.vash.entel.api;

import com.vash.entel.model.enums.AttentionStatus;
import com.vash.entel.service.AttentionService;
import com.vash.entel.service.TicketCodeService;
import com.vash.entel.model.enums.SuccessStatus;
import com.vash.entel.service.impl.WaitingQueueServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/attention")
@RequiredArgsConstructor
public class AttentionController {
    private final WaitingQueueServiceImpl waitingQueueService;
    private final AttentionService attentionService;
    private final TicketCodeService ticketCodeService;

    @GetMapping("/next")
    public ResponseEntity<?> getNextPendingTicket(@RequestParam("moduleId") Integer moduleId){
        return waitingQueueService.getNextPendingTicket(moduleId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/accept")
    public ResponseEntity<Map<String, String>> acceptTicket(@RequestParam Integer moduleId){
        waitingQueueService.storeLastAcceptedTicketId();
        return waitingQueueService.acceptTicket(moduleId);
    }

    @PostMapping("/reject")
    public ResponseEntity<Map<String, String>> rejectTicket() {
        return waitingQueueService.rejectTicket();
    }

    @GetMapping("/lastAcceptedTicketId")
    public ResponseEntity<Map<String, Integer>> getLastAcceptedTicketId() {
        return waitingQueueService.getLastAcceptedTicketId()
                .map(ticketId -> ResponseEntity.ok(Map.of("lastAcceptedTicketId", ticketId)))
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/markAsAttend")
    public ResponseEntity<Map<String, String>> markAsAttend() {
        return waitingQueueService.updateAttentionStatus(AttentionStatus.ATTEND);
    }

    @PostMapping("/markAsNotAttend")
    public ResponseEntity<Map<String, String>> markAsNotAttend() {
        return waitingQueueService.updateAttentionStatus(AttentionStatus.NOT_ATTENDING);
    }
    @PostMapping("/markAsSuccessful")
    public ResponseEntity<Map<String, String>> markAsSuccessful() {
        return waitingQueueService.updateSuccesStatus(SuccessStatus.SUCCESSFUL);
    }

    @PostMapping("/markAsNotSuccessful")
    public ResponseEntity<Map<String, String>> markAsNotSuccessful() {
        return waitingQueueService.updateSuccesStatus(SuccessStatus.NOT_SUCCESSFUl);
    }

    @PostMapping("/{id}/finalize")
    public ResponseEntity<Map<String, String>> finalizeTicket(@PathVariable Integer id) {
        return attentionService.finalizeAttention(id);
    }
}
