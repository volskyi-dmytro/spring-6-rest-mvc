package com.stpunk.spring_6_rest_mvc.controller;

import com.stpunk.spring_6_rest_mvc.entities.Beer;
import com.stpunk.spring_6_rest_mvc.entities.Customer;
import com.stpunk.spring_6_rest_mvc.model.BeerDTO;
import com.stpunk.spring_6_rest_mvc.model.CustomerDTO;
import com.stpunk.spring_6_rest_mvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testSaveNewCustomer() {

        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerName("New Customer")
                .build();

        ResponseEntity responseEntity = customerController.postCustomer(customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[0]);

        Customer customer = customerRepository.findById(savedUUID).get();
        assertThat(customer).isNotNull();

    }

    @Test
    void testListCustomers() {
        List<CustomerDTO> dtos = customerController.getCustomers();

        assertThat(dtos.size()).isEqualTo(3);

    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {

        customerRepository.deleteAll();

        List<CustomerDTO> dtos = customerController.getCustomers();

        assertThat(dtos.size()).isEqualTo(0);

    }

    @Test
    void testGetById() {

        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO dto = customerController.getCustomerById(customer.getCustomerId());

        assertThat(dto).isNotNull();
    }

    @Test
    void testCustomerIdNotFound() {

        assertThrows(NotFoundException.class, () -> {
            customerController.getCustomerById(UUID.randomUUID());
        });
    }
}