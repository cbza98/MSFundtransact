package com.bankntt.msfundtransact.domain.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bankntt.msfundtransact.domain.entities.Transaction;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
}
