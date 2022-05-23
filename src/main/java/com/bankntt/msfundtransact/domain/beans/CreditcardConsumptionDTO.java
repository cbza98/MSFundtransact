package com.bankntt.msfundtransact.domain.beans;

import lombok.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditcardConsumptionDTO {
    @NotBlank
    private String creditcard;
    @NotNull
    @Digits(integer =20, fraction=6)
    private BigDecimal consumption;

}
