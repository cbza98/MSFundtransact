package com.bankntt.MSFundtransact.infraestructure.interfaces;

import com.bankntt.MSFundtransact.domain.beans.AccountDepositDTO;
import com.bankntt.MSFundtransact.domain.beans.AccountTransferenceDTO;

import reactor.core.publisher.Mono;

public interface IAccountTransactionsService {
	
	public Mono<?> doAccountDeposit(AccountDepositDTO dto);
	
	public Mono<?> doTransferBetweenAccounts(AccountTransferenceDTO dto);
	
	public Mono<?> doTransferToThirdParty(AccountTransferenceDTO dto);
	
}
