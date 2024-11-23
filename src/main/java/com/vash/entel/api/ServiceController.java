package com.vash.entel.api;

import com.vash.entel.dto.ServiceDTO;
import com.vash.entel.service.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/service")
@PreAuthorize("hasAnyRole('ADMIN', 'ADVISER','SUPERVISRO')")
public class ServiceController {
    private final ServiceService serviceService;

    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getServiceById(@PathVariable("id") Integer id){
        ServiceDTO serviceDTO = serviceService.findById(id);
        return new ResponseEntity<ServiceDTO>(serviceDTO, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ServiceDTO> getServiceByName(@PathVariable("name") String name) {
        ServiceDTO serviceDTO = serviceService.findByName(name);
        return new ResponseEntity<>(serviceDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ServiceDTO> createService(@Valid @RequestBody ServiceDTO serviceDTO){
        ServiceDTO newService = serviceService.Create(serviceDTO);
        return new ResponseEntity<ServiceDTO>(newService,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceDTO> updateService(@PathVariable("id") Integer id, @Valid @RequestBody ServiceDTO serviceDTO){
        ServiceDTO updateService = serviceService.Update(id, serviceDTO);
        return new ResponseEntity<ServiceDTO>(updateService,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable("id") Integer id){
        serviceService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
