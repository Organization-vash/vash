package com.vash.entel.api;
import com.vash.entel.dto.TicketHistoryDTO;
import com.vash.entel.model.entity.Survey;
import com.vash.entel.model.enums.AttentionStatus;
import com.vash.entel.service.AttentionService;
import com.vash.entel.service.TicketCodeService;
import com.vash.entel.model.enums.SuccessStatus;
import com.vash.entel.service.impl.WaitingQueueServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/attention")
@RequiredArgsConstructor
public class AttentionController {
    private final WaitingQueueServiceImpl waitingQueueService;
    private final AttentionService attentionService;

    @GetMapping("/next")
    public ResponseEntity<?> getNextPendingTicket(@RequestParam("moduleId") Integer moduleId){
        return waitingQueueService.getNextPendingTicket(moduleId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/accept")
public ResponseEntity<Map<String, String>> acceptTicket(@RequestParam Integer moduleId) {
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

    @PostMapping("/register-survey")
    public ResponseEntity<Map<String, String>> registerSurveyForLastAcceptedTicket(
            @RequestBody Map<String, Integer> request) {
        Integer surveyValue = request.get("value");
        return waitingQueueService.registerSurveyForLastAcceptedTicket(surveyValue);
    }

    @PostMapping("/{id}/finalize")
    public ResponseEntity<Map<String, String>> finalizeTicket(@PathVariable Integer id) {
        return attentionService.finalizeAttention(id);
    }
    @PostMapping("/{attentionId}/add-service")
    public ResponseEntity<Map<String, String>> addServiceToAttention(
            @PathVariable Integer attentionId,
            @RequestParam Integer serviceId) {
        attentionService.addServiceToAttention(attentionId, serviceId);
        return ResponseEntity.ok(Map.of("message", "Servicio agregado a la atención con éxito"));
    }
    

    @PostMapping("/{attentionId}/remove-service")
    public ResponseEntity<Map<String, String>> removeServiceFromAttention(
            @PathVariable Integer attentionId,
            @RequestParam Integer serviceId) {
        attentionService.removeServiceFromAttention(attentionId, serviceId);
        return ResponseEntity.ok(Map.of("message", "Servicio eliminado de la atención con éxito"));
    }
    @GetMapping("/getHistory")
    public ResponseEntity<List<TicketHistoryDTO>> getHistoryByModule(@RequestParam Integer moduleId){
        List<TicketHistoryDTO> history = ticketCodeService.getTodayTicketsByModule(moduleId);
        return ResponseEntity.ok(history);
    }
}
