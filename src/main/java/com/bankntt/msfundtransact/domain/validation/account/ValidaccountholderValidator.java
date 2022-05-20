package com.bankntt.msfundtransact.domain.validation.account;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidaccountholderValidator implements ConstraintValidator<Validaccountholder, String> {

	List<String> type = Arrays.asList("AH", "CO", "PL", "VI", "PY");

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return type.contains(value);
	}
}
