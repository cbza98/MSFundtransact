package com.bankntt.MSFundtransact.domain.repository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bankntt.MSFundtransact.domain.entities.CreditCard;
public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, String> {


}
