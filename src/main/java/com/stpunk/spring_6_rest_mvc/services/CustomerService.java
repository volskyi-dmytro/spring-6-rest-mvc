package com.stpunk.spring_6_rest_mvc.services;

import com.stpunk.spring_6_rest_mvc.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    List<Customer> getCustomers();

    Customer getCustomerById(UUID id);
}
