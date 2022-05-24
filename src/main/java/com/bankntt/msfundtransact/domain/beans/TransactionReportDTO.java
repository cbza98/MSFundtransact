package com.bankntt.msfundtransact.domain.beans;

import lombok.*;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReportDTO {
    private Date transactionDate;
    @Digits(integer =20, fraction=6)
    private BigDecimal debit;
    @Digits(integer =20, fraction=6)
    private BigDecimal credit;
}
