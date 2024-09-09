package com.br.payment.dto;

import com.br.payment.utils.enums.PaymentStatus;

import java.math.BigDecimal;

public class PaymentDTO {
    private String codeCharge;
    private BigDecimal paymentValue;
    private PaymentStatus status;

    public String getCodeCharge() {
        return codeCharge;
    }

    public void setCodeCharge(String codeCharge) {
        this.codeCharge = codeCharge;
    }

    public BigDecimal getPaymentValue() {
        return paymentValue;
    }

    public void setPaymentValue(BigDecimal paymentValue) {
        this.paymentValue = paymentValue;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
