package com.vash.entel.service.impl;

import com.vash.entel.dto.CustomerDTO;
import com.vash.entel.exception.BadRequestException;
import com.vash.entel.exception.ResourceNotFoundException;
import com.vash.entel.mapper.CustomerMapper;
import com.vash.entel.model.entity.Customer;
import com.vash.entel.model.enums.DocumentType;
import com.vash.entel.repository.CustomerRepository;
import com.vash.entel.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional
    @Override
    public List<CustomerDTO> getAll() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customerMapper::toCustomerDTO)
                .toList();
    }

    @Override
    @Transactional
    public void validateCustomer(CustomerDTO customerDTO) {
        // Validar longitud del número de documento
        int numberLength = String.valueOf(customerDTO.getDocNumber()).length();
        System.out.println("Validando cliente: " + customerDTO);

        if (customerDTO.getDocumentType() == DocumentType.DNI && numberLength != 8) {
            throw new BadRequestException("El DNI debe tener exactamente 8 dígitos.");
        } else if ((customerDTO.getDocumentType() == DocumentType.PASAPORTE ||
                customerDTO.getDocumentType() == DocumentType.PTP ||
                customerDTO.getDocumentType() == DocumentType.CARNET_EXTRANJERIA)
                && (numberLength < 6 || numberLength > 20)) {
            throw new BadRequestException("El número de documento debe tener entre 6 y 20 dígitos para documentos extranjeros.");
        }

        // Validar que el nombre no esté vacío
        if (customerDTO.getFullname() == null || customerDTO.getFullname().trim().isEmpty()) {
            throw new BadRequestException("El nombre no puede estar vacío.");
        }

        // Verificar si ya existe un número de documento en la base de datos
        Customer existingCustomer = customerRepository.findByDocNumber(customerDTO.getDocNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Documento no encontrado."));

        // Agregar un log para verificar qué está pasando con los datos existentes
        System.out.println("Cliente encontrado en la base de datos: " + existingCustomer);

        // Validar que coincidan el nombre completo, el tipo de documento y el número de documento
        if (!existingCustomer.getFullname().equalsIgnoreCase(customerDTO.getFullname())) {
            throw new BadRequestException("El nombre no coincide con el documento.");
        }
        if (!existingCustomer.getDocumentType().equals(customerDTO.getDocumentType())) {
            throw new BadRequestException("El tipo de documento no coincide.");
        }

        // Si todos los campos coinciden, la validación es exitosa.
        // Convertir el nombre a mayúsculas para la respuesta final.
        customerDTO.setFullname(existingCustomer.getFullname().toUpperCase());
    }
}
