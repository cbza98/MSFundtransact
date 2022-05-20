package com.bankntt.MSFundtransact.infraestructure.interfaces;
import com.bankntt.MSFundtransact.domain.beans.CreateCreditCardDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.bankntt.MSFundtransact.domain.entities.CreditCard;


public interface ICreditCardService {
	public Flux<CreditCard> findAll();

	public Mono<CreditCard> save(CreditCard _entity);

    Mono<CreditCard> createCreditCard(CreateCreditCardDTO _entity);

    public Mono<CreditCard> delete(String Id);

	public Mono<CreditCard> findById(String Id);

}
