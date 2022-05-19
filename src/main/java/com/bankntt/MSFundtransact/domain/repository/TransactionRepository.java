package com.bankntt.MSFundtransact.domain.repository;

import com.bankntt.MSFundtransact.domain.entities.Account;
import com.bankntt.MSFundtransact.domain.entities.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
}
