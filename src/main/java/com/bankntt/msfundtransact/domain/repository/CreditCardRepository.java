package com.bankntt.msfundtransact.domain.repository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bankntt.msfundtransact.domain.entities.CreditCard;
public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, String> {


}
