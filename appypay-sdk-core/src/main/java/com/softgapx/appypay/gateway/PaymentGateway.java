package com.softgapx.appypay.gateway;

import com.softgapx.appypay.dto.charge.CreateChargeResponse;
import com.softgapx.appypay.dto.charge.CreatePaymentRequest;
import com.softgapx.appypay.dto.charge.GetChargeResponse;
import com.softgapx.appypay.dto.reference.ReferencePaymentRequest;

public interface PaymentGateway {

    CreateChargeResponse createPayment(CreatePaymentRequest request);

    GetChargeResponse getCharge(String chargeId);

    void simulateReferencePayment(ReferencePaymentRequest request);
}
