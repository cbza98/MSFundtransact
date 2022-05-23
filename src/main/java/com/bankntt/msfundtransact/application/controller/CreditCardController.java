package com.bankntt.msfundtransact.application.controller;

import com.bankntt.msfundtransact.domain.beans.CreditcardConsumptionDTO;
import com.bankntt.msfundtransact.infraestructure.services.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bankntt.msfundtransact.domain.beans.CreateCreditCardDTO;
import com.bankntt.msfundtransact.domain.entities.CreditCard;
import com.bankntt.msfundtransact.infraestructure.interfaces.ICreditCardService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/MsFundTransact/Entities/CreditCard")
public class CreditCardController {
    @Autowired
    private CreditCardService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<CreditCard>>> FindAll() {
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(service.findAll()));
    }

    @GetMapping("/{id}")
    public Mono<CreditCard> findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> Create(@Valid @RequestBody Mono<CreateCreditCardDTO> request) {

        Map<String, Object> response = new HashMap<>();

        return request
                .flatMap(a -> service.createCreditCard(a).map(c -> {
            response.put("CreditCard", c);
            response.put("message", "Credit Card Successfully created");
            return ResponseEntity.created(URI.create("/MsFundTransact/Entities/CrediCard".concat(c.getCardNumber())))
                    .contentType(MediaType.APPLICATION_JSON).body(response);
        }));
    }
    @PostMapping("/consumption")

    public Mono<ResponseEntity<Map<String, Object>>> Consumption(@Valid  @RequestBody Mono<CreditcardConsumptionDTO> request) {

        Map<String, Object> response = new HashMap<>();

        return request.flatMap(a -> service.updateconsumption(a.getCreditcard(),a.getConsumption()).map(c -> {
            response.put("CreditCard", c);
            response.put("message", "Credit Card Successfully created");
            return ResponseEntity.created(URI.create("/MsFundTransact/Entities/CrediCard".concat(c.getCardNumber())))
                    .contentType(MediaType.APPLICATION_JSON).body(response);
        }));
    }






    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> delete(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();

        return service.delete(id)
                .map(c -> {
                    response.put("CreditCard", c);
                    response.put("message", "Credit Card Successfully created");
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .location( URI.create("/MsFundTransact/Entities/CrediCard".concat(c.getCardNumber())))
                            .body(response);
                });
    }

}
