package com.bankntt.msfundtransact.infraestructure.interfaces;

import com.bankntt.msfundtransact.domain.beans.CreditCardPaymentDTO;
import com.bankntt.msfundtransact.domain.beans.CreditPaymentDTO;
import com.bankntt.msfundtransact.domain.beans.CreditcardConsumptionDTO;
import com.bankntt.msfundtransact.domain.beans.NewCreditDTO;
import com.bankntt.msfundtransact.domain.entities.Credit;

import com.bankntt.msfundtransact.domain.entities.CreditCard;
import com.bankntt.msfundtransact.domain.entities.Transaction;
import reactor.core.publisher.Mono;

public interface ICreditTransactionService {
	
	public Mono<Credit> doCreditPayment (CreditPaymentDTO dto);
	
	public Mono<Transaction> doCreditCardPayment (CreditCardPaymentDTO dto);
	public Mono<Transaction> doCreditcardConsumption(CreditcardConsumptionDTO dto);
	public Mono<Credit> getNewCredit(NewCreditDTO dto);
	
}
