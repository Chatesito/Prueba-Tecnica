package com.chatesito.financial.controller;

import com.chatesito.financial.dto.transaction.TransactionRequestDTO;
import com.chatesito.financial.dto.transaction.TransactionResponseDTO;
import com.chatesito.financial.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    public TransactionController(TransactionService transactionService) { this.transactionService = transactionService; }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> create(@Valid @RequestBody TransactionRequestDTO request) {
        TransactionResponseDTO created = transactionService.create(request);
        return ResponseEntity.created(URI.create("/transactions/" + created.getId())).body(created);
    }

    // Account status: List transactions by account
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> listByAccount(
            @RequestParam Long accountId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(transactionService.listByAccount(accountId, from, to));
    }
}
