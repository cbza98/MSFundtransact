package com.bankntt.MSFundtransact.infraestructure.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankntt.MSFundtransact.domain.entities.Signer;
import com.bankntt.MSFundtransact.infraestructure.interfaces.ISignerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SignerService implements ISignerService {
	@Autowired(required = true)
	@Override
	public Flux<Signer> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Signer> save(Signer _entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Signer> delete(String Id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Signer> findById(String Id) {
		// TODO Auto-generated method stub
		return null;
	}

}
