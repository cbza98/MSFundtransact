package com.bankntt.MSFundtransact.domain.entities;

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
public class Signer {

	@Id
	private String signerid;
	@NotNull
	private String accountid;
	@NotNull
	private String holderid;
}
