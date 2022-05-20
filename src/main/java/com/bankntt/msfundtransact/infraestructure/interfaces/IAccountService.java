package com.bankntt.msfundtransact.infraestructure.interfaces;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.bankntt.msfundtransact.domain.beans.CompanyCheckingAccountDTO;
import com.bankntt.msfundtransact.domain.beans.PeopleCheckingAccountDTO;
import com.bankntt.msfundtransact.domain.beans.SavingAccountDTO;
import com.bankntt.msfundtransact.domain.beans.TimeDepositAccountDTO;
import com.bankntt.msfundtransact.domain.entities.Account;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAccountService {
	Flux<Account> findAll();

	Mono<Account> createSavingAccount(SavingAccountDTO account);
	
	Mono<Account> createTimeDepositAccount(TimeDepositAccountDTO account);
	
	Mono<Account> createPeopleCheckingAccount(PeopleCheckingAccountDTO account);
	
	Mono<Account> createCompanyCheckingAccount(CompanyCheckingAccountDTO account);
	
	Mono<Account> delete(String id);

	Mono<Account> findById(String id);
	
	Mono<Account> findByAccountNumber(String id);
	
	Mono<Account> update(String id, Account request);
	
	Mono<Account> updateBalanceDp(String id, BigDecimal balance);
	
	Mono<Account> updateBalanceWt(String id, BigDecimal balance);

	Flux<Account> saveAll(List<Account> a);

}
