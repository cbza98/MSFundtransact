package com.bankntt.msfundtransact.infraestructure.services;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.bankntt.msfundtransact.application.exception.AccountNotCreatedException;
import com.bankntt.msfundtransact.application.exception.EntityNotExistsException;
import com.bankntt.msfundtransact.application.helpers.CardGeneratorValues;
import com.bankntt.msfundtransact.domain.beans.BusinessPartnerBean;
import com.bankntt.msfundtransact.domain.beans.CreateCreditCardDTO;
import com.bankntt.msfundtransact.domain.entities.CreditCard;
import com.bankntt.msfundtransact.domain.repository.CreditCardRepository;
import com.bankntt.msfundtransact.infraestructure.interfaces.ICreditCardService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Function;

public class CreditCardService implements ICreditCardService {

    CreditCardRepository repository;

    @Override
    public Flux<CreditCard> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<CreditCard> save(CreditCard _entity) {
        return null;
    }

    @Override
    public Mono<CreditCard> createCreditCard(CreateCreditCardDTO _entity) {
       return Mono.just(_entity)
                .doOnNext(r -> getBusinessPartner.accept(r.getCodeBusinessPartner()))
                .flatMap(saveCreditCard ).switchIfEmpty(Mono.error(new AccountNotCreatedException()));

    }

    @Override
    public Mono<CreditCard> delete(String Id) {
        return repository.findById(Id).flatMap(deleted -> repository.delete(deleted).then(Mono.just(deleted)))
                .switchIfEmpty(Mono.error(new EntityNotExistsException()));
    }

    @Override
    public Mono<CreditCard> findById(String Id) {
        return repository.findById(Id);
    }
    private final Consumer<String> getBusinessPartner = businessPartnerId-> {

        WebClient businessPartnerClient = WebClient.builder().baseUrl("http://localhost:9090/BusinessPartnerService")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

        businessPartnerClient.get()
                .uri(uriBuilder -> uriBuilder.path("/BusinessPartner/{id}").build(businessPartnerId))
                .retrieve().onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new EntityNotExistsException()) )
                .bodyToMono(BusinessPartnerBean.class);

    };

    private final Function<CreateCreditCardDTO, Mono<CreditCard>> saveCreditCard = creditCardDto -> {

        CreditCard a;

            a = CreditCard.builder()
                    .cardNumber(CardGeneratorValues.CardNumberGenerate())
                    .approvedline(creditCardDto.getLimit())
                    .valid(true)
                    .expiringDate(CardGeneratorValues.CardExpiringDateGenerate())
                    .codeBusinessPartner(creditCardDto.getCodeBusinessPartner())
                    .cvv(CardGeneratorValues.CardCVVGenerate())
                    .openDate(new Date()).build();

        return repository.save(a);

    };

}
