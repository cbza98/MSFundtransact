package com.bankntt.msfundtransact.infraestructure.interfaces;

import com.bankntt.msfundtransact.domain.entities.Signer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ISignerService {
	public Flux<Signer> findAll();

	public Mono<Signer> save(Signer _entity);

	public Mono<Signer> delete(String Id);

	public Mono<Signer> findById(String Id);
}
