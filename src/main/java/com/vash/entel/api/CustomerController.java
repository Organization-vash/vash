package com.vash.entel.api;

import com.vash.entel.dto.CustomerDTO;
import com.vash.entel.exception.BadRequestException;
import com.vash.entel.exception.ResourceNotFoundException;
import com.vash.entel.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/valida")
    public ResponseEntity<Map<String, String>> validateCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            // Llamamos al servicio para validar el cliente
            customerService.validateCustomer(customerDTO);

            // Si no hay excepciones, la validación fue exitosa
            response.put("message", "Validación exitosa. Cliente encontrado.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BadRequestException e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCategories(){
        return ResponseEntity.ok(customerService.getAll());
    }
}
