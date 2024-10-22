package com.vash.entel.service;

import com.vash.entel.dto.ServiceDTO;

import java.util.List;

public interface ServiceService {
    List<ServiceDTO> getAll();
    ServiceDTO findById(int id);
    ServiceDTO findByName(String name);
    ServiceDTO Create(ServiceDTO ServiceDTO);
    ServiceDTO Update(Integer id, ServiceDTO updateServiceDTO);
    void delete(Integer id);
}
