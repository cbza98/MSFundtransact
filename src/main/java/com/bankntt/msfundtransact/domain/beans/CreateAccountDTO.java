package com.bankntt.msfundtransact.domain.beans;

import com.bankntt.msfundtransact.domain.validation.account.IsPeopleSubTypeAccount;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountDTO {
    @NotNull
    private String codeBusinessPartner;
    @NotNull
    private String accountCode;
}
