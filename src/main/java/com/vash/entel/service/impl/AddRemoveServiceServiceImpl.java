package com.vash.entel.service.impl;

import com.vash.entel.model.entity.Attention;
import com.vash.entel.repository.AddRemoveServiceRepository;
import com.vash.entel.repository.AttentionRepository;
import com.vash.entel.repository.ServiceRepository;
import com.vash.entel.service.AddRemoveServiceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AddRemoveServiceServiceImpl implements AddRemoveServiceService {
    private final AddRemoveServiceRepository addRemoveServiceRepository;
    private final ServiceRepository serviceRepository;
    private final AttentionRepository attentionRepository;

    @Transactional
    @Override
    public List<com.vash.entel.model.entity.Service> getServices(Integer attentionId) {
        Attention attention = addRemoveServiceRepository.findById(attentionId)
                .orElseThrow(() -> new RuntimeException("Attention Not Found"));
        return attention.getServices();
    }

    @Transactional
    @Override
    public com.vash.entel.model.entity.Service addService(Integer attentionId, Integer serviceId) {
        Attention attention = addRemoveServiceRepository.findById(attentionId)
                .orElseThrow(() -> new RuntimeException("Attention Not Found"));
        com.vash.entel.model.entity.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service Not Found"));
        
        service.setAttention(attention);
        service.setUpdatedAt(LocalDateTime.now());
        return serviceRepository.save(service);
    }

    @Transactional
    @Override
    public void removeService(Integer attentionId, Integer serviceId) {
        Attention attention = addRemoveServiceRepository.findById(attentionId)
                .orElseThrow(() -> new RuntimeException("Attention Not Found"));
        com.vash.entel.model.entity.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service Not Found"));

        // Verificar que la atención tenga más de un servicio antes de eliminar
        if (attention.getServices().size() <= 1) {
            throw new RuntimeException("Cannot remove the last service. An attention must have at least one service.");
        }

        service.setAttention(null);
        serviceRepository.save(service);
    }
}
