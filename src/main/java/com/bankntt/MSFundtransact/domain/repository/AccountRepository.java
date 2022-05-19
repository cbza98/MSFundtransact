package com.bankntt.MSFundtransact.domain.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bankntt.MSFundtransact.domain.entities.Account;

import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
	Mono<Long> countByAccountTypeAndCodeBusinessPartner(String Tipo, String Code);
	Mono<Account> findByAccountNumber(String number);
}
