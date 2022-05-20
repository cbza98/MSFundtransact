package com.bankntt.msfundtransact.infraestructure.interfaces;

import com.bankntt.msfundtransact.domain.entities.Credit;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ICreditService {
    Flux<Credit> findAll();

    Mono<Credit> save(Credit _account);

    Mono<Credit> delete(String Id);

    Mono<Credit> findById(String Id);


}
