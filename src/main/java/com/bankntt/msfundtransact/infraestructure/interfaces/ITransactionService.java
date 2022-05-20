package com.bankntt.msfundtransact.infraestructure.interfaces;

import org.springframework.http.ResponseEntity;

import com.bankntt.msfundtransact.domain.beans.*;
import com.bankntt.msfundtransact.domain.entities.Transaction;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITransactionService {
	
	Flux<Transaction> findAll();
	
	Mono<Transaction> delete(String id);

	Mono<Transaction> findById(String id);
	
	Mono<ResponseEntity<Transaction>> update(String id, Transaction request);
	
	Flux<Transaction> saveAll(List<Transaction> a);

    Mono<Transaction> doAccountWithdrawal(AccountOperationDTO dto);
    
    Mono<Transaction> doAccountDeposit(AccountOperationDTO dto);
	
	Mono<Transaction> TransferBetweenAccounts(AccountTransferDTO dto);
	
	Mono<Transaction> doTransferToThirdParty(AccountTransferDTO dto);
}
