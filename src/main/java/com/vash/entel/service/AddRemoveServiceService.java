package com.vash.entel.service;

import com.vash.entel.model.entity.Service;
import jakarta.servlet.ServletOutputStream;

import java.util.List;

public interface AddRemoveServiceService {
    List<Service> getServices(Integer attention);
    Service addService(Integer attention,Integer serviceId);
    void removeService(Integer attention,Integer serviceId);
}
