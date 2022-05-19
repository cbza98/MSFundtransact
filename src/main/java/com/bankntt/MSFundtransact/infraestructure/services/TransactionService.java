package com.bankntt.MSFundtransact.infraestructure.services;

import com.bankntt.MSFundtransact.application.exception.AccountNotCreatedException;
import com.bankntt.MSFundtransact.domain.beans.*;
import com.bankntt.MSFundtransact.domain.entities.Transaction;
import com.bankntt.MSFundtransact.domain.enums.TransactionType;
import com.bankntt.MSFundtransact.domain.repository.TransactionRepository;
import com.bankntt.MSFundtransact.infraestructure.interfaces.IAccountTransactionsService;
import com.bankntt.MSFundtransact.infraestructure.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class TransactionService implements ITransactionService, IAccountTransactionsService {

    //Attribute
    @Autowired
    TransactionRepository trepository;
    @Autowired
    AccountService accountService;

    //Master
    @Override
    public Flux<Transaction> findAll() {
        return null;
    }

    @Override
    public Mono<Transaction> createSavingAccount(SavingAccountDTO account) {
        return null;
    }

    @Override
    public Mono<Transaction> createTimeDepositAccount(TimeDepositAccountDTO account) {
        return null;
    }

    @Override
    public Mono<Transaction> createPeopleCheckingAccount(PeopleCheckingAccountDTO account) {
        return null;
    }

    @Override
    public Mono<Transaction> createCompanyCheckingAccount(CompanyCheckingAccountDTO account) {
        return null;
    }

    @Override
    public Mono<Transaction> delete(String id) {
        return null;
    }

    @Override
    public Mono<Transaction> findById(String id) {
        return null;
    }

    @Override
    public Mono<ResponseEntity<Transaction>> update(String id, Transaction request) {
        return null;
    }

    @Override
    public Flux<Transaction> saveAll(List<Transaction> a) {
        return null;
    }
    //Account

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
    public Mono<Transaction> TransferBetweenAccounts(AccountTransferenceDTO dto) {
        return null;
    }

    @Override
    public Mono<Transaction> doTransferToThirdParty(AccountTransferenceDTO dto) {
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
        accountService.updatebalancedp(t.getToaccount(),t.getAmount());
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
        accountService.updatebalancewt(t.getToaccount(),t.getAmount());
        return _t;
    };
}
