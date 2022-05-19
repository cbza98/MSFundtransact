package com.bankntt.MSFundtransact.infraestructure.services;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bankntt.MSFundtransact.application.exception.AccountNotCreatedException;
import com.bankntt.MSFundtransact.application.exception.EntityNotExistsException;
import com.bankntt.MSFundtransact.application.helpers.AccountGeneratorValues;
import com.bankntt.MSFundtransact.domain.beans.BusinessPartnerBean;
import com.bankntt.MSFundtransact.domain.beans.CompanyCheckingAccountDTO;
import com.bankntt.MSFundtransact.domain.beans.PeopleCheckingAccountDTO;
import com.bankntt.MSFundtransact.domain.beans.SavingAccountDTO;
import com.bankntt.MSFundtransact.domain.beans.TimeDepositAccountDTO;
import com.bankntt.MSFundtransact.domain.entities.Account;
import com.bankntt.MSFundtransact.domain.repository.AccountRepository;
import com.bankntt.MSFundtransact.infraestructure.interfaces.IAccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountService implements IAccountService {

	
	@Autowired
	AccountRepository repository;

	@Override
	public Flux<Account> findAll() {
		return repository.findAll();
	}
	@Override
	public Mono<Account> delete(String Id) {
		return repository.findById(Id).flatMap(deleted -> repository.delete(deleted).then(Mono.just(deleted)));
	}

	@Override
	public Mono<Account> findById(String Id) {
		return repository.findById(Id);
	}

	@Override
	public Mono<ResponseEntity<Account>> update(String id, Account _request) {
		return repository.findById(id).flatMap(a -> {
			a.setAccountName(_request.getAccountName());
			a.setAccountNumber(_request.getAccountNumber());
			a.setAccountType(_request.getAccountType());
			a.setCodeBusinessPartner(_request.getCodeBusinessPartner());
			a.setDate_Opened(_request.getDate_Opened());
			a.setValid(_request.getValid());
			return repository.save(a);
		}).map(updated -> new ResponseEntity<>(updated, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.OK));
	}

	public Flux<Account> saveAll(List<Account> a) {
		
		return repository.saveAll(a);
	}

	@Override
	public Mono<Account> createSavingAccount(SavingAccountDTO account) {
		
		return getBusinessPartner(account.getCodeBusinessPartner()).then(
				repository.countByAccountTypeAndCodeBusinessPartner("AH", account.getCodeBusinessPartner())
				.filter(LongThanZero::test)
				.then(Mono.just(account)
						  .flatMap(FuctionalSaveAccount::apply)
						  .switchIfEmpty(Mono.error(new AccountNotCreatedException())))
		);
	}

	@Override
	public Mono<Account> createTimeDepositAccount(TimeDepositAccountDTO account) {
					
		return getBusinessPartner(account.getCodeBusinessPartner()).then(
			Mono.defer(()->{
				Account a = Account.builder()
							.accountId(AccountGeneratorValues.IdentityGenerate("PL",account.getCodeBusinessPartner()))
							.accountNumber(AccountGeneratorValues.NumberGenerate("PL"))
							.accountName("Time Deposit Account")
							.accountType("PL")
							.valid(true)
							.codeBusinessPartner(account.getCodeBusinessPartner())
							.date_Opened(new Date()).build();
							
							return repository.save(a);
			}));
	}
	
	@Override
	public Mono<Account> createPeopleCheckingAccount(PeopleCheckingAccountDTO account) {
					
		return getBusinessPartner(account.getCodeBusinessPartner()).then(
				repository.countByAccountTypeAndCodeBusinessPartner("CO", account.getCodeBusinessPartner())
				.filter(LongThanZero::test)
				.then(Mono.defer(()->{
					Account a = Account.builder()
							.accountId(AccountGeneratorValues.IdentityGenerate("CO",account.getCodeBusinessPartner()))
							.accountNumber(AccountGeneratorValues.NumberGenerate("CO"))
							.accountName("People Checking Account")
							.accountType("CO")
							.valid(true)
							.codeBusinessPartner(account.getCodeBusinessPartner())
							.date_Opened(new Date()).build();
					
							return repository.save(a);
				
				}).switchIfEmpty(Mono.error(new AccountNotCreatedException()))));
	}

	@Override
	public Mono<Account> createCompanyCheckingAccount(CompanyCheckingAccountDTO account) {
			
	
		
		return getBusinessPartner(account.getCodeBusinessPartner()).then(
				
					Mono.defer(()->{
						if(account.getSubType().equals("PY")) {	
							
							//validacion tarjeta
							Account a = Account.builder()
										.accountId(AccountGeneratorValues.IdentityGenerate("CO",account.getCodeBusinessPartner()))
										.accountNumber(AccountGeneratorValues.NumberGenerate("CO"))
										.accountName("Pyme Company Checking Account")
										.accountType("CO")
										.valid(true)
										.codeBusinessPartner(account.getCodeBusinessPartner())
										.date_Opened(new Date()).build();
								
							return repository.save(a);
						}
						else {
							 Account a = Account.builder()
										.accountId(AccountGeneratorValues.IdentityGenerate("CO",account.getCodeBusinessPartner()))
										.accountNumber(AccountGeneratorValues.NumberGenerate("CO"))
										.accountName("Standart Company Checking Account")
										.accountType("CO")
										.subType(account.getSubType())
										.valid(true)
										.codeBusinessPartner(account.getCodeBusinessPartner())
										.date_Opened(new Date()).build();
							 return repository.save(a);
						}
				}));
	}
	
	private Mono<BusinessPartnerBean> getBusinessPartner(String businessPartnerId){
		
		WebClient businessPartnerClient = WebClient.builder().baseUrl("http://localhost:9090/BusinessPartnerService")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	
		return businessPartnerClient.get()
			   .uri(uriBuilder -> uriBuilder.path("/BusinessPartner/{id}").build(businessPartnerId))
			   .retrieve().onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new EntityNotExistsException()))
			   .bodyToMono(BusinessPartnerBean.class);
		
	}
	
	private Predicate<Long> LongThanZero = a -> (a == 0);
	
	private Function<SavingAccountDTO, Mono<Account>> FuctionalSaveAccount = account -> {	
		
		if(account.getSubType().equals("VI")) {
			 
			 //validacion tarjeta credito
			 Account a = Account.builder()
						.accountId(AccountGeneratorValues.IdentityGenerate("AH",account.getCodeBusinessPartner()))
						.accountNumber(AccountGeneratorValues.NumberGenerate("AH"))
						.accountName("VIP Saving Account")
						.accountType("AH")
						.subType(account.getSubType())
						.valid(true)
						.codeBusinessPartner(account.getCodeBusinessPartner())
						.date_Opened(new Date()).build();
			
			return repository.save(a);
		}
		else{
			
			 Account a = Account.builder()
						.accountId(AccountGeneratorValues.IdentityGenerate("AH",account.getCodeBusinessPartner()))
						.accountNumber(AccountGeneratorValues.NumberGenerate("AH"))
						.accountName("Standart Saving Account")
						.accountType("AH")
						.subType(account.getSubType())
						.valid(true)
						.codeBusinessPartner(account.getCodeBusinessPartner())
						.date_Opened(new Date()).build();
			 return repository.save(a);
		}

	};
	
	
}
