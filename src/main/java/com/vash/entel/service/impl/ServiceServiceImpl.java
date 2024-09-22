package com.vash.entel.service.impl;

import com.vash.entel.model.entity.Service;
import com.vash.entel.repository.ServiceRepository;
import com.vash.entel.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Service> getAll() {
        return serviceRepository.findAll();
    }

    @Transactional
    @Override
    public Service findById(int id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found with id: " + id));
    }

    @Transactional
    @Override
    public Service Create(Service service) {
        service.setCreatedAt(LocalDateTime.now());
        return serviceRepository.save(service);
    }

    @Transactional
    @Override
    public Service Update(Integer id, Service service) {
        Service serviceFromDb = findById(id);
        serviceFromDb.setName(service.getName());
        serviceFromDb.setDescription(service.getDescription());
        serviceFromDb.setUpdatedAt(LocalDateTime.now());
        return serviceRepository.save(serviceFromDb);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Service service = findById(id);
        serviceRepository.delete(service);
    }
}
