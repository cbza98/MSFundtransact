package com.bankntt.MSFundtransact.domain.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	@Id
	private String cardNumber;
	@NotNull
	private String cardName;
	private String expiringDate;
	@Digits(integer = 20, fraction = 6)
	private BigDecimal approvedline;
	@Digits(integer = 20, fraction = 6)
	private BigDecimal availableline;
	@Digits(integer = 20, fraction = 6)
	private BigDecimal consumedline;
	@NotNull
	private String cvv;
	@NotNull
	private Boolean valid;
	@NotNull
	private Date openDate;
	@NotNull
	private String codeBusinessPartner;
	@NotNull
	@JsonIgnore
	private String expiringyear;
	@NotNull
	@JsonIgnore
	private String expiringmonth;

}
