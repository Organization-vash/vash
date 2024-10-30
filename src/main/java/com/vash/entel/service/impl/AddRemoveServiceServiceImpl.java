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
    public List<com.vash.entel.model.entity.Service> getServices(Integer attention) {
        Attention attentiond =addRemoveServiceRepository.findById(attention)
                .orElseThrow(() -> new RuntimeException("Attention Not Found"));
        return attentiond.getServices();
    }


    @Transactional
    @Override
    public com.vash.entel.model.entity.Service addService(Integer attention, Integer serviceId) {
        Attention attentiond =addRemoveServiceRepository.findById(attention)
                .orElseThrow(() -> new RuntimeException("Attention Not Found"));
        com.vash.entel.model.entity.Service serviced = serviceRepository.findById(serviceId)
                .orElseThrow(()-> new RuntimeException("Service Not Found"));
        serviced.setAttention(attentiond);
        serviced.setUpdatedAt(LocalDateTime.now());
        return serviceRepository.save(serviced);
    }


    @Transactional
    @Override
    public void removeService(Integer attention, Integer serviceId) {
        addRemoveServiceRepository.findById(attention)
                .orElseThrow(() -> new RuntimeException("Attention Not Found"));
        com.vash.entel.model.entity.Service serviced = serviceRepository.findById(serviceId)
                .orElseThrow(()-> new RuntimeException("Service Not Found"));
        serviced.setAttention(null);
        serviceRepository.save(serviced);

    }

}
