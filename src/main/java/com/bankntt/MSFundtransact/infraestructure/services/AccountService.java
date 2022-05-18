package com.bankntt.MSFundtransact.infraestructure.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bankntt.MSFundtransact.application.exception.EntityNotExists;
import com.bankntt.MSFundtransact.application.helpers.AccountGeneratorValues;
import com.bankntt.MSFundtransact.domain.beans.BusinessPartnerBean;
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
	public Mono<Account> save(Account a) {
		
		WebClient businessPartnerClient = WebClient.builder().baseUrl("http://localhost:9090/BusinessPartnerService")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
		
		WebClient creditCardClient = WebClient.builder().baseUrl("http://localhost:9092/CreditCardService")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();


		
		return businessPartnerClient.get()
				.uri(uriBuilder -> uriBuilder.path("/BusinessPartner/{id}").build(a.getCodeBusinessPartner()))
				.retrieve().onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new EntityNotExists()))
				.bodyToMono(BusinessPartnerBean.class)// Hasta aca es la obtencion del web client
				// Filtro si es C= Compañia
				.filter(r -> r.getType().equals("C"))
				.flatMap(t -> Mono.just(a).filter(r -> r.getAccountType().equals("CO"))// Si es tipo CUENTA CORRIENTE					
						
						
						// GUARDA EN EL REPOSITORIO
						.flatMap(f -> {
							
							a.setAccountId(AccountGeneratorValues.IdentityGenerate(a.getAccountType(),
									a.getCodeBusinessPartner()));
							a.setAccountNumber(AccountGeneratorValues.NumberGenerate(a.getAccountType()));
							
							return repository.save(a);

						})
						// DE LO CONTRARIO LANZA ERROR
						.switchIfEmpty(Mono.error(new EntityNotExists())))// AccountNotCreatedException
				.switchIfEmpty(repository
						.countByAccountTypeAndCodeBusinessPartner(a.getAccountType(), a.getCodeBusinessPartner())
						// Obtiene
						// el
						// Count
						// segun los
						// parametros
						// Realiza el filtro de Cuenta de Personas
						.filter(r -> (a.getAccountType().equals("CO") && r == 0) || // Si es igual a cuenta corriente y
																					// el
																					// conteo es 0
						// O
								(a.getAccountType().equals("AH") && r == 0) || // Si es igual a cuenta ahorros y el
																				// conteo
																				// es 0
								// O
								(a.getAccountType().equals("PL"))// Si es Cuenta Plazo fijo
						).flatMap(r -> {

							a.setAccountId(AccountGeneratorValues.IdentityGenerate(a.getAccountType(),
									a.getCodeBusinessPartner()));
							a.setAccountNumber(AccountGeneratorValues.NumberGenerate(a.getAccountType()));
							return repository.save(a);
						})// GUARDA EN EL REPOSITORIO
						.switchIfEmpty(Mono.error(new EntityNotExists())));// AccountNotCreatedException

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

	@Override
	public Flux<Account> saveAll(List<Account> a) {
		return repository.saveAll(a);
	}
	
	
}
