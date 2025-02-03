package com.stpunk.spring_6_rest_mvc.services;

import com.stpunk.spring_6_rest_mvc.mappers.CustomerMapper;
import com.stpunk.spring_6_rest_mvc.model.CustomerDTO;
import com.stpunk.spring_6_rest_mvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> getCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional
                .ofNullable(customerMapper
                        .customerToCustomerDTO(customerRepository.findById(id)
                                .orElse(null)));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {

        return customerMapper
                .customerToCustomerDTO(customerRepository
                        .save(customerMapper.customerDTOtoCustomer(customer)));
    }

    @Override
    public Optional<CustomerDTO> updateById(UUID customerId, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

        customerRepository.findById(customerId).ifPresentOrElse(foundCustomer -> {

            foundCustomer.setName(customer.getName());

            atomicReference.set(Optional.of(customerMapper
                    .customerToCustomerDTO(customerRepository.save(foundCustomer))));

        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteById(UUID customerId) {
        if(customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

        customerRepository.findById(customerId).ifPresentOrElse(foundCustomer -> {
            if(StringUtils.hasText(customer.getName())) {
                foundCustomer.setName(customer.getName());
            }
            atomicReference.set(Optional.of(customerMapper
                    .customerToCustomerDTO(customerRepository.save(foundCustomer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
}
