package com.bankntt.msfundtransact.application.controller;

import com.bankntt.msfundtransact.domain.beans.AccountTransferDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bankntt.msfundtransact.domain.beans.AccountOperationDTO;
import com.bankntt.msfundtransact.infraestructure.services.TransactionService;

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
    public Mono<ResponseEntity<Map<String, Object>>> deposit(@Valid @RequestBody Mono<AccountOperationDTO> request) {

        Map<String, Object> response = new HashMap<>();

        return request.flatMap(a -> service.doAccountDeposit(a).map(c -> {
            response.put("Withdrawal", c);
            response.put("message", "Successful Deposit Transaction ");
            return ResponseEntity.created(URI.create("/MsFundTransact/Entities/Transaction/".concat(c.getTransactionId())))
                    .contentType(MediaType.APPLICATION_JSON).body(response);
        }));
    }
    @PostMapping("/Withdrawal")
    public Mono<ResponseEntity<Map<String, Object>>> withdrawal(@Valid @RequestBody Mono<AccountOperationDTO> request) {

        Map<String, Object> response = new HashMap<>();

        return request.flatMap(a -> service.doAccountWithdrawal(a).map(c -> {
            response.put("Withdrawal", c);
            response.put("message", "Successful Withdrawal Transaction");
            return ResponseEntity.created(URI.create("/MsFundTransact/Entities/Transaction/".concat(c.getTransactionId())))
                    .contentType(MediaType.APPLICATION_JSON).body(response);
        }));
    }

    @PostMapping("/TransferSameHolder")
    public Mono<ResponseEntity<Map<String, Object>>> TransferSameHolder(@Valid @RequestBody Mono<AccountTransferDTO> request) {

        Map<String, Object> response = new HashMap<>();

        return request.flatMap(a -> service.TransferBetweenAccounts(a).map(c -> {
            response.put("Transfer Same Holder", c);
            response.put("message", "Successful Transfer Same Holder");
            return ResponseEntity.created(URI.create("/MsFundTransact/Entities/Transaction/".concat(c.getTransactionId())))
                    .contentType(MediaType.APPLICATION_JSON).body(response);
        }));
    }

    @PostMapping("/TransferToThirdParty")
    public Mono<ResponseEntity<Map<String, Object>>> TransferToThirdParty(@Valid @RequestBody Mono<AccountTransferDTO> request) {

        Map<String, Object> response = new HashMap<>();

        return request.flatMap(a -> service.doTransferToThirdParty(a).map(c -> {
            response.put("Transfer Same Holder", c);
            response.put("message", "Successful Transfer Same Holder");
            return ResponseEntity.created(URI.create("/MsFundTransact/Entities/Transaction/".concat(c.getTransactionId())))
                    .contentType(MediaType.APPLICATION_JSON).body(response);
        }));
    }


}
