package com.stpunk.spring_6_rest_mvc.repositories;

import com.stpunk.spring_6_rest_mvc.entities.Customer;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testSaveCustomer() {

        Customer savedCustomer = customerRepository.save(Customer.builder()
                .customerName("New Customer")
                .build());
        customerRepository.flush();

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getCustomerId()).isNotNull();

    }

    @Test
    void testSaveCustomerTooLongName() {

        assertThrows(ConstraintViolationException.class, () -> {

            Customer savedCustomer = customerRepository.save(Customer.builder()
                    .customerName("New CustomerNew CustomerNew CustomerNew CustomerNew CustomerNew Customer")
                    .build());
            customerRepository.flush();

        });
    }
}