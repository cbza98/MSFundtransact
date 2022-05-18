package com.bankntt.MSFundtransact.infraestructure.interfaces;

import com.bankntt.MSFundtransact.domain.beans.CreditCardPaymentDTO;
import com.bankntt.MSFundtransact.domain.beans.CreditPaymentDTO;
import com.bankntt.MSFundtransact.domain.beans.NewCreditDTO;

import com.bankntt.MSFundtransact.domain.entities.Credit;
import reactor.core.publisher.Mono;

public interface ICreditTransactionService {
	
	public Mono<Credit> doCreditPayment (CreditPaymentDTO dto);
	
	public Mono<Credit> doCreditCardPayment (CreditCardPaymentDTO dto);
	
	public Mono<Credit> getNewCredit(NewCreditDTO dto);
	
}
