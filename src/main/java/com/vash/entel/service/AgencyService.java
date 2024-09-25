package com.vash.entel.service;

import com.vash.entel.model.entity.Agency;

import java.util.List;

public interface AgencyService {
    List<Agency> getAll();
    Agency findById(int id);
}
