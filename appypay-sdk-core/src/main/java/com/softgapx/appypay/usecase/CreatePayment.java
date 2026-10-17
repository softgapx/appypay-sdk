package com.softgapx.appypay.usecase;

import com.softgapx.appypay.dto.charge.CreateChargeResponse;
import com.softgapx.appypay.dto.charge.CreatePaymentRequest;
import com.softgapx.appypay.dto.charge.PaymentRequestValidator;
import com.softgapx.appypay.gateway.PaymentGateway;

public final class CreatePayment {

    private final PaymentGateway paymentGateway;

    public CreatePayment(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public CreateChargeResponse execute(CreatePaymentRequest request) {

        PaymentRequestValidator.validate(request);

        return paymentGateway.createPayment(request);
    }
}
