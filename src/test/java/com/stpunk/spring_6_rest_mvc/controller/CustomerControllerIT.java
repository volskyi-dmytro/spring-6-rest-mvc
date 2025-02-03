package com.stpunk.spring_6_rest_mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stpunk.spring_6_rest_mvc.entities.Customer;
import com.stpunk.spring_6_rest_mvc.mappers.CustomerMapper;
import com.stpunk.spring_6_rest_mvc.model.CustomerDTO;
import com.stpunk.spring_6_rest_mvc.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.stpunk.spring_6_rest_mvc.controller.CustomerController.CUSTOMER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ActiveProfiles("default")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testPatchCustomerBadName() throws Exception {

        Customer customer = customerRepository.findAll().get(0);

        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("name", "New nameNew nameNew nameNew nameNew nameNew nameNew nameNew nameNew name");

        MvcResult mvcResult = mockMvc.perform(patch(CUSTOMER_PATH_ID, customer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testDeleteByIdNotFound() {

        assertThrows(NotFoundException.class, () -> {
            customerController.deleteById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void deleteByIdFound() {

        Customer customer = customerRepository.findAll().get(0);

        ResponseEntity responseEntity = customerController.deleteById(customer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(customerRepository.findById(customer.getId())).isEmpty();

    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.updateById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void updateCustomerTest() {

        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);

        final String customerName = "UPDATED";
        customerDTO.setName(customerName);

        ResponseEntity responseEntity = customerController.updateById(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getName()).isEqualTo(customerName);

    }

    @Rollback
    @Transactional
    @Test
    void testSaveNewCustomer() {

        CustomerDTO customerDTO = CustomerDTO.builder()
                .name("New Customer")
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

        CustomerDTO dto = customerController.getCustomerById(customer.getId());

        assertThat(dto).isNotNull();
    }

    @Test
    void testCustomerIdNotFound() {

        assertThrows(NotFoundException.class, () -> {
            customerController.getCustomerById(UUID.randomUUID());
        });
    }
}