package com.softgapx.appypay.api;

import com.softgapx.appypay.dto.charge.CreateChargeResponse;
import com.softgapx.appypay.dto.charge.CreatePaymentRequest;
import com.softgapx.appypay.dto.charge.GetChargeResponse;
import com.softgapx.appypay.dto.reference.ReferencePaymentRequest;
import com.softgapx.appypay.gateway.PaymentGateway;
import com.softgapx.appypay.usecase.CreatePayment;
import com.softgapx.appypay.usecase.GetPayment;

public final class AppyPayPaymentService implements PaymentService {

    private final CreatePayment createPayment;
    private final GetPayment getPayment;
    private final PaymentGateway paymentGateway;

    public AppyPayPaymentService(
            CreatePayment createPayment,
            GetPayment getPayment,
            PaymentGateway paymentGateway) {

        this.createPayment = createPayment;
        this.getPayment = getPayment;
        this.paymentGateway = paymentGateway;
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {

        CreatePaymentRequest internalRequest = new CreatePaymentRequest(
                request.orderId(),
                request.amount(),
                request.description(),
                request.paymentMethod(),
                request.phoneNumber(),
                request.referenceNumber(),
                request.dueDate()
        );

        CreateChargeResponse response = createPayment.execute(internalRequest);

        return new PaymentResponse(
                response.id(),
                response.merchantTransactionId(),
                response.amount(),
                response.responseStatus().status()
        );
    }

    @Override
    public GetChargeResponse getPayment(String transactionId) {
        return getPayment.execute(transactionId);
    }

    @Override
    public void simulateReferencePayment(ReferencePaymentRequest request) {
        paymentGateway.simulateReferencePayment(request);
    }
}
