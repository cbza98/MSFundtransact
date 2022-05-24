package com.bankntt.msfundtransact.infraestructure.services;

import com.bankntt.msfundtransact.domain.entities.Credit;
import com.bankntt.msfundtransact.domain.entities.CreditCard;
import com.bankntt.msfundtransact.domain.repository.AccountRepository;
import com.bankntt.msfundtransact.domain.repository.CreditCardRepository;
import com.bankntt.msfundtransact.domain.repository.CreditRepository;
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

@Service
public class TransactionService implements ITransactionService, ICreditTransactionService {

    //Attribute
    @Autowired
    TransactionRepository trepository;
    @Autowired
    AccountRepository arepository;
    @Autowired
    CreditCardRepository crepository;
    @Autowired
    CreditRepository crrepository;
    @Autowired
    AccountService accountService;
    @Autowired
    CreditCardService creditcardService;
    @Autowired
    CreditService creditService;
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

        return accountService.findByAccountNumber(dto.getAccount()).flatMap (a -> {
            a.setBalance(a.getBalance().add(dto.getAmount()));
            return arepository.save(a);
        })


                .then(Mono.just(dto)
                        .flatMap(savedeposit))
                .switchIfEmpty(Mono.error(new AccountNotCreatedException()));

    }
    @Override
    public Mono<Transaction> TransferBetweenAccounts(AccountTransferDTO dto) {
        return accountService.findByAccountNumber(dto.getFaccount())
                .filter(r -> r.getCodeBusinessPartner().equals(dto.getFbp()))
                .flatMap (a -> {
                    a.setBalance(a.getBalance().subtract(dto.getAmount()));
                    return arepository.save(a);})
                .then(accountService.findByAccountNumber(dto.getTaccount())
                        .flatMap( a -> {
                                    a.setBalance(a.getBalance().add(dto.getAmount()));
                                    return arepository.save(a);})
                .then(Mono.just(dto).flatMap(Savetransfertbtweenaccount)

                        .switchIfEmpty(Mono.error(new AccountNotCreatedException()))));

    }

    @Override
    public Mono<Transaction> doAccountWithdrawal(AccountOperationDTO dto) {

        return accountService.findByAccountNumber(dto.getAccount()).then(Mono.just(dto)
                        .flatMap(savewithdrawal))
                .switchIfEmpty(Mono.error(new AccountNotCreatedException()));

    }

    @Override
    public Mono<Transaction> doTransferToThirdParty(AccountTransferDTO dto) {
        return accountService.findByAccountNumber(dto.getFaccount())
                .filter(r -> r.getCodeBusinessPartner().equals(dto.getFbp()))
                .flatMap (a -> {
                    a.setBalance(a.getBalance().subtract(dto.getAmount()));
                    return arepository.save(a);})
                .then(accountService.findByAccountNumber(dto.getTaccount())
                        .flatMap( a -> {
                            a.setBalance(a.getBalance().add(dto.getAmount()));
                            return arepository.save(a);})


                        .then(Mono.just(dto).flatMap(saveTransferToThirdParty)
                                .switchIfEmpty(Mono.error(new AccountNotCreatedException()))));


    }
    // Credits Transactions
    @Override
    public Mono<Transaction> doCreditPayment(CreditPaymentDTO dto) {
        return creditService.findById(dto.getCreditid())
                .filter(r -> isValidPaymentCredit.test(r, dto))
                .flatMap(r ->
                {
                    r.setPaymentcredit(r.getPaymentcredit().add(dto.getPayment()));
                    return crrepository.save(r);
                })
                .then(
                        Mono.just(dto)
                                .flatMap(savepaymentcredit))
                .switchIfEmpty(Mono.error(new AccountNotCreatedException()));
    }

    @Override
    public Mono<Transaction> doCreditConsumption(CreditConsumptionDTO dto) {
        return creditService.findById(dto.getCreditid())
                .filter(r -> isValidConsumptionCredit.test(r, dto))
                .flatMap(r ->
                {
                    r.setUsedcredit(r.getUsedcredit().add(dto.getConsumption()));
                    return crrepository.save(r);
                })
                .then(
                        Mono.just(dto)
                                .flatMap(saveconsumptioncredit))
                .switchIfEmpty(Mono.error(new AccountNotCreatedException()));
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
                                .flatMap(Savepaymentcreditcard))
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
    public Mono<Transaction> getNewCredit(NewCreditDTO dto) {
        return null;
    }
    //Methods
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
        _a = a.getAvailableline();
        _b = a.getConsumedline().add(b.getConsumption());
        responsen = _a.compareTo(_b);
        return responsen >= 0;
    };
    private final Function<CreditcardConsumptionDTO, Mono<Transaction>> saveconsumptioncreditcard = consumption -> {

        Transaction t;
        Mono<Transaction> _t;
        t = Transaction.builder()
                .debit(consumption.getConsumption())
                .creditcardId(consumption.getCreditcard())
                .transactiontype(TransactionType.CREDIT_CARD_CONSUMPTION)
                .createDate(new Date()).build();
        _t = trepository.save(t);
        return _t;
    };
    private final BiPredicate<CreditCard, CreditCardPaymentDTO> isValidPaymentCreditCard = (a, b) ->
    {
        BigDecimal _a, _b;
        Integer responsen;
        _a = a.getAvailableline();
        _b = a.getConsumedline().add(b.getPayment());
        responsen = _a.compareTo(_b);
        return responsen >= 0;
    };
    private final BiPredicate<Credit, CreditPaymentDTO> isValidPaymentCredit = (a, b) ->
    {
        BigDecimal _a, _b;
        Integer responsen;

        _a = a.getAvailablecredit();
        _b = a.getUsedcredit().add(b.getPayment());
        responsen = _a.compareTo(_b);
        return responsen >= 0;
    };
    private final BiPredicate<Credit, CreditConsumptionDTO> isValidConsumptionCredit= (a, b) ->
    {
        BigDecimal _a, _b;
        Integer responsen;
        _a = a.getAvailablecredit();
        _b = a.getUsedcredit().add(b.getConsumption());
        responsen = _a.compareTo(_b);
        return responsen >= 0;
    };
    private final Function<CreditCardPaymentDTO, Mono<Transaction>> Savepaymentcreditcard = payment -> {

        Transaction t;
        Mono<Transaction> _t;
        t = Transaction.builder()
                .credit(payment.getPayment())
                .creditcardId(payment.getCreditcard())
                .transactiontype(TransactionType.CREDIT_CARD_PAYMENT)
                .createDate(new Date()).build();
        _t = trepository.save(t);
        return _t;
    };
    private final Function<CreditPaymentDTO, Mono<Transaction>> savepaymentcredit = payment -> {

        Transaction t;
        Mono<Transaction> _t;
        t = Transaction.builder()
                .credit(payment.getPayment())
                .creditid(payment.getCreditid())
                .transactiontype(TransactionType.CREDIT_PAYMENT)
                .createDate(new Date()).build();
        _t = trepository.save(t);
        return _t;
    };
    private final Function<CreditConsumptionDTO, Mono<Transaction>> saveconsumptioncredit = consumption -> {

        Transaction t;
        Mono<Transaction> _t;
        t = Transaction.builder()
                .debit(consumption.getConsumption())
                .creditid(consumption.getCreditid())
                .transactiontype(TransactionType.CREDIT_CONSUMPTION)
                .createDate(new Date()).build();
        _t = trepository.save(t);
        return _t;
    };
    private final Function<AccountTransferDTO, Mono<Transaction>> Savetransfertbtweenaccount = transfer -> {

        Transaction t;
        String a = transfer.getFaccount();
        String b = transfer.getTaccount();
        if (a.equals(b))
        {
            Mono.error(new AccountNotCreatedException());
        }

        Mono<Transaction> _t;
        t = Transaction.builder()
                .amount(transfer.getAmount())
                .debit(transfer.getAmount())
                .credit(transfer.getAmount())
                .toaccount(b)
                .fromaccount(a)
                .transactiontype(TransactionType.SAME_HOLDER_TRANSFER)
                .createDate(new Date()).build();
        _t = trepository.save(t);
        return _t;
    };
    private final Function<AccountTransferDTO, Mono<Transaction>> saveTransferToThirdParty = transfer -> {

        Transaction t;
        String a = transfer.getFaccount();
        String b = transfer.getTaccount();
        if (a.equals(b))
        {
            Mono.error(new AccountNotCreatedException());
        }

        Mono<Transaction> _t;
        t = Transaction.builder()
                .amount(transfer.getAmount())
                .debit(transfer.getAmount())
                .credit(transfer.getAmount())
                .toaccount(b)
                .fromaccount(a)
                .transactiontype(TransactionType.THIRD_PARTY_TRANSFER)
                .createDate(new Date()).build();
        _t = trepository.save(t);
        return _t;
    };

}






