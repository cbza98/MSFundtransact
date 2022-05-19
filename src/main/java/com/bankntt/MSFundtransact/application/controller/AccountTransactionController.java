package com.bankntt.MSFundtransact.application.controller;

import com.bankntt.MSFundtransact.domain.beans.AccountOperationDTO;
import com.bankntt.MSFundtransact.domain.entities.Transaction;
import com.bankntt.MSFundtransact.infraestructure.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/MsFundTransact/Actions/AccountTransactionService")
public class AccountTransactionController {
    @Autowired
    private TransactionService service;
    @PostMapping("/Deposit")
    public Mono<ResponseEntity<Map<String, Object>>> Deposit(@Valid @RequestBody Mono<AccountOperationDTO> request) {

        Map<String, Object> response = new HashMap<>();

        return request.flatMap(a -> service.doAccountDeposit(a).map(c -> {
            response.put("Withdrawal", c);
            response.put("message", "Successful Deposit Transaction ");
            return ResponseEntity.created(URI.create("/MsFundTransact/Entities/Transaction/".concat(c.getTransactionId())))
                    .contentType(MediaType.APPLICATION_JSON).body(response);
        }));
    }
    @PostMapping("/Withdrawal")
    public Mono<ResponseEntity<Map<String, Object>>> Withdrawal(@Valid @RequestBody Mono<AccountOperationDTO> request) {

        Map<String, Object> response = new HashMap<>();

        return request.flatMap(a -> service.doAccountWithdrawal(a).map(c -> {
            response.put("Withdrawal", c);
            response.put("message", "Successful Withdrawal Transaction");
            return ResponseEntity.created(URI.create("/MsFundTransact/Entities/Transaction/".concat(c.getTransactionId())))
                    .contentType(MediaType.APPLICATION_JSON).body(response);
        }));
    }
}
