package com.vash.entel.api;

import com.vash.entel.model.entity.Agency;
import com.vash.entel.service.AgencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/agency")
@RequiredArgsConstructor
public class AgencyController {
    private final AgencyService agencyService;

    @GetMapping
    public ResponseEntity<List<Agency>> getAllAgencies() {
        return ResponseEntity.ok(agencyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agency> getAgencyById(@PathVariable("id") Integer id){
        Agency agency = agencyService.findById(id);
        return new ResponseEntity<Agency>(agency, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Agency> createAgency(@Valid @RequestBody Agency agency) {
        Agency newAgency = agencyService.createAgency(agency);
        return new ResponseEntity<Agency>(newAgency, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agency> updateAgency(@PathVariable Integer id, @RequestBody Agency agency) {
        Agency updatedAgency = agencyService.updateAgency(id, agency);
        return ResponseEntity.ok(updatedAgency);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgency(@PathVariable("id") Integer id){
        agencyService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
