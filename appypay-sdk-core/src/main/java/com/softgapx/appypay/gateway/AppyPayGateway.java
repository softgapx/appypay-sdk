package com.softgapx.appypay.gateway;

import com.softgapx.appypay.client.AppyPayClient;
import com.softgapx.appypay.dto.charge.CreateChargeRequest;
import com.softgapx.appypay.dto.charge.CreateChargeResponse;
import com.softgapx.appypay.dto.charge.CreatePaymentRequest;
import com.softgapx.appypay.dto.charge.GetChargeResponse;
import com.softgapx.appypay.dto.reference.ReferencePaymentRequest;
import com.softgapx.appypay.mapper.ChargeMapper;

public final class AppyPayGateway implements PaymentGateway {

    private final AppyPayClient client;
    private final ChargeMapper mapper;

    public AppyPayGateway(AppyPayClient client, ChargeMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    @Override
    public CreateChargeResponse createPayment(CreatePaymentRequest request) {

        CreateChargeRequest chargeRequest = mapper.toChargeRequest(request);

        return client.createCharge(chargeRequest);
    }

    @Override
    public GetChargeResponse getCharge(String transactionId) {
        return client.getCharge(transactionId);
    }

    @Override
    public void simulateReferencePayment(ReferencePaymentRequest request) {

        client.simulateReferencePayment(
                request.entity(),
                request.referenceNumber()
        );
    }
}
