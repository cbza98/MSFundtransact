package com.bankntt.MSFundtransact.infraestructure.interfaces;

import com.bankntt.MSFundtransact.domain.beans.*;
import com.bankntt.MSFundtransact.domain.entities.Account;
import com.bankntt.MSFundtransact.domain.entities.Transaction;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITransactionService {
	public Flux<Transaction> findAll();

	public Mono<Transaction> createSavingAccount(SavingAccountDTO account);
	
	public Mono<Transaction> createTimeDepositAccount(TimeDepositAccountDTO account);
	
	public Mono<Transaction> createPeopleCheckingAccount(PeopleCheckingAccountDTO account);
	
	public Mono<Transaction> createCompanyCheckingAccount(CompanyCheckingAccountDTO account);
	
	public Mono<Transaction> delete(String id);

	public Mono<Transaction> findById(String id);
	
	public Mono<ResponseEntity<Transaction>> update(String id, Transaction request);
	
	public Flux<Transaction> saveAll(List<Transaction> a);

    Mono<Transaction> doAccountWithdrawal(AccountOperationDTO dto);
}
