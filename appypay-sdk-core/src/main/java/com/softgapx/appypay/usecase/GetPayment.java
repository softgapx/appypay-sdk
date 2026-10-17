package com.softgapx.appypay.usecase;

import com.softgapx.appypay.dto.charge.GetChargeResponse;
import com.softgapx.appypay.gateway.PaymentGateway;

public final class GetPayment {

    private final PaymentGateway paymentGateway;

    public GetPayment(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public GetChargeResponse execute(String transactionId) {
        return paymentGateway.getCharge(transactionId);
    }
}
