package com.bankntt.MSFundtransact.application.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankntt.MSFundtransact.domain.beans.CompanyCheckingAccountDTO;
import com.bankntt.MSFundtransact.domain.beans.PeopleCheckingAccountDTO;
import com.bankntt.MSFundtransact.domain.beans.SavingAccountDTO;
import com.bankntt.MSFundtransact.domain.beans.TimeDepositAccountDTO;
import com.bankntt.MSFundtransact.domain.entities.Account;
import com.bankntt.MSFundtransact.infraestructure.services.AccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/Account")
public class AccountController {
	@Autowired
	private AccountService service;

	@GetMapping
	public Mono<ResponseEntity<Flux<Account>>> FindAll() {
		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(service.findAll()));
	}

	@GetMapping("/{id}")
	public Mono<Account> findById(@PathVariable String id) {
		return service.findById(id);
	}


	@PostMapping("/CreateSavingAccount")
	public Mono<ResponseEntity<Map<String, Object>>> createSavingAccount(@Valid @RequestBody Mono<SavingAccountDTO> request) {

		Map<String, Object> response = new HashMap<>();

		return request.flatMap(a -> {
			return service.createSavingAccount(a).map(c -> {
				response.put("Cuenta", c);
				response.put("mensaje", "Cuenta creada con exito");
				return ResponseEntity.created(URI.create("/api/Account/".concat(c.getAccountId())))
						.contentType(MediaType.APPLICATION_JSON).body(response);
			});
		});
	}
	
	@PostMapping("/CreateTimeDepositAccount")
	public Mono<ResponseEntity<Map<String, Object>>> createTimeDepositAccount(@Valid @RequestBody Mono<TimeDepositAccountDTO> request) {

		Map<String, Object> response = new HashMap<>();

		return request.flatMap(a -> {
			return service.createTimeDepositAccount(a).map(c -> {
				response.put("Cuenta", c);
				response.put("mensaje", "Cuenta creada con exito");
				return ResponseEntity.created(URI.create("/api/Account/".concat(c.getAccountId())))
						.contentType(MediaType.APPLICATION_JSON).body(response);
			});
		});
	}
	
	@PostMapping("/CreatePeopleCheckingAccount")
	public Mono<ResponseEntity<Map<String, Object>>> createPeopleCheckingAccount(@Valid @RequestBody Mono<PeopleCheckingAccountDTO> request) {

		Map<String, Object> response = new HashMap<>();

		return request.flatMap(a -> {
			return service.createPeopleCheckingAccount(a).map(c -> {
				response.put("Cuenta", c);
				response.put("mensaje", "Cuenta creada con exito");
				return ResponseEntity.created(URI.create("/api/Account/".concat(c.getAccountId())))
						.contentType(MediaType.APPLICATION_JSON).body(response);
			});
		});
	}
	
	@PostMapping("/CreateCompanyCheckingAccount")
	public Mono<ResponseEntity<Map<String, Object>>> createCompanyCheckingAccount(@Valid @RequestBody Mono<CompanyCheckingAccountDTO> request) {

		Map<String, Object> response = new HashMap<>();

		return request.flatMap(a -> {
			return service.createCompanyCheckingAccount(a).map(c -> {
				response.put("Cuenta", c);
				response.put("mensaje", "Cuenta creada con exito");
				return ResponseEntity.created(URI.create("/api/Account/".concat(c.getAccountId())))
						.contentType(MediaType.APPLICATION_JSON).body(response);
			});
		});
	}
	@PostMapping("/saveBulk")
	public Mono<ResponseEntity<Map<String, Object>>> saveBulk(@RequestBody Flux<Account> businessPartnerList) {

		Map<String, Object> response = new HashMap<>();

		return businessPartnerList.collectList().flatMap(a -> service.saveAll(a).collectList()).map(c -> {
			response.put("BusinessPartners", c);
			response.put("mensaje", "Succesfull BusinessPartner Created");
			return ResponseEntity.created(URI.create("/api/BusinessPartner/")).contentType(MediaType.APPLICATION_JSON)
					.body(response);
		});
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<Account>> Update(@PathVariable String id, @RequestBody Account request) {
		return service.update(id, request);

	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> Delete(@PathVariable String id) {
		return service.delete(id).map(r -> ResponseEntity.ok().<Void>build())
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	
}
