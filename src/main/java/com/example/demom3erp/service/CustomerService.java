package com.example.demom3erp.service;


import com.example.demom3erp.dto.CustomerDto;
import com.example.demom3erp.entity.Customer;
import com.example.demom3erp.exception.ResourceNotFoundException;
import com.example.demom3erp.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    private CustomerRepository customerRepository;

    // Convert Customer entity to CustomerDto
    private CustomerDto convertToDto(Customer customer) {
        return new CustomerDto(customer.getId(),customer.getName(),customer.getEmail(),customer.getPhone()
        );
    }

    // Convert CustomerDto to Customer entity
    private Customer convertToEntity(CustomerDto customerDto) {
        return new Customer(customerDto.getName(), customerDto.getEmail());
    }

    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        logger.info("Fetching all Customers");
        return customerRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long customerId) {
        logger.info("Fetching all customer by Id: {}",customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        return convertToDto(customer);
    }

    @Transactional
    public CustomerDto createCustomer(CustomerDto customerDto) {
        logger.info("Creating a new Customer");
        Customer customer = convertToEntity(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDto(savedCustomer);
    }

    @Transactional
    public CustomerDto getCustomerByEmail(String email) {
        logger.info("Fetching customer by email: {}",email);
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) {
            throw new ResourceNotFoundException("Customer not found with email: " + email);
        }
        return convertToDto(customer);
    }

    @Transactional
    public CustomerDto updateCustomer(Long customerId, CustomerDto customerDto) {
        logger.info("Updating customer with id: {}", customerId);
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        existingCustomer.setName(customerDto.getName());
        existingCustomer.setEmail(customerDto.getEmail());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return convertToDto(updatedCustomer);
    }

    @Transactional
    public void deleteCustomer(Long customerId) {
        logger.info("Deleting customer with id: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        customer.setDeletedFlag(true);
        customerRepository.delete(customer);
    }
}

