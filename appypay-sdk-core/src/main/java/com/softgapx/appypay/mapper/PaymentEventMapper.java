package com.softgapx.appypay.mapper;

import com.softgapx.appypay.dto.charge.GetChargeResponse;
import com.softgapx.appypay.dto.charge.PaymentDetails;
import com.softgapx.appypay.webhook.PaymentEvent;

/** Converte a resposta de consulta de cobrança da AppyPay num {@link PaymentEvent} do SDK. */
public final class PaymentEventMapper {

    public PaymentEvent toEvent(GetChargeResponse response) {

        if (response == null || response.payment() == null) {

            throw new IllegalArgumentException("Resposta de pagamento inválida.");
        }

        PaymentDetails payment = response.payment();

        String status = payment.status();
        String normalizedStatus = status == null ? null : status.trim();

        return new PaymentEvent(
                payment.id(),
                payment.merchantTransactionId(),
                payment.amount(),
                normalizedStatus,
                payment.paymentMethod(),
                payment.reference()
        );
    }
}
