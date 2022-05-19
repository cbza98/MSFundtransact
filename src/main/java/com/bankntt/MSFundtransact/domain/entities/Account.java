package com.bankntt.MSFundtransact.domain.entities;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

import com.bankntt.MSFundtransact.domain.validation.Account.IsTypeAccount;

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
public class Account {

	@Id
	private String accountId;
	private String accountName;
	@NotNull
	private String codeBusinessPartner;
	private String accountNumber;
	
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