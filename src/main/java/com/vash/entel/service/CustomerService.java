package com.vash.entel.service;


import com.vash.entel.dto.CustomerDTO;

public interface CustomerService {
    CustomerDTO findByDocNumber(Long docNumber);
}
