package com.bankntt.msfundtransact.infraestructure.interfaces;
import com.bankntt.msfundtransact.domain.beans.CreateCreditCardDTO;
import com.bankntt.msfundtransact.domain.entities.CreditCard;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ICreditCardService {
    Flux<CreditCard> findAll();

    Mono<CreditCard> save(CreditCard _entity);

    Mono<CreditCard> createCreditCard(CreateCreditCardDTO _entity);

    Mono<CreditCard> delete(String Id);

    Mono<CreditCard> findById(String Id);

}
