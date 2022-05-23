package com.bankntt.msfundtransact.infraestructure.services;

import com.bankntt.msfundtransact.domain.beans.*;
import com.bankntt.msfundtransact.domain.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bankntt.msfundtransact.application.exception.EntityNotExistsException;
import com.bankntt.msfundtransact.domain.entities.Credit;
import com.bankntt.msfundtransact.domain.repository.CreditRepository;
import com.bankntt.msfundtransact.infraestructure.interfaces.ICreditService;
import com.bankntt.msfundtransact.infraestructure.interfaces.ICreditTransactionService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class CreditService implements ICreditService {
    //Servicios Crud de Credito
    @Autowired
    CreditRepository repository;

    @Override
    public Flux<Credit> findAll() {

        return repository.findAll();
    }

    public Mono<Credit> save(Credit a) {
        WebClient client = WebClient.builder().baseUrl("http://localhost:8080/BusinessPartnerService")

                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
        return client.get()
                .uri(uriBuilder -> uriBuilder.path("/BusinessPartner/{id}").build(a.getCodebusinesspartner()))
                .retrieve().onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new EntityNotExistsException()))
                .bodyToMono(BusinessPartnerBean.class)
                .filter(r -> r.getType().equals("C") || r.getType().equals("P"))
                .flatMap(f -> {

                    //	a.getCreatedate(new Date().getDateInstance);
                    return repository.save(a);
                })
                .switchIfEmpty(Mono.error(new EntityNotExistsException())
                );// AccountNotCreatedException
    }

    @Override
    public Mono<Credit> delete(String Id) {

        return repository.findById(Id).flatMap(r -> repository.delete(r).then(Mono.just(r)));
    }

    @Override
    public Mono<Credit> findById(String Id) {
        // TODO Auto-generated method stub
        return repository.findById(Id);
    }
}
