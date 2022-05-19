package com.bankntt.MSFundtransact.domain.beans;

import com.bankntt.MSFundtransact.domain.validation.Account.IsPeopleSubTypeAccount;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class CreateCreditCardDTO {
    @NotNull
    private String codeBusinessPartner;

    @NotNull
    @Digits(integer = 20, fraction = 6)
    private BigDecimal Limit;
}
