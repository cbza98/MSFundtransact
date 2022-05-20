package com.bankntt.msfundtransact.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bankntt.msfundtransact.domain.entities.Credit;
import com.bankntt.msfundtransact.infraestructure.services.CreditService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
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

        return request.flatMap(a -> service.save(a).map(c -> {
            response.put("Credit", c);
            response.put("mensaje", "Cuenta creada con exito");
            return ResponseEntity.created(URI.create("/api/Credit/".concat(c.getCreditid())))
                    .contentType(MediaType.APPLICATION_JSON).body(response);
        }));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> delete(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();

        return service.delete(id)
                .map(c -> {
                    response.put("CreditCard", c);
                    response.put("message", "Successful Credit Card Deleted");
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .location( URI.create("/api/BusinessPartner/".concat(c.getCreditid())))
                            .body(response);
                });
    }







}
