package com.bankntt.msfundtransact.infraestructure.interfaces;

import com.bankntt.msfundtransact.domain.beans.*;
import com.bankntt.msfundtransact.domain.entities.Credit;

import com.bankntt.msfundtransact.domain.entities.CreditCard;
import com.bankntt.msfundtransact.domain.entities.Transaction;
import reactor.core.publisher.Mono;

public interface ICreditTransactionService {
	
	public Mono<Transaction> doCreditPayment (CreditPaymentDTO dto);
	public Mono<Transaction> doCreditConsumption (CreditConsumptionDTO dto);
	public Mono<Transaction> doCreditCardPayment (CreditCardPaymentDTO dto);
	public Mono<Transaction> doCreditcardConsumption(CreditcardConsumptionDTO dto);
	public Mono<Transaction> getNewCredit(NewCreditDTO dto);
	
}
