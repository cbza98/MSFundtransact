package com.bankntt.msfundtransact.infraestructure.services;

import com.bankntt.msfundtransact.domain.entities.Credit;
import com.bankntt.msfundtransact.domain.entities.CreditCard;
import com.bankntt.msfundtransact.domain.repository.CreditCardRepository;
import com.bankntt.msfundtransact.infraestructure.interfaces.ICreditTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bankntt.msfundtransact.application.exception.AccountNotCreatedException;
import com.bankntt.msfundtransact.domain.beans.*;
import com.bankntt.msfundtransact.domain.entities.Transaction;
import com.bankntt.msfundtransact.domain.enums.TransactionType;
import com.bankntt.msfundtransact.domain.repository.TransactionRepository;
import com.bankntt.msfundtransact.infraestructure.interfaces.ITransactionService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
public class TransactionService implements ITransactionService, ICreditTransactionService {

    //Attribute
    @Autowired
    TransactionRepository trepository;
    @Autowired
    CreditCardRepository crepository;
    @Autowired
    AccountService accountService;
    @Autowired
    CreditCardService creditcardService;

    //Crud
    @Override
    public Flux<Transaction> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Mono<Transaction> delete(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Mono<Transaction> findById(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Mono<ResponseEntity<Transaction>> update(String id, Transaction request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Flux<Transaction> saveAll(List<Transaction> a) {
        // TODO Auto-generated method stub
        return null;
    }

    //Transaction
    @Override
    public Mono<Transaction> doAccountDeposit(AccountOperationDTO dto) {

        return accountService.findByAccountNumber(dto.getAccount()).then(Mono.just(dto)
                        .flatMap(savedeposit))
                .switchIfEmpty(Mono.error(new AccountNotCreatedException()));

    }

    @Override
    public Mono<Transaction> TransferBetweenAccounts(AccountTransferDTO dto) {
        return null;
    }

    @Override
    public Mono<Transaction> doAccountWithdrawal(AccountOperationDTO dto) {

        return accountService.findByAccountNumber(dto.getAccount()).then(Mono.just(dto)
                        .flatMap(savewithdrawal))
                .switchIfEmpty(Mono.error(new AccountNotCreatedException()));

    }



    @Override
    public Mono<Transaction> doTransferToThirdParty(AccountTransferDTO dto) {
        return null;
    }

    // Credits Transactions
    @Override
    public Mono<Credit> doCreditPayment(CreditPaymentDTO dto) {
        return null;
    }

    @Override
    public Mono<Transaction> doCreditCardPayment(CreditCardPaymentDTO dto) {
        return creditcardService.findById(dto.getCreditcard())
                .filter(r -> isValidPaymentCreditCard.test(r, dto))
                .flatMap(r ->
                {
                    r.setConsumedline(r.getConsumedline().subtract(dto.getPayment()));
                    r.setAvailableline(r.getAvailableline().add(dto.getPayment()));
                    return crepository.save(r);

                })
                .then(
                        Mono.just(dto)
                                .flatMap(savepaymentcreditcard))
                .switchIfEmpty(Mono.error(new AccountNotCreatedException()));
    }

    @Override
    public Mono<Transaction> doCreditcardConsumption(CreditcardConsumptionDTO dto) {
        return creditcardService.findById(dto.getCreditcard())
                .filter(r -> isValidConsumptionCreditCard.test(r, dto))
                .flatMap(r ->
                {
                    r.setConsumedline(r.getConsumedline().add(dto.getConsumption()));
                    r.setAvailableline(r.getAvailableline().subtract(dto.getConsumption()));
                    return crepository.save(r);

                })
                .then(
                        Mono.just(dto)
                                .flatMap(saveconsumptioncreditcard))
                .switchIfEmpty(Mono.error(new AccountNotCreatedException()));
    }

    @Override
    public Mono<Credit> getNewCredit(NewCreditDTO dto) {
        return null;
    }

    //Metodos Funcionales
    private final Function<AccountOperationDTO, Mono<Transaction>> savedeposit = deposit -> {

        Transaction t;
        Mono<Transaction> _t;
        t = Transaction.builder()
                .amount(deposit.getAmount())
                .toaccount(deposit.getAccount())
                .transactiontype(TransactionType.DEPOSIT)
                .createDate(new Date()).build();

        _t = trepository.save(t);
        accountService.updateBalanceDp(t.getToaccount(), t.getAmount());
        return _t;
    };
    private final Function<AccountOperationDTO, Mono<Transaction>> savewithdrawal = withdrawal -> {

        Transaction t;
        Mono<Transaction> _t;
        t = Transaction.builder()
                .amount(withdrawal.getAmount())
                .toaccount(withdrawal.getAccount())
                .transactiontype(TransactionType.DEPOSIT)
                .createDate(new Date()).build();

        _t = trepository.save(t);
        accountService.updateBalanceWt(t.getToaccount(), t.getAmount());
        return _t;
    };
    private final BiPredicate<CreditCard, CreditcardConsumptionDTO> isValidConsumptionCreditCard = (a, b) ->
    {
        BigDecimal _a, _b;
        Integer responsen;
        boolean b1;
        _a = a.getAvailableline();
        _b = a.getConsumedline().add(b.getConsumption());
        responsen = _a.compareTo(_b);
        return b1 = responsen >= 0;
    };
    private final Function<CreditcardConsumptionDTO, Mono<Transaction>> saveconsumptioncreditcard = consumption -> {

        Transaction t;
        CreditCard c = new CreditCard();
        Mono<Transaction> _t;
        t = Transaction.builder()
                .debit(consumption.getConsumption())
                .creditcard(consumption.getCreditcard())
                .transactiontype(TransactionType.CREDIT_CARD_CONSUMPTION)
                .createDate(new Date()).build();
        _t = trepository.save(t);
        //  creditcardService.updateconsumption(t.getCreditcard(),t.getDebit());
        return _t;
    };
    private final BiPredicate<CreditCard, CreditCardPaymentDTO> isValidPaymentCreditCard = (a, b) ->
    {
        BigDecimal _a, _b;
        Integer responsen;
        boolean b1;
        _a = a.getAvailableline();
        _b = a.getConsumedline().add(b.getPayment());
        responsen = _a.compareTo(_b);
        return b1 = responsen >= 0;
    };
    private final Predicate<AccountTransferDTO> isDistinctAccount = (dto) ->
    {
        Boolean r1 = true;
        String a = dto.getFaccount();
        String b = dto.getTaccount();
        if (a.equals(b)) {
            r1= false;
        }
        return r1;
    };

    private final Function<CreditCardPaymentDTO, Mono<Transaction>> savepaymentcreditcard = payment -> {

        Transaction t;
        Mono<Transaction> _t;
        t = Transaction.builder()
                .credit(payment.getPayment())
                .creditcard(payment.getCreditcard())
                .transactiontype(TransactionType.CREDIT_CARD_PAYMENT)
                .createDate(new Date()).build();
        _t = trepository.save(t);
        //   creditcardService.updatepayments(payment.getCreditcard(), payment.getPayment());
        return _t;
    };
}
