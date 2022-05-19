package com.bankntt.MSFundtransact.domain.beans;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BusinessPartnerBean {
	private String codeBP;
	private String docType;
	private String docNum;
	private String name;
	private String type;
	private String telephone1;
	private String telephone2;
	private String contactPerson;
	private BigDecimal creditCardLine;
	private BigDecimal creditLine;
	private BigDecimal creditCard;
	private BigDecimal debitLine;
	private BigDecimal debitCard;
	private String email;
	private Boolean valid;
}
