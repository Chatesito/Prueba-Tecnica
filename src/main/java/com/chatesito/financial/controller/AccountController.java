package com.chatesito.financial.controller;

import com.chatesito.financial.dto.account.AccountRequestDTO;
import com.chatesito.financial.dto.account.AccountResponseDTO;
import com.chatesito.financial.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    public AccountController(AccountService accountService) { this.accountService = accountService; }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> create(@Valid @RequestBody AccountRequestDTO request) {
        AccountResponseDTO created = accountService.create(request);
        return ResponseEntity.created(URI.create("/accounts/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getById(id));
    }

    @GetMapping(params = "clientId")
    public ResponseEntity<List<AccountResponseDTO>> getByClient(@RequestParam Long clientId) {
        return ResponseEntity.ok(accountService.getByClient(clientId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AccountRequestDTO request) {
        return ResponseEntity.ok(accountService.update(id, request));
    }

    // Change state: Activa, Inactiva, Cancelada
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable Long id, @RequestParam String status) {
        accountService.changeStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}
