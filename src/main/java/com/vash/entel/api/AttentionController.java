package com.vash.entel.api;

import com.vash.entel.dto.NextPendingTicketResponseDTO;
import com.vash.entel.dto.TicketHistoryDTO;
import com.vash.entel.model.enums.AttentionStatus;
import com.vash.entel.service.AttentionService;
import com.vash.entel.service.TicketCodeService;
import com.vash.entel.model.enums.SuccessStatus;
import com.vash.entel.service.impl.WaitingQueueServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.List;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @GetMapping("/report")
    public ResponseEntity<List<Map<String, Object>>> getReportData() {
        List<Map<String, Object>> reportData = attentionService.getReportData();
        return ResponseEntity.ok(reportData);
    }

    @GetMapping("/download-report")
    public ResponseEntity<byte[]> downloadReport() {
        try {
            // Llama al servicio para obtener los datos del reporte
            byte[] reportData = attentionService.generateExcelReport();

            // Formatea el nombre del archivo
            String filename = "Reporte_" + LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("HH-mm_dd-MM-yyyy")) + ".xlsx";

            // Crea los headers para la respuesta HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

            // Devuelve el archivo Excel como respuesta
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reportData);
        } catch (IOException e) {
            // Maneja los errores y devuelve el código de estado HTTP adecuado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

    @GetMapping("/transferred-tickets")
    public ResponseEntity<?> checkTransferredTickets(){
        return waitingQueueService.checkTransferredTickets()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}
