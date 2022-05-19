package com.bankntt.MSFundtransact.infraestructure.services;

import com.bankntt.MSFundtransact.application.exception.EntityNotExistsException;
import com.bankntt.MSFundtransact.domain.beans.BusinessPartnerBean;
import com.bankntt.MSFundtransact.domain.beans.CreditCardPaymentDTO;
import com.bankntt.MSFundtransact.domain.beans.CreditPaymentDTO;
import com.bankntt.MSFundtransact.domain.beans.NewCreditDTO;
import com.bankntt.MSFundtransact.domain.entities.Credit;
import com.bankntt.MSFundtransact.domain.repository.CreditRepository;
import com.bankntt.MSFundtransact.infraestructure.interfaces.ICreditService;
import com.bankntt.MSFundtransact.infraestructure.interfaces.ICreditTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class CreditService implements ICreditService, ICreditTransactionService {

    @Autowired
    CreditRepository repository;

    @Override
    public Flux<Credit> findAll() {

        return repository.findAll();
    }

    public Mono<Credit> save(Credit a) {
        WebClient client = WebClient.builder().baseUrl("http://localhost:8080/BusinessPartnerService")

                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
        return client.get()
                .uri(uriBuilder -> uriBuilder.path("/BusinessPartner/{id}").build(a.getCodebusinesspartner()))
                .retrieve().onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new EntityNotExistsException()))
                .bodyToMono(BusinessPartnerBean.class)
                .filter(r -> r.getType().equals("C") || r.getType().equals("P"))
                .flatMap(f -> {

                    //	a.getCreatedate(new Date().getDateInstance);
                    return repository.save(a);
                })
                .switchIfEmpty(Mono.error(new EntityNotExistsException())
                );// AccountNotCreatedException
    }

    @Override
    public Mono<Credit> delete(String Id) {

        return repository.findById(Id).flatMap(r -> repository.delete(r).then(Mono.just(r)));
    }

    @Override
    public Mono<Credit> findById(String Id) {
        // TODO Auto-generated method stub
        return repository.findById(Id);
    }

    @Override
    public Mono<Credit> doCreditPayment(CreditPaymentDTO dto) {
        return null;
    }

    @Override
    public Mono<Credit> doCreditCardPayment(CreditCardPaymentDTO dto) {
        return null;
    }

    @Override
    public Mono<Credit> getNewCredit(NewCreditDTO dto) {
        return null;
    }
}
