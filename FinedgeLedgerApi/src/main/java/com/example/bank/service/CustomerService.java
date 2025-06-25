package com.example.bank.service;

import com.example.bank.domain.Customer;
import com.example.bank.dto.CreateCustomerDTO;
import com.example.bank.dto.CustomerDTO;
import com.example.bank.dto.UpdateCustomerDTO;
import com.example.bank.exception.*;
import com.example.bank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repo;

    /* ---------- CREATE ---------- */

    public CustomerDTO create(CreateCustomerDTO dto) {
        if (repo.findByFiscalCode(dto.fiscalCode()).isPresent())
            throw new DuplicateFiscalCodeException("Codice fiscale già registrato");
        if (repo.findByEmail(dto.email()).isPresent())
            throw new DuplicateEmailException("Email già registrata");

        Customer c = Customer.builder()
                .id(UUID.randomUUID())
                .fullName(dto.fullName())
                .email(dto.email())
                .fiscalCode(dto.fiscalCode())
                .build();
        return toDTO(repo.save(c));
    }

    /* ---------- READ ---------- */

    public List<CustomerDTO> findAll() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    public CustomerDTO get(UUID id) {
        return toDTO(repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente")));
    }

    public CustomerDTO getByFiscalCode(String cf) {
        return toDTO(repo.findByFiscalCode(cf)
                .orElseThrow(() -> new NotFoundException("Cliente")));
    }

    /* ---------- UPDATE ---------- */

    public CustomerDTO update(UUID id, UpdateCustomerDTO dto) {
        Customer c = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente"));

        if (!c.getFiscalCode().equalsIgnoreCase(dto.fiscalCode()) &&
                repo.findByFiscalCode(dto.fiscalCode()).isPresent())
            throw new DuplicateFiscalCodeException("Codice fiscale già registrato");

        if (!c.getEmail().equalsIgnoreCase(dto.email()) &&
                repo.findByEmail(dto.email()).isPresent())
            throw new DuplicateEmailException("Email già registrata");

        c.setFullName(dto.fullName());
        c.setEmail(dto.email());
        c.setFiscalCode(dto.fiscalCode());

        return toDTO(repo.save(c));
    }

    /* ---------- helper ---------- */

    private CustomerDTO toDTO(Customer c) {
        return new CustomerDTO(
                c.getId(),
                c.getFullName(),
                c.getEmail(),
                c.getFiscalCode()
        );
    }
}
