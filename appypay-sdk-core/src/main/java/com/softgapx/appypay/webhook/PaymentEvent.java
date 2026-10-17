package com.softgapx.appypay.webhook;

import java.math.BigDecimal;

import com.softgapx.appypay.dto.common.Reference;

public record PaymentEvent(

        String paymentId,

        String merchantTransactionId,

        BigDecimal amount,

        String status,

        String paymentMethod,

        Reference reference

) {

    public PaymentEvent(
            String paymentId,
            String merchantTransactionId,
            BigDecimal amount,
            String status,
            String paymentMethod) {

        this(paymentId, merchantTransactionId, amount, status, paymentMethod, null);
    }
}
