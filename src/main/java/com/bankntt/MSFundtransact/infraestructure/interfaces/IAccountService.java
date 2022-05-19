package com.bankntt.MSFundtransact.infraestructure.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.bankntt.MSFundtransact.domain.beans.CompanyCheckingAccountDTO;
import com.bankntt.MSFundtransact.domain.beans.PeopleCheckingAccountDTO;
import com.bankntt.MSFundtransact.domain.beans.SavingAccountDTO;
import com.bankntt.MSFundtransact.domain.beans.TimeDepositAccountDTO;
import com.bankntt.MSFundtransact.domain.entities.Account;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAccountService {
	public Flux<Account> findAll();

	public Mono<Account> createSavingAccount(SavingAccountDTO account);
	
	public Mono<Account> createTimeDepositAccount(TimeDepositAccountDTO account);
	
	public Mono<Account> createPeopleCheckingAccount(PeopleCheckingAccountDTO account);
	
	public Mono<Account> createCompanyCheckingAccount(CompanyCheckingAccountDTO account);
	
	public Mono<Account> delete(String id);

	public Mono<Account> findById(String id);
	
	public Mono<ResponseEntity<Account>> update(String id, Account request);
	
	public Flux<Account> saveAll(List<Account> a);

}
