package com.bankntt.MSFundtransact.application.controller;

import com.bankntt.MSFundtransact.domain.entities.Transaction;
import com.bankntt.MSFundtransact.infraestructure.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
