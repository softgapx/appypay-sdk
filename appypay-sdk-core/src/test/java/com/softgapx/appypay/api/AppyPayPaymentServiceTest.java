package com.softgapx.appypay.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.softgapx.appypay.dto.charge.CreateChargeResponse;
import com.softgapx.appypay.dto.charge.CreatePaymentRequest;
import com.softgapx.appypay.dto.common.ResponseStatus;
import com.softgapx.appypay.dto.common.SourceDetails;
import com.softgapx.appypay.gateway.PaymentGateway;
import com.softgapx.appypay.usecase.CreatePayment;
import com.softgapx.appypay.usecase.GetPayment;

class AppyPayPaymentServiceTest {

    private CreatePayment createPayment;
    private GetPayment getPayment;
    private PaymentGateway paymentGateway;
    private AppyPayPaymentService paymentService;

    @BeforeEach
    void setUp() {

        createPayment = mock(CreatePayment.class);
        getPayment = mock(GetPayment.class);
        paymentGateway = mock(PaymentGateway.class);

        paymentService = new AppyPayPaymentService(createPayment, getPayment, paymentGateway);
    }

    @Test
    void shouldCreateGpoPayment() {

        PaymentRequest request = new PaymentRequest(
                "ORDERGPO01",
                new BigDecimal("1000.00"),
                "Teste GPO",
                PaymentMethod.GPO,
                "923456789",
                null,
                null
        );

        CreateChargeResponse gatewayResponse = createChargeResponse(
                "payment-gpo-001", "ORDERGPO01", new BigDecimal("1000.00"), "Success", "GPO"
        );

        when(createPayment.execute(any(CreatePaymentRequest.class)))
                .thenReturn(gatewayResponse);

        PaymentResponse result = paymentService.createPayment(request);

        assertNotNull(result);
        assertEquals("payment-gpo-001", result.paymentId());
        assertEquals("ORDERGPO01", result.orderId());
        assertEquals(new BigDecimal("1000.00"), result.amount());
        assertEquals("Success", result.status());

        verify(createPayment).execute(argThat(internalRequest ->
                internalRequest.orderId().equals("ORDERGPO01")
                        && internalRequest.paymentMethod() == PaymentMethod.GPO
                        && internalRequest.phoneNumber().equals("923456789")
        ));
    }

    @Test
    void shouldCreateRefPayment() {

        LocalDateTime dueDate = LocalDateTime.of(2026, 12, 31, 23, 59, 59);

        PaymentRequest request = new PaymentRequest(
                "ORDERREF01",
                new BigDecimal("5000.00"),
                "Teste REF",
                PaymentMethod.REF,
                null,
                "123456789",
                dueDate
        );

        CreateChargeResponse gatewayResponse = createChargeResponse(
                "payment-ref-001", "ORDERREF01", new BigDecimal("5000.00"), "Success", "REF"
        );

        when(createPayment.execute(any(CreatePaymentRequest.class)))
                .thenReturn(gatewayResponse);

        PaymentResponse result = paymentService.createPayment(request);

        assertEquals("payment-ref-001", result.paymentId());
        assertEquals("Success", result.status());

        verify(createPayment).execute(argThat(internalRequest ->
                internalRequest.paymentMethod() == PaymentMethod.REF
                        && internalRequest.referenceNumber().equals("123456789")
                        && internalRequest.dueDate().equals(dueDate)
        ));
    }

    @Test
    void shouldDelegateGetPaymentToUseCase() {

        paymentService.getPayment("txn-001");

        verify(getPayment).execute("txn-001");
    }

    private CreateChargeResponse createChargeResponse(
            String id, String merchantTransactionId, BigDecimal amount, String status, String paymentMethod) {

        return new CreateChargeResponse(
                id,
                merchantTransactionId,
                amount,
                null,
                new ResponseStatus(
                        true, status, 100, "Pagamento registado com sucesso.",
                        paymentMethod, new SourceDetails(1, "NONE", "NONE", "OK")
                )
        );
    }
}
