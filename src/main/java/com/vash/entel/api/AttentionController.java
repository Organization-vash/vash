package com.vash.entel.api;

import com.vash.entel.model.enums.AttentionStatus;
import com.vash.entel.service.AttentionService;
import com.vash.entel.model.enums.SuccessStatus;
import com.vash.entel.service.impl.AttentionServiceImpl;
import com.vash.entel.service.impl.WaitingQueueServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/attention")
@RequiredArgsConstructor
public class AttentionController {
    private final WaitingQueueServiceImpl attentionService;
    private final WaitingQueueServiceImpl waitingQueueService;

    @GetMapping("/next")
    public ResponseEntity<?> getNextPendingTicket(@RequestParam("moduleId") Integer moduleId){
        return attentionService.getNextPendingTicket(moduleId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/accept")
    public ResponseEntity<Map<String, String>> acceptTicket(){
        waitingQueueService.storeLastAcceptedTicketId();
        return attentionService.acceptTicket();
    }

    @PostMapping("/reject")
    public ResponseEntity<Map<String, String>> rejectTicket() {
        return attentionService.rejectTicket();
    }

    @GetMapping("/lastAcceptedTicketId")
    public ResponseEntity<Map<String, Integer>> getLastAcceptedTicketId() {
        return waitingQueueService.getLastAcceptedTicketId()
                .map(ticketId -> ResponseEntity.ok(Map.of("lastAcceptedTicketId", ticketId)))
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/markAsAttend")
    public ResponseEntity<Map<String, String>> markAsAttend() {
        return attentionService.updateAttentionStatus(AttentionStatus.ATTEND);
    }

    @PostMapping("/markAsNotAttend")
    public ResponseEntity<Map<String, String>> markAsNotAttend() {
        return attentionService.updateAttentionStatus(AttentionStatus.NOT_ATTENDING);
    }
    @PostMapping("/markAsSuccessful")
    public ResponseEntity<Map<String, String>> markAsSuccessful() {
        return attentionService.updateSuccesStatus(SuccessStatus.SUCCESSFUL);
    }

    @PostMapping("/markAsNotSuccessful")
    public ResponseEntity<Map<String, String>> markAsNotSuccessful() {
        return attentionService.updateSuccesStatus(SuccessStatus.NOT_SUCCESSFUl);
    }

}
