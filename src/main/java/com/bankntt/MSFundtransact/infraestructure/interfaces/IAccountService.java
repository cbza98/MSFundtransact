package com.bankntt.MSFundtransact.infraestructure.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.bankntt.MSFundtransact.domain.entities.Account;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAccountService {
	public Flux<Account> findAll();

	public Mono<Account> save(Account _account);
	
	public Mono<Account> delete(String Id);

	public Mono<Account> findById(String Id);
	
	public Mono<ResponseEntity<Account>> update(String id, Account _request);
	
	public Flux<Account> saveAll(List<Account> a);

}
