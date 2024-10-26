package com.vash.entel.api;

import com.vash.entel.service.impl.AttentionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attention")
@RequiredArgsConstructor
public class AttentionController {
    private final AttentionServiceImpl attentionService;

    @GetMapping("/next")
    public ResponseEntity<?> getNextPendingTicket(@RequestParam("moduleId") Integer moduleId){
        return attentionService.getNextPendingTicket(moduleId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptTicket(){
        return attentionService.acceptTicket();
    }

    @PostMapping("/reject")
    public ResponseEntity<String> rejectTicket() {
        return attentionService.rejectTicket();
    }
}
