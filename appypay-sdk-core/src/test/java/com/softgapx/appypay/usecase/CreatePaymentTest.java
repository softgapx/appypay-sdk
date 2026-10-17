package com.softgapx.appypay.usecase;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.softgapx.appypay.api.PaymentMethod;
import com.softgapx.appypay.dto.charge.CreateChargeResponse;
import com.softgapx.appypay.dto.charge.CreatePaymentRequest;
import com.softgapx.appypay.exception.InvalidPaymentRequestException;
import com.softgapx.appypay.gateway.PaymentGateway;

class CreatePaymentTest {

    private PaymentGateway paymentGateway;
    private CreatePayment createPayment;

    @BeforeEach
    void setUp() {
        paymentGateway = mock(PaymentGateway.class);
        createPayment = new CreatePayment(paymentGateway);
    }

    @Test
    void shouldDelegateToGatewayWhenRequestIsValid() {

        CreatePaymentRequest request = new CreatePaymentRequest(
                "ORDER001", new BigDecimal("100.00"), "Teste",
                PaymentMethod.GPO, "923456789", null, null
        );

        CreateChargeResponse expected = mock(CreateChargeResponse.class);

        when(paymentGateway.createPayment(request)).thenReturn(expected);

        CreateChargeResponse result = createPayment.execute(request);

        assertSame(expected, result);
        verify(paymentGateway).createPayment(request);
    }

    @Test
    void shouldRejectInvalidRequestBeforeCallingGateway() {

        CreatePaymentRequest invalidRequest = new CreatePaymentRequest(
                "ORDER001", new BigDecimal("100.00"), "Teste",
                PaymentMethod.GPO, null, null, null
        );

        assertThrows(
                InvalidPaymentRequestException.class,
                () -> createPayment.execute(invalidRequest)
        );
    }
}
