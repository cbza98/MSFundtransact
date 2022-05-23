package com.bankntt.msfundtransact.infraestructure.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bankntt.msfundtransact.application.exception.AccountNotCreatedException;
import com.bankntt.msfundtransact.application.exception.EntityNotExistsException;
import com.bankntt.msfundtransact.application.helpers.AccountGeneratorValues;
import com.bankntt.msfundtransact.domain.beans.BusinessPartnerBean;
import com.bankntt.msfundtransact.domain.beans.CompanyCheckingAccountDTO;
import com.bankntt.msfundtransact.domain.beans.PeopleCheckingAccountDTO;
import com.bankntt.msfundtransact.domain.beans.SavingAccountDTO;
import com.bankntt.msfundtransact.domain.beans.TimeDepositAccountDTO;
import com.bankntt.msfundtransact.domain.entities.Account;
import com.bankntt.msfundtransact.domain.repository.AccountRepository;
import com.bankntt.msfundtransact.infraestructure.interfaces.IAccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountService implements IAccountService {

    @Autowired
    AccountRepository repository;

    //methods

    // Cruds
    @Override
    public Flux<Account> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Account> delete(String Id) {
        return repository.findById(Id).flatMap(deleted -> repository.delete(deleted).then(Mono.just(deleted)))
        				 .switchIfEmpty(Mono.error(new EntityNotExistsException()));
    }

    @Override
    public Mono<Account> findById(String Id) {
        return repository.findById(Id);
    }

    @Override
    public Mono<Account> findByAccountNumber(String id) {
        return repository.findByAccountNumber(id);
    }
    public Flux<Account> saveAll(List<Account> a) {

        return repository.saveAll(a);
    }

    public Mono<Boolean> exist(String Id)
    {
        return repository.existsById(Id);
    }
    @Override
    public Mono<Account> update(String id, Account _request) {

        return repository.findById(id).flatMap(a -> {
                    a.setAccountName(_request.getAccountName());
                    a.setAccountNumber(_request.getAccountNumber());
                    a.setAccountType(_request.getAccountType());
                    a.setCodeBusinessPartner(_request.getCodeBusinessPartner());
                    a.setDate_Opened(_request.getDate_Opened());
                    a.setValid(_request.getValid());
                    return repository.save(a);
                }).switchIfEmpty(Mono.error(new EntityNotExistsException()));
    }
    // Balance
    @Override
    public Mono<Account> updateBalanceDp(String id, BigDecimal balance) {
        return repository.findById(id).flatMap(a ->
                {
                    a.setBalance(a.getBalance().add(balance));
                    return repository.save(a);
                });
    }
    
    @Override
    public Mono<Account> updateBalanceWt(String id, BigDecimal balance) {
        return repository.findById(id).flatMap(a ->
                {
                    a.setBalance(a.getBalance().subtract(balance));
                    return repository.save(a);
                });
    }
    //Create
    @Override
    public Mono<Account> createSavingAccount(SavingAccountDTO account) {

        return Mono.just(account).doOnNext(r -> getBusinessPartner.accept(r.getCodeBusinessPartner())).then(
                repository.countByAccountTypeAndCodeBusinessPartner("AH", account.getCodeBusinessPartner())
                        .filter(longThanZero)
                        .then(Mono.just(account)
                                  .flatMap(saveSavingAccount)
                                  .switchIfEmpty(Mono.error(new AccountNotCreatedException())))
        );
    }

    @Override
    public Mono<Account> createTimeDepositAccount(TimeDepositAccountDTO account) {

        return Mono.just(account)
        	   .doOnNext(r -> getBusinessPartner.accept(r.getCodeBusinessPartner()))
        	   .flatMap(saveTimeDepositAccount).switchIfEmpty(Mono.error(new AccountNotCreatedException()));
        	  
    }

    @Override
    public Mono<Account> createPeopleCheckingAccount(PeopleCheckingAccountDTO account) {

        return Mono.just(account)
         	   .doOnNext(r -> getBusinessPartner.accept(r.getCodeBusinessPartner()))
         	   .flatMap(savePeopleCheckingAccount).switchIfEmpty(Mono.error(new AccountNotCreatedException()));
    }

    @Override
    public Mono<Account> createCompanyCheckingAccount(CompanyCheckingAccountDTO account) {

        return Mono.just(account)
                .doOnSuccess(r -> getBusinessPartner.accept(r.getCodeBusinessPartner()))
          	   .flatMap(saveCompanyCheckingAccount).switchIfEmpty(Mono.error(new AccountNotCreatedException()));
    }
    //Functionals
    Predicate<Long> longThanZero = a -> (a == 0);

    private final Consumer<String> getBusinessPartner = businessPartnerId-> {

        WebClient businessPartnerClient = WebClient.builder().baseUrl("http://localhost:9090/BusinessPartnerService")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

        businessPartnerClient.get()
                .uri(uriBuilder -> uriBuilder.path("/BusinessPartner/{id}").build(businessPartnerId))
                .retrieve().onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new EntityNotExistsException()) )
                .bodyToMono(BusinessPartnerBean.class);

    };
    
    private final Function<SavingAccountDTO, Mono<Account>> saveSavingAccount = account -> {

        Account a;
        if (account.getSubType().equals("VI")) {

            //validacion tarjeta credito
            a = Account.builder()
                    .accountId(AccountGeneratorValues.IdentityGenerate("AH", account.getCodeBusinessPartner()))
                    .accountNumber(AccountGeneratorValues.NumberGenerate("AH"))
                    .accountName("VIP Saving Account")
                    .accountType("AH")
                    .subType(account.getSubType())
                    .valid(true)
                    .codeBusinessPartner(account.getCodeBusinessPartner())
                    .date_Opened(new Date()).build();

        } else {

            a = Account.builder()
                    .accountId(AccountGeneratorValues.IdentityGenerate("AH", account.getCodeBusinessPartner()))
                    .accountNumber(AccountGeneratorValues.NumberGenerate("AH"))
                    .accountName("Standart Saving Account")
                    .accountType("AH")
                    .subType(account.getSubType())
                    .valid(true)
                    .codeBusinessPartner(account.getCodeBusinessPartner())
                    .date_Opened(new Date()).build();
        }
        return repository.save(a);

    };
    
    private final Function<TimeDepositAccountDTO, Mono<Account>> saveTimeDepositAccount = account -> {

    	  Account a = Account.builder()
                  .accountId(AccountGeneratorValues.IdentityGenerate("PL", account.getCodeBusinessPartner()))
                  .accountNumber(AccountGeneratorValues.NumberGenerate("PL"))
                  .accountName("Time Deposit Account")
                  .accountType("PL")
                  .valid(true)
                  .codeBusinessPartner(account.getCodeBusinessPartner())
                  .date_Opened(new Date()).build();

          return repository.save(a);
    };
    
    private final Function<PeopleCheckingAccountDTO, Mono<Account>> savePeopleCheckingAccount = account ->{
    	
            Account a = Account.builder()
                    .accountId(AccountGeneratorValues.IdentityGenerate("CO", account.getCodeBusinessPartner()))
                    .accountNumber(AccountGeneratorValues.NumberGenerate("CO"))
                    .accountName("People Checking Account")
                    .accountType("CO")
                    .valid(true)
                    .codeBusinessPartner(account.getCodeBusinessPartner())
                    .date_Opened(new Date()).build();

            return repository.save(a);
    };
    
    private final Function<CompanyCheckingAccountDTO, Mono<Account>> saveCompanyCheckingAccount = account ->{
        Account a;
        if (account.getSubType().equals("PY")) {

            //validacion tarjeta
            a = Account.builder()
                    .accountId(AccountGeneratorValues.IdentityGenerate("CO", account.getCodeBusinessPartner()))
                    .accountNumber(AccountGeneratorValues.NumberGenerate("CO"))
                    .accountName("Pyme Company Checking Account")
                    .accountType("CO")
                    .valid(true)
                    .codeBusinessPartner(account.getCodeBusinessPartner())
                    .date_Opened(new Date()).build();

        } else {
            a = Account.builder()
                    .accountId(AccountGeneratorValues.IdentityGenerate("CO", account.getCodeBusinessPartner()))
                    .accountNumber(AccountGeneratorValues.NumberGenerate("CO"))
                    .accountName("Standart Company Checking Account")
                    .accountType("CO")
                    .subType(account.getSubType())
                    .valid(true)
                    .codeBusinessPartner(account.getCodeBusinessPartner())
                    .date_Opened(new Date()).build();
        }
        return repository.save(a);
    };
}
