package com.example.bank.service;

import com.example.bank.domain.Customer;
import com.example.bank.dto.*;
import com.example.bank.exception.NotFoundException;
import com.example.bank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repo;

    public CustomerDTO create(CreateCustomerDTO dto) {
        Customer c = Customer.builder()
                .fullName(dto.fullName())
                .email(dto.email())
                .fiscalCode(dto.fiscalCode())
                .build();
        return toDto(repo.save(c));
    }

    public CustomerDTO get(UUID id) {
        return toDto(repo.findById(id).orElseThrow(() -> new NotFoundException("Customer")));
    }

    public List<CustomerDTO> list() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    private CustomerDTO toDto(Customer c) {
        return new CustomerDTO(c.getId(), c.getFullName(), c.getEmail(), c.getFiscalCode());
    }
}
