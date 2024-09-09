package com.br.payment.utils.enums;

public enum PaymentStatus {
    PARTIAL("PARTIAL"),
    TOTAL("TOTAL"),
    EXCEEDS("EXCEEDS");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
