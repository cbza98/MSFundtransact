package com.bankntt.msfundtransact.domain.repository;


import com.bankntt.msfundtransact.domain.entities.AccountItem;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface AccountItemRepository extends ReactiveMongoRepository<AccountItem,String> {

    Mono<AccountItem> findByAccountType(String accountType);
}
