package com.vash.entel.service.impl;

import com.vash.entel.model.entity.Agency;
import com.vash.entel.repository.AgencyRepository;
import com.vash.entel.service.AgencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
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
                .orElseThrow(() -> new IllegalArgumentException("Agencia no encontrada con id: " + id));
    }

    @Transactional
    @Override
    public Agency createAgency(Agency agency) {
        Optional<Agency> existingAgency = agencyRepository.findBySeatAndCity(agency.getSeat(), agency.getCity());
        if (existingAgency.isPresent()) {
            throw new IllegalArgumentException("La sede '" + agency.getSeat() + "' ya existe en la ciudad '" + agency.getCity() + "'");
        }
        agency.setCreatedAt(LocalDateTime.now());
        return agencyRepository.save(agency);
    }

    @Transactional
    @Override
    public Agency updateAgency(Integer id, Agency updatedAgency) {
        Agency existingAgency = agencyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La agencia con el ID " + id + " no existe"));

        Optional<Agency> conflictingAgency = agencyRepository.findByCityAndSeat(updatedAgency.getCity(), updatedAgency.getSeat());
        if (conflictingAgency.isPresent() && !conflictingAgency.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe una agencia con la sede '" + updatedAgency.getSeat() +
                    "' en la ciudad '" + updatedAgency.getCity() + "'");
        }

        existingAgency.setCity(updatedAgency.getCity());
        existingAgency.setSeat(updatedAgency.getSeat());
        existingAgency.setUpdatedAt(LocalDateTime.now());

        return agencyRepository.save(existingAgency);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Agency agency = agencyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La agencia con el ID " + id + " no existe"));
        agencyRepository.delete(agency);
    }
}
