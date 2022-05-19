package com.bankntt.MSFundtransact.infraestructure.services;

import com.bankntt.MSFundtransact.application.exception.EntityNotExistsException;
import com.bankntt.MSFundtransact.application.helpers.AccountGeneratorValues;
import com.bankntt.MSFundtransact.domain.beans.BusinessPartnerBean;
import com.bankntt.MSFundtransact.domain.beans.CreateCreditCardDTO;
import com.bankntt.MSFundtransact.domain.beans.SavingAccountDTO;
import com.bankntt.MSFundtransact.domain.entities.Account;
import com.bankntt.MSFundtransact.domain.entities.CreditCard;
import com.bankntt.MSFundtransact.domain.repository.CreditCardRepository;
import com.bankntt.MSFundtransact.infraestructure.interfaces.ICreditCardService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
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
        // TODO Auto-generated method stub
        return null;
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

    private final Function<CreateCreditCardDTO, Mono<CreditCard>> CreateSavingAccount = creditCardDto -> {

        CreditCard a;

            a = CreditCard.builder()


                    .valid(true)
                    .codeBusinessPartner(creditCardDto.getCodeBusinessPartner())
                    .openDate(new Date()).build();

        return repository.save(a);

    };

}
