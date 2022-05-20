package com.bankntt.msfundtransact.domain.beans;


import javax.validation.constraints.NotNull;

import com.bankntt.msfundtransact.domain.validation.account.IsPeopleSubTypeAccount;

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
