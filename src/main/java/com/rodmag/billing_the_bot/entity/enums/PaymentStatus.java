package com.rodmag.billing_the_bot.entity.enums;

public enum PaymentStatus {

    PAID("Pago"),
    NOT_PAID("Não Pago");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}