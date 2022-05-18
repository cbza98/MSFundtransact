package com.bankntt.MSFundtransact.domain.beans;


import javax.validation.constraints.NotNull;

import com.bankntt.MSFundtransact.domain.validation.Account.IsPeopleSubTypeAccount;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SavingAccountDTO {
	@NotNull
	private String codeBusinessPartner;
	
	@NotNull
	@IsPeopleSubTypeAccount
	private String subType;
}
