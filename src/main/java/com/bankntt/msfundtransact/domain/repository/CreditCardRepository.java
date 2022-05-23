package com.bankntt.msfundtransact.domain.repository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bankntt.msfundtransact.domain.entities.CreditCard;
import reactor.core.publisher.Mono;

public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, String> {
    Mono<CreditCard> findBycardNumber(String s);
}
