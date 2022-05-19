package com.bankntt.MSFundtransact.domain.repository;

import com.bankntt.MSFundtransact.domain.entities.Credit;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CreditRepository extends ReactiveMongoRepository<Credit, String> {
}
