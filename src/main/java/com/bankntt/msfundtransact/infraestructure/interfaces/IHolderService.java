package com.bankntt.msfundtransact.infraestructure.interfaces;

import com.bankntt.msfundtransact.domain.entities.Holder;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IHolderService {
	public Flux<Holder> findAll();

	public Mono<Holder> save(Holder _entity);

	public Mono<Holder> delete(String Id);

	public Mono<Holder> findById(String Id);
}
