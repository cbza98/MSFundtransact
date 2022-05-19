package com.bankntt.MSFundtransact.infraestructure.interfaces;

import com.bankntt.MSFundtransact.domain.entities.Account;
import com.bankntt.MSFundtransact.domain.entities.Credit;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICreditService {
    public Flux<Credit> findAll();

    public Mono<Credit> save(Credit _account);

    public Mono<Credit> delete(String Id);

    public Mono<Credit> findById(String Id);


}
