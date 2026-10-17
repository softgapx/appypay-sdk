package com.softgapx.appypay.gateway;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.softgapx.appypay.api.PaymentMethod;
import com.softgapx.appypay.client.AppyPayClient;
import com.softgapx.appypay.dto.charge.CreateChargeRequest;
import com.softgapx.appypay.dto.charge.CreateChargeResponse;
import com.softgapx.appypay.dto.charge.CreatePaymentRequest;
import com.softgapx.appypay.mapper.ChargeMapper;

class AppyPayGatewayTest {

    private AppyPayClient client;
    private ChargeMapper mapper;
    private AppyPayGateway gateway;

    @BeforeEach
    void setUp() {
        client = mock(AppyPayClient.class);
        mapper = mock(ChargeMapper.class);
        gateway = new AppyPayGateway(client, mapper);
    }

    @Test
    void shouldCreatePaymentSuccessfully() {

        CreatePaymentRequest request = new CreatePaymentRequest(
                "ORDER001", new BigDecimal("100.00"), "Teste GPO",
                PaymentMethod.GPO, "923456789", null, null
        );

        CreateChargeRequest chargeRequest = mock(CreateChargeRequest.class);
        CreateChargeResponse expectedResponse = mock(CreateChargeResponse.class);

        when(mapper.toChargeRequest(request)).thenReturn(chargeRequest);
        when(client.createCharge(chargeRequest)).thenReturn(expectedResponse);

        CreateChargeResponse result = gateway.createPayment(request);

        assertSame(expectedResponse, result);
        verify(mapper).toChargeRequest(request);
        verify(client).createCharge(chargeRequest);
    }
}
