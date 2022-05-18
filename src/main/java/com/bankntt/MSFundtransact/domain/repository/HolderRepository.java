package com.bankntt.MSFundtransact.domain.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bankntt.MSFundtransact.domain.entities.Holder;

public interface HolderRepository extends ReactiveMongoRepository<Holder, String> {

}
