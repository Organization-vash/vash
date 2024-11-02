package com.vash.entel.api;

import com.vash.entel.model.entity.Service;
import com.vash.entel.service.AddRemoveServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/addRemoveService")
public class AddRemoveServiceController {

    private final AddRemoveServiceService addRemoveServiceService;

    // Obtener todos los servicios asociados a una atención
    @GetMapping("/{attentionId}/services")
    public ResponseEntity<List<Service>> getServicesByAttention(@PathVariable Integer attentionId) {
        List<Service> services = addRemoveServiceService.getServices(attentionId);
        return ResponseEntity.ok(services);
    }

    // Agregar un servicio a una atención
    @PostMapping("/{attentionId}/addService")
    public ResponseEntity<Service> addServiceToAttention(@PathVariable Integer attentionId, @RequestParam Integer serviceId) {
        Service updatedService = addRemoveServiceService.addService(attentionId, serviceId);
        return ResponseEntity.ok(updatedService);
    }

    // Eliminar un servicio de una atención
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{attentionId}/removeService/{serviceId}")
    public ResponseEntity<Void> removeServiceFromAttention(@PathVariable Integer attentionId, @PathVariable Integer serviceId) {
        addRemoveServiceService.removeService(attentionId, serviceId);
        return ResponseEntity.noContent().build();
    }
}
