package com.chatesito.financial.controller;

import com.chatesito.financial.dto.client.ClientRequestDTO;
import com.chatesito.financial.dto.client.ClientResponseDTO;
import com.chatesito.financial.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClientController.class)
@Import(ClientControllerTest.TestConfig.class)
public class ClientControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ClientService clientService() {
            return Mockito.mock(ClientService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        ClientResponseDTO resp = new ClientResponseDTO();
        resp.setId(10L);
        Mockito.when(clientService.create(any(ClientRequestDTO.class))).thenReturn(resp);

        ClientRequestDTO req = new ClientRequestDTO();
        req.setFirstName("Carlos");
        req.setLastName("Pérez");
        req.setEmail("carlos@example.com");
        req.setIdentificationType("CC");
        req.setIdentificationNumber("12345678901");
        req.setBirthDate(LocalDate.of(1990, 5, 10));

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/clients/10")));
    }

    @Test
    void patch_shouldReturn200() throws Exception {
        ClientResponseDTO resp = new ClientResponseDTO();
        resp.setId(5L);
        Mockito.when(clientService.update(any(Long.class), any(ClientRequestDTO.class))).thenReturn(resp);

        ClientRequestDTO req = new ClientRequestDTO();
        req.setFirstName("Ana");
        req.setLastName("Díaz");
        req.setEmail("ana@example.com");
        req.setIdentificationType("CC");
        req.setIdentificationNumber("99999999999");
        req.setBirthDate(LocalDate.of(1992, 1, 1));

        mockMvc.perform(patch("/clients/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }
}
