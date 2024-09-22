package com.vash.entel.api;

import com.vash.entel.model.entity.Service;
import com.vash.entel.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/service")
public class ServiceController {
    private final ServiceService serviceService;

    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable("id") Integer id){
        Service service = serviceService.findById(id);
        return new ResponseEntity<Service>(service, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Service> createService(@RequestBody Service service){
        Service newService = serviceService.Create(service);
        return new ResponseEntity<Service>(newService,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Service> updateService(@PathVariable("id") Integer id, @RequestBody Service service){
        Service updateService = serviceService.Update(id, service);
        return new ResponseEntity<Service>(updateService,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Service> deleteService(@PathVariable("id") Integer id){
        serviceService.delete(id);
        return new ResponseEntity<Service>(HttpStatus.NO_CONTENT);
    }
}
