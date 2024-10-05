package com.vash.entel.service.impl;

import com.vash.entel.model.entity.Agency;
import com.vash.entel.repository.AgencyRepository;
import com.vash.entel.service.AgencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class AgencyServiceImpl implements AgencyService {
    private final AgencyRepository agencyRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Agency> getAll() {
        return agencyRepository.findAll();
    }

    @Transactional
    @Override
    public Agency findById(int id) {
        return agencyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found with id: " + id));
    }
}
