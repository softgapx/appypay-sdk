package com.softgapx.appypay.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.softgapx.appypay.dto.charge.GetChargeResponse;
import com.softgapx.appypay.dto.charge.PaymentDetails;
import com.softgapx.appypay.webhook.PaymentEvent;

class PaymentEventMapperTest {

    private final PaymentEventMapper mapper = new PaymentEventMapper();

    @Test
    void shouldMapPaymentDetailsToEventAndTrimStatus() {

        PaymentDetails details = new PaymentDetails(
                "payment-001", "ORDER001", "CHARGE", "DEBIT",
                new BigDecimal("100.00"), "AOA", "Success ", "desc",
                false, null, "GPO", null, null, null, null, null, null
        );

        PaymentEvent event = mapper.toEvent(new GetChargeResponse(details));

        assertEquals("payment-001", event.paymentId());
        assertEquals("ORDER001", event.merchantTransactionId());
        assertEquals("Success", event.status());
        assertEquals("GPO", event.paymentMethod());
    }

    @Test
    void shouldRejectResponseWithoutPayment() {

        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.toEvent(new GetChargeResponse(null))
        );
    }
}
