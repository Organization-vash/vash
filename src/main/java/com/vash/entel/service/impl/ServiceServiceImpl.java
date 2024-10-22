package com.vash.entel.service.impl;

import com.vash.entel.dto.ServiceDTO;
import com.vash.entel.exception.BadRequestException;
import com.vash.entel.exception.ResourceNotFoundException;
import com.vash.entel.mapper.ServiceMapper;
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
    private final ServiceMapper serviceMapper;

    @Transactional(readOnly = true)
    @Override
    public List<ServiceDTO> getAll() {
        List<Service> services = serviceRepository.findAll();
        return services.stream()
                .map(serviceMapper::toDTO)
                .toList();
    }

    @Transactional
    @Override
    public ServiceDTO findById(int id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("El servicio con ID "+id+" no fue encontrado"));
        return serviceMapper.toDTO(service);
    }

    @Transactional
    @Override
    public ServiceDTO findByName(String name) {
        Service service = serviceRepository.findByName(name)
                .orElseThrow(()-> new ResourceNotFoundException("No se encontro coincidencia"));
        return serviceMapper.toDTO(service);
    }

    @Transactional
    @Override
    public ServiceDTO Create(ServiceDTO serviceDTO) {
        serviceRepository.findByNameAndType(serviceDTO.getName(), serviceDTO.getType())
                        .ifPresent(existingService -> {
                            throw new BadRequestException("El servicio con el mismo nombre y tipo ya existe");
                        });
        Service service = serviceMapper.toEntity(serviceDTO);
        service.setCreatedAt(LocalDateTime.now());
        service = serviceRepository.save(service);
        return serviceMapper.toDTO(service);
    }

    @Transactional
    @Override
    public ServiceDTO Update(Integer id, ServiceDTO updateServiceDTO) {
        Service serviceFromDb = serviceRepository.findById(id)
                        .orElseThrow(()-> new ResourceNotFoundException("El servicio con ID "+id+" no fue encontrado"));
        serviceRepository.findByNameAndType(updateServiceDTO.getName(), updateServiceDTO.getType())
                        .filter(existingService -> !existingService.getId().equals(id))
                                .ifPresent(existingService -> {
                                    throw new BadRequestException("El servicio con el mismo nombre y tipo ya existe");
                                });
        serviceFromDb.setName(updateServiceDTO.getName());
        serviceFromDb.setDescription(updateServiceDTO.getDescription());
        serviceFromDb.setUpdatedAt(LocalDateTime.now());
        serviceFromDb = serviceRepository.save(serviceFromDb);
        return serviceMapper.toDTO(serviceFromDb);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Service service = serviceRepository.findById(id)
                        .orElseThrow(()->new ResourceNotFoundException("El servicio con ID "+id+" no fue encontrado"));
        serviceRepository.delete(service);
    }
}
