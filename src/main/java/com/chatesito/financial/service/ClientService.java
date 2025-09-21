package com.chatesito.financial.service;

import com.chatesito.financial.dto.client.ClientRequestDTO;
import com.chatesito.financial.dto.client.ClientResponseDTO;

import java.util.List;

public interface ClientService {

    ClientResponseDTO create(ClientRequestDTO request);
    ClientResponseDTO getById(Long id);
    List<ClientResponseDTO> getAll();
    ClientResponseDTO update(Long id, ClientRequestDTO request);
    void delete(Long id);
}
