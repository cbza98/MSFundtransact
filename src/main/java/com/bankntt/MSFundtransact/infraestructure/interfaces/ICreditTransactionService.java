package com.bankntt.MSFundtransact.infraestructure.interfaces;

import com.bankntt.MSFundtransact.domain.beans.CreditCardPaymentDTO;
import com.bankntt.MSFundtransact.domain.beans.CreditPaymentDTO;
import com.bankntt.MSFundtransact.domain.beans.NewCreditDTO;

import reactor.core.publisher.Mono;

public interface ICreditTransactionService {
	
	public Mono<?> doCreditPayment (CreditPaymentDTO dto);
	
	public Mono<?> doCreditCardPayment (CreditCardPaymentDTO dto);
	
	public Mono<?> getNewCredit(NewCreditDTO dto);
	
}
