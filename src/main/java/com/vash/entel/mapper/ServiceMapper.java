package com.vash.entel.mapper;

import com.vash.entel.dto.ServiceDTO;
import com.vash.entel.model.entity.Service;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ServiceMapper {

    private final ModelMapper modelMapper;

    public ServiceDTO toDTO(Service service) {
        return modelMapper.map(service, ServiceDTO.class);
    }

    public Service toEntity(ServiceDTO serviceDTO) {
        return modelMapper.map(serviceDTO, Service.class);
    }
}
