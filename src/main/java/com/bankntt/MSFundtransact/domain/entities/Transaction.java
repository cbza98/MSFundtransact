/**
 * 
 */
package com.bankntt.MSFundtransact.domain.entities;

import com.bankntt.MSFundtransact.domain.enums.TransactionType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "transactions")
public class Transaction {
	
	@Id
    private String transactionId;
    @NotNull
    private TransactionType transactiontype;
    private String fromaccount;
    private String toaccount;
    private Date createDate;
    @NotNull
    @Digits(integer =20, fraction=6)
    private BigDecimal amount;
    private String creditcard;
    private String creditid;

	
}
