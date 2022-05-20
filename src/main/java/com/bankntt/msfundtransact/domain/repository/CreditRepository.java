package com.bankntt.msfundtransact.domain.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bankntt.msfundtransact.domain.entities.Credit;

public interface CreditRepository extends ReactiveMongoRepository<Credit, String> {
}
