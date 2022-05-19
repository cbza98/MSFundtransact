package com.bankntt.MSFundtransact.application.controller;

import com.bankntt.MSFundtransact.domain.entities.Account;
import com.bankntt.MSFundtransact.domain.entities.Credit;
import com.bankntt.MSFundtransact.infraestructure.services.AccountService;
import com.bankntt.MSFundtransact.infraestructure.services.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/Credit")
public class CreditController {
    @Autowired
    private CreditService service;


    @GetMapping
    public Mono<ResponseEntity<Flux<Credit>>> FindAll() {
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(service.findAll()));
    }

    @GetMapping("/{id}")
    public Mono<Credit> findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> Create(@Valid @RequestBody Mono<Credit> request) {

        Map<String, Object> response = new HashMap<>();

        return request.flatMap(a -> {
            return service.save(a).map(c -> {
                response.put("Credit", c);
                response.put("mensaje", "Cuenta creada con exito");
                return ResponseEntity.created(URI.create("/api/Credit/".concat(c.getCreditid())))
                        .contentType(MediaType.APPLICATION_JSON).body(response);
            });
        }).onErrorResume(t -> {
            return Mono.just(t).cast(WebExchangeBindException.class).flatMap(e -> Mono.just(e.getFieldErrors()))
                    .flatMapMany(Flux::fromIterable).map(fieldError -> "Message Validation Entity: "
                            + fieldError.getField() + " " + fieldError.getDefaultMessage())
                    .collectList().flatMap(list -> {

                        response.put("errors", list);
                        response.put("timestamp", new Date());
                        response.put("status", HttpStatus.BAD_REQUEST.value());

                        return Mono.just(ResponseEntity.badRequest().body(response));

                    });

        });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> Delete(@PathVariable String id) {
        return service.delete(id).map(r -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }







}
