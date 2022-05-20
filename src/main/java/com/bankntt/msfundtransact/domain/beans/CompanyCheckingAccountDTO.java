package com.bankntt.msfundtransact.domain.beans;

import javax.validation.constraints.NotNull;

import com.bankntt.msfundtransact.domain.validation.account.IsCompanySubTypeAccount;

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
