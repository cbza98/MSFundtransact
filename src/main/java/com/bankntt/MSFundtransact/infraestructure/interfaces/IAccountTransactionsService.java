package com.bankntt.MSFundtransact.infraestructure.interfaces;

import com.bankntt.MSFundtransact.domain.beans.AccountOperationDTO;
import com.bankntt.MSFundtransact.domain.beans.AccountTransferenceDTO;

import com.bankntt.MSFundtransact.domain.entities.Transaction;
import reactor.core.publisher.Mono;

public interface IAccountTransactionsService {
	
	public Mono<Transaction> doAccountDeposit(AccountOperationDTO dto);
	
	public Mono<Transaction> TransferBetweenAccounts(AccountTransferenceDTO dto);
	
	public Mono<Transaction> doTransferToThirdParty(AccountTransferenceDTO dto);
	
}
