package com.bankntt.msfundtransact.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankntt.msfundtransact.domain.entities.Transaction;
import com.bankntt.msfundtransact.infraestructure.services.TransactionService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/MsFundTransact/Entities/Transaction")
public class TransactionController {
    @Autowired
    private TransactionService service;
    @GetMapping("/{id}")
    public Mono<Transaction> findById(@PathVariable String id) {
        return service.findById(id);
    }
}
