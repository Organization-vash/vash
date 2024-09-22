package com.vash.entel.service;

import com.vash.entel.model.entity.Service;

import java.util.List;

public interface ServiceService {
    List<Service> getAll();
    Service findById(int id);
    Service Create(Service service);
    Service Update(Integer id, Service service);
    void delete(Integer id);
}
