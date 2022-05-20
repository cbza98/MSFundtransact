package com.bankntt.msfundtransact.infraestructure.interfaces;
import com.bankntt.msfundtransact.domain.beans.CreateCreditCardDTO;
import com.bankntt.msfundtransact.domain.entities.CreditCard;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ICreditCardService {
	public Flux<CreditCard> findAll();

	public Mono<CreditCard> save(CreditCard _entity);

    Mono<CreditCard> createCreditCard(CreateCreditCardDTO _entity);

    public Mono<CreditCard> delete(String Id);

	public Mono<CreditCard> findById(String Id);

}
