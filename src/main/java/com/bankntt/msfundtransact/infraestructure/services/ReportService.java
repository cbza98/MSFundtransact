package com.bankntt.msfundtransact.infraestructure.services;

import com.bankntt.msfundtransact.domain.beans.AvaliableBalanceDTO;
import com.bankntt.msfundtransact.domain.beans.ChargedFeesDTO;
import com.bankntt.msfundtransact.domain.beans.DailyAverageBalanceDTO;
import com.bankntt.msfundtransact.domain.beans.TransactionReportDTO;
import com.bankntt.msfundtransact.domain.repository.AccountRepository;
import com.bankntt.msfundtransact.domain.repository.CreditCardRepository;
import com.bankntt.msfundtransact.infraestructure.interfaces.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReportService implements IReportService {
    private AccountRepository accRepository;
    private CreditCardRepository creditCardRepositoryRepository;

    @Override
    public Flux<TransactionReportDTO> getCreditCardTransactions(String creditCardNumber) {
        return null;
    }

    @Override
    public Flux<TransactionReportDTO> getAccountTransactions(String accountNumber) {
        return null;
    }

    @Override
    public Mono<AvaliableBalanceDTO> getCreditCardBalance(String creditCardNumber) {
        return null;
    }

    @Override
    public Mono<AvaliableBalanceDTO> getAccountBalance(String accountNumber) {
        return null;
    }

    @Override
    public Mono<DailyAverageBalanceDTO> getCreditCardDailyAverageBalance(String creditCardNumber) {
        return null;
    }

    @Override
    public Mono<DailyAverageBalanceDTO> getAccountDailyAverageBalance(String accountNumber) {
        return null;
    }

    @Override
    public Flux<ChargedFeesDTO> getCreditCardChargedFees(String creditCardNumber) {
        return null;
    }

    @Override
    public Flux<ChargedFeesDTO> getAccountChargedFees(String accountNumber) {
        return null;
    }
}
