package com.vash.entel.service.impl;


import com.vash.entel.dto.CustomerDTO;
import com.vash.entel.repository.CustomerRepository;
import com.vash.entel.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDTO findByDocNumber(Long docNumber) {
        // Buscar el documento en la base de datos.
        return customerRepository.findByDocNumber(docNumber)
                .map(customer -> {
                    CustomerDTO dto = new CustomerDTO();
                    dto.setDocNumber(customer.getDocNumber());
                    dto.setFullname(customer.getFullname());  // El controlador convertirá el nombre a mayúsculas
                    return dto;
                })
                .orElse(null); // Si no se encuentra, retorna null
    }
}
