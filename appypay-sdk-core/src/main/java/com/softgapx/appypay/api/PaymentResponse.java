package com.softgapx.appypay.api;

import java.math.BigDecimal;

public record PaymentResponse(

        String paymentId,

        String orderId,

        BigDecimal amount,

        String status

) {
}
