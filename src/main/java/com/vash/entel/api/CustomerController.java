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


@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/valida")
    public ResponseEntity<String> validateAndCompleteCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        Long docNumber = customerDTO.getDocNumber();
        int length = String.valueOf(docNumber).length();  // Convertir a String solo para contar los dígitos

        // Validar longitud mínima y máxima para todos los documentos
        if (length < 6 || length > 20) {
            throw new BadRequestException("Documento inválido. Debe tener entre 6 y 20 dígitos.");
        }

        // Validar si el documento tiene exactamente 8 dígitos (DNI)
        if (length == 8) {
            // Tratamos este caso como DNI
            CustomerDTO result = customerService.findByDocNumber(docNumber);
            if (result == null) {
                throw new ResourceNotFoundException("DNI no encontrado.");
            }

            // Convertir el nombre a mayúsculas
            String uppercasedName = result.getFullname().toUpperCase();
            return new ResponseEntity<>("Nombre completado para DNI: " + uppercasedName, HttpStatus.OK);
        }

        // Para otros tipos de documentos (6-20 dígitos)
        CustomerDTO result = customerService.findByDocNumber(docNumber);
        if (result == null) {
            throw new ResourceNotFoundException("Documento no encontrado.");
        }

        // Convertir el nombre a mayúsculas
        String uppercasedName = result.getFullname().toUpperCase();
        return new ResponseEntity<>("Nombre completado: " + uppercasedName, HttpStatus.OK);
    }
}
