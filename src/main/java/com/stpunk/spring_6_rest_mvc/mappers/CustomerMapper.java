package com.stpunk.spring_6_rest_mvc.mappers;

import com.stpunk.spring_6_rest_mvc.entities.Customer;
import com.stpunk.spring_6_rest_mvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDTOtoCustomer(CustomerDTO dto);

    CustomerDTO customerToCustomerDTO(Customer customer);

}
