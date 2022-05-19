package com.bankntt.MSFundtransact.domain.beans;

import javax.validation.constraints.NotNull;

import com.bankntt.MSFundtransact.domain.validation.Account.IsCompanySubTypeAccount;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CompanyCheckingAccountDTO {
	
	@NotNull
	private String codeBusinessPartner;
	
	@IsCompanySubTypeAccount
	@NotNull
	private String subType;
}
