package com.bankntt.MSFundtransact.domain.entities;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

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
public class CreditCard {
	@NotNull
	private String cardname;
	@Id
	private String cardnumber;
	@NotNull
	private Date ddate;
	@NotNull
	private String ccv;
	@NotNull
	private Boolean valid;

}
