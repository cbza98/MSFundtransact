package com.bankntt.MSFundtransact.domain.validation.Account;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IstypeaccountValidator implements ConstraintValidator<Istypeaccount, String> {

	List<String> type = Arrays.asList("AH", "CO", "PL", "VI", "PY");

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return type.contains(value);
	}
}
