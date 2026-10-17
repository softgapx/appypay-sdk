package com.softgapx.appypay.mapper;

import com.softgapx.appypay.api.AppyPayConfig;
import com.softgapx.appypay.api.PaymentMethod;
import com.softgapx.appypay.dto.charge.CreateChargeRequest;
import com.softgapx.appypay.dto.charge.CreatePaymentRequest;
import com.softgapx.appypay.dto.charge.PaymentInfo;

/** Converte o pedido público {@link CreatePaymentRequest} no formato exigido pela API da AppyPay. */
public final class ChargeMapper {

    private static final String CURRENCY = "AOA";

    private final AppyPayConfig config;

    public ChargeMapper(AppyPayConfig config) {
        this.config = config;
    }

    public CreateChargeRequest toChargeRequest(CreatePaymentRequest request) {

        PaymentInfo paymentInfo;
        String paymentMethod;

        if (request.paymentMethod() == PaymentMethod.GPO) {

            paymentInfo = new PaymentInfo(request.phoneNumber(), null, null);
            paymentMethod = config.paymentMethodGpo();

        } else if (request.paymentMethod() == PaymentMethod.REF) {

            paymentInfo = new PaymentInfo(null, request.referenceNumber(), request.dueDate());
            paymentMethod = config.paymentMethodRef();

        } else {

            paymentInfo = new PaymentInfo(null, null, null);
            paymentMethod = null;
        }

        return new CreateChargeRequest(
                request.amount(),
                CURRENCY,
                request.description(),
                request.orderId(),
                paymentMethod,
                paymentInfo,
                null
        );
    }
}
