package com.vash.entel.mapper;

import com.vash.entel.dto.CustomerDTO;
import com.vash.entel.model.entity.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public class CustomerMapper {

    private final ModelMapper modelMapper;

    public CustomerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CustomerDTO toCustomerDTO(Customer customer){
        CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);
        return customerDTO;
    }

    public Customer toCustomerEntity(CustomerDTO customerDTO) {
        return modelMapper.map(customerDTO, Customer.class);
    }
}