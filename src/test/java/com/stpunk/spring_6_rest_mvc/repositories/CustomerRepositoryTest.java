package com.stpunk.spring_6_rest_mvc.repositories;

import com.stpunk.spring_6_rest_mvc.entities.Customer;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("default")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testSaveCustomer() {

        Customer savedCustomer = customerRepository.save(Customer.builder()
                .name("New Customer")
                .build());
        customerRepository.flush();

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();

    }

    @Test
    void testSaveCustomerTooLongName() {

        assertThrows(ConstraintViolationException.class, () -> {

            Customer savedCustomer = customerRepository.save(Customer.builder()
                    .name("New CustomerNew CustomerNew CustomerNew CustomerNew CustomerNew Customer")
                    .build());
            customerRepository.flush();

        });
    }
}