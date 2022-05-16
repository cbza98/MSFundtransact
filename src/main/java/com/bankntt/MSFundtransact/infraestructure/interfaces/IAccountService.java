package com.bankntt.MSFundtransact.infraestructure.interfaces;

import com.bankntt.MSFundtransact.domain.entities.Account;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAccountService {
	public Flux<Account> findAll();

	public Mono<Account> save(Account _account);

	public Mono<Account> delete(String Id);

	public Mono<Account> findById(String Id);

}
