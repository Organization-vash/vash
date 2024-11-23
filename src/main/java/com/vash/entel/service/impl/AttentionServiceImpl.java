package com.vash.entel.service.impl;

import com.vash.entel.exception.ResourceNotFoundException;
import com.vash.entel.exception.BadRequestException;
import com.vash.entel.model.entity.Attention;
import com.vash.entel.model.entity.Service;
import com.vash.entel.model.enums.AttentionStatus;
import com.vash.entel.model.enums.SuccessStatus;
import com.vash.entel.repository.AttentionRepository;
import com.vash.entel.repository.ServiceRepository;
import com.vash.entel.service.AttentionService;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@org.springframework.stereotype.Service 
public class AttentionServiceImpl implements AttentionService {

    private final AttentionRepository attentionRepository;
    private final ServiceRepository serviceRepository;

    public AttentionServiceImpl(AttentionRepository attentionRepository, ServiceRepository serviceRepository) {
        this.attentionRepository = attentionRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public void addServiceToAttention(Integer attentionId, Integer serviceId) {
        Attention attention = attentionRepository.findById(attentionId)
                .orElseThrow(() -> new ResourceNotFoundException("Attention no encontrada"));
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service no encontrado"));

        attention.getServices().add(service);
        attentionRepository.save(attention);
    }

    @Override
    public void removeServiceFromAttention(Integer attentionId, Integer serviceId) {
        Attention attention = attentionRepository.findById(attentionId)
                .orElseThrow(() -> new ResourceNotFoundException("Attention no encontrada"));
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service no encontrado"));

        if (attention.getServices().size() <= 1) {
            throw new BadRequestException("Debe haber al menos un servicio seleccionado");
        }

        attention.getServices().remove(service);
        attentionRepository.save(attention);
    }

    @Override
    public ResponseEntity<Map<String, String>> finalizeAttention(Integer attentionId) {
        Optional<Attention> attentionOpt = attentionRepository.findById(attentionId);

        if (attentionOpt.isPresent()) {
            Attention attention = attentionOpt.get();

            if ((attention.getSuccessStatus() == SuccessStatus.SUCCESSFUL || attention.getSuccessStatus() == SuccessStatus.NOT_SUCCESSFUl)
                    && (attention.getAttentionStatus() == AttentionStatus.ATTEND || attention.getAttentionStatus() == AttentionStatus.NOT_ATTENDING)) {
                attention.setUpdated_at(LocalDateTime.now());
                attentionRepository.save(attention);

                return ResponseEntity.ok(Map.of("message", "Atención finalizada con éxito"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "No se cumplen las condiciones para finalizar la atención"));
            }
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Atención no encontrada"));
        }
    }
}
