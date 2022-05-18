package com.bankntt.MSFundtransact.domain.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bankntt.MSFundtransact.domain.entities.Signer;

public interface SignerRepository extends ReactiveMongoRepository<Signer, String> {

}
