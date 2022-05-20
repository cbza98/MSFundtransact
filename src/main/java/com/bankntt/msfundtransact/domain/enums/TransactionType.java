package com.bankntt.msfundtransact.domain.enums;

public enum TransactionType {
    DEPOSIT("01"),
    WITHDRAWAL("02"),
    THIRD_PARTY_TRANSFER("03"),
    SAME_HOLDER_TRANSFER("04"),
    CREDIT_PAYMENT("05"),
    CREDIT_CARD_PAYMENT("06"),
    CREDIT_CARD_CONSUMPTION("07");
    public final String type;

    private TransactionType(String type) {
        this.type = type;
    }
}
