package com.bankntt.msfundtransact.application.controller;

import com.bankntt.msfundtransact.domain.beans.*;
import com.bankntt.msfundtransact.infraestructure.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/MsFundTransact/Actions/CreditTransactionService")
public class CreditTransactionController {
    @Autowired
    private TransactionService service;
    @PostMapping("/Creditcardpayment")
    //Pagos Tarjetas de Credito
    public Mono<ResponseEntity<Map<String, Object>>> Creditcardpayment(@Valid @RequestBody Mono<CreditCardPaymentDTO> request) {

        Map<String, Object> response = new HashMap<>();

        return request.flatMap(a -> service.doCreditCardPayment(a).map(c -> {
            response.put("Credit Card Payment", c);
            response.put("message", "Successful Credit Card Payment ");
            return ResponseEntity.created(URI.create("/MsFundTransact/Entities/Transaction/".concat(c.getTransactionId())))
                    .contentType(MediaType.APPLICATION_JSON).body(response);
        }));
    }
    //Consumos Tarjetas de Credito
    @PostMapping("/Creditcardconsumption")
    public Mono<ResponseEntity<Map<String, Object>>> Creditcardconsumption(@Valid @RequestBody Mono<CreditcardConsumptionDTO> request) {

        Map<String, Object> response = new HashMap<>();

        return request.flatMap(a -> service.doCreditcardConsumption(a).map(c -> {
            response.put("Withdrawal", c);
            response.put("message", "Successful Credit Card Consumption");
            return ResponseEntity.created(URI.create("/MsFundTransact/Entities/Transaction/".concat(c.getTransactionId())))
                    .contentType(MediaType.APPLICATION_JSON).body(response);
        }));
    }

    @PostMapping("/Creditpayment")
    //Pagos de Credito
    public Mono<ResponseEntity<Map<String, Object>>> Creditpayment(@Valid @RequestBody Mono<CreditPaymentDTO> request) {

        Map<String, Object> response = new HashMap<>();

        return request.flatMap(a -> service.doCreditPayment(a).map(c -> {
            response.put("Credit Payment", c);
            response.put("message", "Successful Credit Payment ");
            return ResponseEntity.created(URI.create("/MsFundTransact/Entities/Transaction/".concat(c.getTransactionId())))
                    .contentType(MediaType.APPLICATION_JSON).body(response);
        }));
    }
    //Consumos de Credito
    @PostMapping("/Creditconsumption")
    public Mono<ResponseEntity<Map<String, Object>>> Creditconsumption(@Valid @RequestBody Mono<CreditConsumptionDTO> request) {

        Map<String, Object> response = new HashMap<>();

        return request.flatMap(a -> service.doCreditConsumption(a).map(c -> {
            response.put("Credit Consumption", c);
            response.put("message", "Successful Credit Consumption");
            return ResponseEntity.created(URI.create("/MsFundTransact/Entities/Transaction/".concat(c.getTransactionId())))
                    .contentType(MediaType.APPLICATION_JSON).body(response);
        }));
    }





}
