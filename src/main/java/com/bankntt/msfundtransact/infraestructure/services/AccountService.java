package com.bankntt.msfundtransact.infraestructure.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import com.bankntt.msfundtransact.domain.beans.*;
import com.bankntt.msfundtransact.domain.entities.AccountItem;
import com.bankntt.msfundtransact.infraestructure.interfaces.IAccountItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bankntt.msfundtransact.application.exception.AccountNotCreatedException;
import com.bankntt.msfundtransact.application.exception.EntityNotExistsException;
import com.bankntt.msfundtransact.application.helpers.AccountGeneratorValues;
import com.bankntt.msfundtransact.domain.entities.Account;
import com.bankntt.msfundtransact.domain.repository.AccountRepository;
import com.bankntt.msfundtransact.infraestructure.interfaces.IAccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private IAccountItemService itemService;
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
    public Mono<Account> createAccount(CreateAccountDTO account) {
        return getAccountItem.apply(account)
                .flatMap(ai->existsBusinessPartner.apply(account,ai))
                .filter(ai->isBusinessPartnerAllowed.test(account,ai))
                .flatMap(ai->validateLimitCreation.apply(account,ai))
                .flatMap(ai->mapToAccountAndSave.apply(account,ai))
                .switchIfEmpty(Mono.error(AccountNotCreatedException::new));
    }

    //Functions
    private final BiFunction<CreateAccountDTO,AccountItem, Mono<AccountItem>> existsBusinessPartner = (account,accountItem)-> {

        WebClient businessPartnerClient = WebClient.builder().baseUrl("http://localhost:9090/BusinessPartnerService")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

        return  businessPartnerClient.get()
                .uri(uriBuilder -> uriBuilder.path("/BusinessPartner/{id}")
                .build(account.getCodeBusinessPartner()))
                .retrieve().onStatus(HttpStatus::is4xxClientError, error -> Mono.error(EntityNotExistsException::new) )
                .bodyToMono(BusinessPartnerBean.class).thenReturn(accountItem);
    };

    private final Function<CreateAccountDTO,Mono<AccountItem>> getAccountItem= createAccountDTO->
            itemService.findByAccountType(createAccountDTO.getAccountCode())
            .switchIfEmpty(Mono.error(EntityNotExistsException::new));

    private final BiPredicate<CreateAccountDTO,AccountItem> isBusinessPartnerAllowed= (createAccountDTO, accountItem)->
            accountItem.getBusinessPartnerAllowed()
                    .contains(createAccountDTO.getCodeBusinessPartner().substring(0,1));

    private final BiFunction<CreateAccountDTO,AccountItem, Mono<AccountItem>> validateLimitCreation = (account,accountItem) ->

            repository.countByAccountTypeAndCodeBusinessPartner(account.getAccountCode(),
                    account.getCodeBusinessPartner())
                    .filter(count->accountItem.getLimitAccountsAllowed()>count||
                            accountItem.getHasAccountsLimit().equals(false))
                    .map(count->accountItem)
                    .switchIfEmpty(Mono.error(AccountNotCreatedException::new));
    private final BiFunction<CreateAccountDTO,AccountItem, Mono<Account>> mapToAccountAndSave = (account,accountItem) -> {

    Account a = Account.builder()
            .accountNumber(AccountGeneratorValues.NumberGenerate(accountItem.getAccountType()))
            .valid(true)
            .balance(new BigDecimal("0.00"))
            .codeBusinessPartner(account.getCodeBusinessPartner())
            .date_Opened(new Date())
            .accountName(accountItem.getAccountName())
            .accountType(accountItem.getAccountType())
            .minDiaryAmount(accountItem.getMinDiaryAmount())
            .maintenanceCommission(accountItem.getMaintenanceCommission())
            .commission(accountItem.getCommission())
            .limitTransaction(accountItem.getLimitTransaction())
            .limitDay(accountItem.getLimitDay())
            .creditCardIsRequired(accountItem.getCreditCardIsRequired())
            .businessPartnerAllowed(accountItem.getBusinessPartnerAllowed())
            .limitAccountsAllowed(accountItem.getLimitAccountsAllowed())
            .moreHoldersAreAllowed(accountItem.getMoreHoldersAreAllowed())
            .signersAreAllowed(accountItem.getSignersAreAllowed())
            .hasAccountsLimit(accountItem.getHasAccountsLimit())
            .build();
        return repository.save(a);
    };
}
