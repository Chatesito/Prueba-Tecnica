package com.chatesito.financial.service.impl;

import com.chatesito.financial.dto.client.ClientRequestDTO;
import com.chatesito.financial.dto.client.ClientResponseDTO;
import com.chatesito.financial.entity.Client;
import com.chatesito.financial.mapper.ClientMapper;
import com.chatesito.financial.repository.AccountRepository;
import com.chatesito.financial.repository.ClientRepository;
import com.chatesito.financial.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final ClientMapper clientMapper;

    public ClientServiceImpl(ClientRepository clientRepository,
                             AccountRepository accountRepository,
                             ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    @Transactional
    public ClientResponseDTO create(ClientRequestDTO request) {
        validateAdult(request.getBirthDate());

        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new DataIntegrityViolationException("El email ya está registrado");
        }
        if (clientRepository.existsByIdentificationNumber(request.getIdentificationNumber())) {
            throw new DataIntegrityViolationException("El número de identificación ya está registrado");
        }

        Client entity = clientMapper.toEntity(request);
        Client saved = clientRepository.save(entity);
        return clientMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDTO getById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado: " + id));
        return clientMapper.toResponseDTO(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponseDTO> getAll() {
        return clientMapper.toResponseDTO(clientRepository.findAll());
    }

    @Override
    @Transactional
    public ClientResponseDTO update(Long id, ClientRequestDTO request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado: " + id));
        if (request.getBirthDate() != null) {
            validateAdult(request.getBirthDate());
        }
        clientMapper.updateEntity(request, client);
        Client saved = clientRepository.save(client);
        return clientMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado: " + id));
        if (accountRepository.existsByClientId(id)) {
            throw new IllegalStateException("No se puede eliminar el cliente, tiene productos vinculados");
        }
        clientRepository.delete(client);
    }

    private void validateAdult(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria");
        }
        int years = Period.between(birthDate, LocalDate.now()).getYears();
        if (years < 18) {
            throw new IllegalArgumentException("El cliente debe ser mayor de edad (+ 18)");
        }
    }
}
