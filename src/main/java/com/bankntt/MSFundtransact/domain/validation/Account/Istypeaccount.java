package com.bankntt.MSFundtransact.domain.validation.Account;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = IstypeaccountValidator.class)
@Documented
public @interface Istypeaccount {
	String message() default "Type Account is no valid.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
