package com.bankntt.msfundtransact.infraestructure.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bankntt.msfundtransact.application.exception.AccountNotCreatedException;
import com.bankntt.msfundtransact.domain.beans.*;
import com.bankntt.msfundtransact.domain.entities.Transaction;
import com.bankntt.msfundtransact.domain.enums.TransactionType;
import com.bankntt.msfundtransact.domain.repository.TransactionRepository;
import com.bankntt.msfundtransact.infraestructure.interfaces.ITransactionService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class TransactionService implements ITransactionService{

    //Attribute
    @Autowired
    TransactionRepository trepository;
    @Autowired
    AccountService accountService;

    //Crud
    @Override
	public Flux<Transaction> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Transaction> delete(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Transaction> findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<ResponseEntity<Transaction>> update(String id, Transaction request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Flux<Transaction> saveAll(List<Transaction> a) {
		// TODO Auto-generated method stub
		return null;
	}
    
    //Transaction

    @Override
    public Mono<Transaction> doAccountDeposit(AccountOperationDTO dto) {

        return accountService.findByAccountNumber(dto.getAccount()).then(Mono.just(dto)
                        .flatMap(savedeposit))
                .switchIfEmpty(Mono.error(new AccountNotCreatedException()));

    }
    @Override
    public Mono<Transaction> doAccountWithdrawal(AccountOperationDTO dto) {

        return accountService.findByAccountNumber(dto.getAccount()).then(Mono.just(dto)
                        .flatMap(savewithdrawal))
                .switchIfEmpty(Mono.error(new AccountNotCreatedException()));

    }

    @Override
    public Mono<Transaction> TransferBetweenAccounts(AccountTransferDTO dto) {
        return null;
    }

    @Override
    public Mono<Transaction> doTransferToThirdParty(AccountTransferDTO dto) {
        return null;
    }



    //Metodos Funcionales
    private final Function<AccountOperationDTO, Mono<Transaction>> savedeposit = deposit -> {

        Transaction t;
        Mono<Transaction> _t;
        t = Transaction.builder()
                .amount(deposit.getAmount())
                .toaccount(deposit.getAccount())
                .transactiontype(TransactionType.DEPOSIT)
                .createDate(new Date()).build();

         _t =  trepository.save(t);
        accountService.updateBalanceDp(t.getToaccount(),t.getAmount());
        return _t;
    };
    
    private final Function<AccountOperationDTO, Mono<Transaction>> savewithdrawal = deposit -> {

        Transaction t;
        Mono<Transaction> _t;
        t = Transaction.builder()
                .amount(deposit.getAmount())
                .toaccount(deposit.getAccount())
                .transactiontype(TransactionType.DEPOSIT)
                .createDate(new Date()).build();

        _t =  trepository.save(t);
        accountService.updateBalanceWt(t.getToaccount(),t.getAmount());
        return _t;
    };

	
}
