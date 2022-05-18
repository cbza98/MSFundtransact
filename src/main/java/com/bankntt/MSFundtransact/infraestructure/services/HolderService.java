package com.bankntt.MSFundtransact.infraestructure.services;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bankntt.MSFundtransact.application.exception.EntityNotExists;
import com.bankntt.MSFundtransact.domain.beans.BusinessPartnerBean;
import com.bankntt.MSFundtransact.domain.entities.Holder;
import com.bankntt.MSFundtransact.domain.repository.AccountRepository;
import com.bankntt.MSFundtransact.domain.repository.HolderRepository;
import com.bankntt.MSFundtransact.infraestructure.interfaces.IHolderService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class HolderService implements IHolderService {
	@Autowired(required = true)
	HolderRepository repository;
	@Autowired(required = true)
	AccountRepository repositoryaccount;
	Predicate<String> isBpCompany = (String bp) -> {

		WebClient client = WebClient.builder().baseUrl("http://localhost:8080/BusinessPartnerService")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
		client.get().uri(uriBuilder -> uriBuilder.path("/BusinessPartner/{id}").build(bp)).retrieve()
				.onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new EntityNotExists()))
				.bodyToMono(BusinessPartnerBean.class);

		return true;

	};

	Predicate<String> iAccountvalid = (String Id) -> {

		return true;

	};

	@Override
	public Flux<Holder> findAll() {
		return repository.findAll();

	}

	@Override
	public Mono<Holder> save(Holder a) {

		return null;
	}

	@Override
	public Mono<Holder> delete(String Id) {
		// TODO Auto-generated method stub
		return repository.findById(Id).flatMap(deleted -> repository.delete(deleted).then(Mono.just(deleted)));
	}

	@Override
	public Mono<Holder> findById(String Id) {
		return repository.findById(Id);

	}

}
