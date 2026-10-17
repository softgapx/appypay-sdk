package com.softgapx.appypay.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.softgapx.appypay.api.AppyPayConfig;
import com.softgapx.appypay.api.PaymentMethod;
import com.softgapx.appypay.dto.charge.CreateChargeRequest;
import com.softgapx.appypay.dto.charge.CreatePaymentRequest;

class ChargeMapperTest {

    private ChargeMapper mapper;

    @BeforeEach
    void setUp() {

        AppyPayConfig config = AppyPayConfig.builder()
                .authUrl("https://auth.test")
                .gatewayUrl("https://gateway.test")
                .clientId("client-id")
                .clientSecret("client-secret")
                .resource("resource")
                .paymentMethodGpo("GPO_TEST")
                .paymentMethodRef("REF_TEST")
                .build();

        mapper = new ChargeMapper(config);
    }

    @Test
    void shouldMapCreatePaymentRequestToCreateChargeRequest() {

        CreatePaymentRequest request = new CreatePaymentRequest(
                "ORDER001", new BigDecimal("100.00"), "Teste GPO",
                PaymentMethod.GPO, "923456789", null, null
        );

        CreateChargeRequest result = mapper.toChargeRequest(request);

        assertNotNull(result);
        assertEquals(new BigDecimal("100.00"), result.amount());
        assertEquals("AOA", result.currency());
        assertEquals("Teste GPO", result.description());
        assertEquals("ORDER001", result.merchantTransactionId());
        assertEquals("GPO_TEST", result.paymentMethod());
        assertNotNull(result.paymentInfo());
        assertEquals("923456789", result.paymentInfo().phoneNumber());
    }

    @Test
    void shouldMapReferencePaymentRequestToChargeRequest() {

        CreatePaymentRequest request = new CreatePaymentRequest(
                "ORDERREF01", new BigDecimal("5000.00"), "Teste REF",
                PaymentMethod.REF, null, "123456789",
                LocalDateTime.of(2026, 12, 31, 23, 59, 59)
        );

        CreateChargeRequest result = mapper.toChargeRequest(request);

        assertEquals("REF_TEST", result.paymentMethod());
        assertNull(result.paymentInfo().phoneNumber());
        assertEquals("123456789", result.paymentInfo().referenceNumber());
        assertEquals(LocalDateTime.of(2026, 12, 31, 23, 59, 59), result.paymentInfo().dueDate());
    }
}
