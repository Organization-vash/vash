package com.vash.entel.service.impl;

import com.vash.entel.model.entity.Attention;
import com.vash.entel.model.enums.AttentionStatus;
import com.vash.entel.model.enums.SuccessStatus;
import com.vash.entel.repository.AttentionRepository;
import com.vash.entel.service.AttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class AttentionServiceImpl implements AttentionService {

    @Autowired
    private AttentionRepository attentionRepository;

    @Override
    public ResponseEntity<Map<String, String>> finalizeAttention(Integer attentionId) {
        Optional<Attention> attentionOpt = attentionRepository.findById(attentionId);

        if (attentionOpt.isPresent()) {
            Attention attention = attentionOpt.get();

            if ((attention.getSuccessStatus() == SuccessStatus.SUCCESSFUL || attention.getSuccessStatus() == SuccessStatus.NOT_SUCCESSFUl) && (attention.getAttentionStatus() == AttentionStatus.ATTEND || attention.getAttentionStatus() == AttentionStatus.NOT_ATTENDING)){
                attention.setUpdated_at(LocalDateTime.now());
                attentionRepository.save(attention);

                return ResponseEntity.ok(Map.of("message", "Atención finalizada con éxito"));
            }
            else {
                return ResponseEntity.badRequest().body(Map.of("message","No se cumplen las condiciones para finalizar la atención"));
            }
        }
        else {
            return ResponseEntity.badRequest().body(Map.of("message", "Atención no encontrada"));
        }
    }
}
