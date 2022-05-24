package com.bankntt.msfundtransact.application.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.bankntt.msfundtransact.domain.beans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankntt.msfundtransact.domain.entities.Account;
import com.bankntt.msfundtransact.infraestructure.services.AccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/MsFundTransact/Entities/Accounts")
public class AccountController {
	@Autowired
	private AccountService service;

	@GetMapping
	public Mono<ResponseEntity<Flux<Account>>> findAll() {
		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(service.findAll()));
	}

	@GetMapping("/{id}")
	public Mono<Account> findById(@PathVariable String id) {
		return service.findById(id);
	}


	@PostMapping("/CreateAccount")
	public Mono<ResponseEntity<Map<String, Object>>> createAccount(@Valid @RequestBody Mono<CreateAccountDTO> request) {

		Map<String, Object> response = new HashMap<>();

		return request.flatMap(a -> service.createAccount(a).map(c -> {
			response.put("Cuenta", c);
			response.put("mensaje", "Cuenta creada con exito");
			return ResponseEntity.created(URI.create("/api/Account/".concat(c.getAccountNumber())))
					.contentType(MediaType.APPLICATION_JSON).body(response);
		}));
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

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Map<String, Object> >> delete(@PathVariable String id) {
		 Map<String, Object> response = new HashMap<>();

		return service.delete(id)
				.map(c -> {
					response.put("BusinessPartner", c);
					response.put("mensaje", "Succesfull BusinessPartner Deleted");
					return ResponseEntity.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.location( URI.create("/api/BusinessPartner/".concat(c.getAccountNumber())))
							.body(response);
				});
	}

	
}
