package com.vash.entel.service;

import com.vash.entel.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {

    void validateCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> getAll();
}
