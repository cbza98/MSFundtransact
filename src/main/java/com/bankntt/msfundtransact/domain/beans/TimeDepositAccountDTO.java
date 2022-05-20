package com.bankntt.msfundtransact.domain.beans;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeDepositAccountDTO {
	@NotNull
	private String codeBusinessPartner;

}
