package com.bankntt.msfundtransact.domain.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.bankntt.msfundtransact.domain.validation.account.IsTypeAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Account {

	@Id
	private String accountId;
	private String accountName;
	@NotNull
	private String codeBusinessPartner;
	private String accountNumber;
	@JsonIgnore
	@Digits(integer =20, fraction=6)
	private BigDecimal balance;
	@NotNull
	@NotBlank
	@IsTypeAccount
	private String accountType;
	@NotNull
	private Date date_Opened;
	@NotNull	
	private Boolean valid;
	private String subType;
	private Boolean minAmountIsRequired;
	private Boolean maintenanceComision;
	private List<Holder> holders;
	private List<Signer> signers;


}