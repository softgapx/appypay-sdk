package com.softgapx.appypay.dto.charge;

import java.math.BigDecimal;

public record CreateChargeRequest(

        BigDecimal amount,
        String currency,
        String description,
        String merchantTransactionId,
        String paymentMethod,
        PaymentInfo paymentInfo,
        Options options

) {
}
