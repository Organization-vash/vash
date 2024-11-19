package com.vash.entel.service;

import com.vash.entel.model.entity.Agency;

import java.util.List;

public interface AgencyService {
    List<Agency> getAll();
    Agency findById(int id);

    Agency createAgency(Agency agency);
    Agency updateAgency(Integer id, Agency updatedAgency);
    void delete(Integer id);
}
