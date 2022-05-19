package com.bankntt.MSFundtransact.infraestructure.services;

import com.bankntt.MSFundtransact.domain.entities.CreditCard;
import com.bankntt.MSFundtransact.infraestructure.interfaces.ICreditCardService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CreditCardService implements ICreditCardService {

    @Override
    public Flux<CreditCard> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Mono<CreditCard> save(CreditCard _entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Mono<CreditCard> delete(String Id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Mono<CreditCard> findById(String Id) {
        // TODO Auto-generated method stub
        return null;
    }

}
