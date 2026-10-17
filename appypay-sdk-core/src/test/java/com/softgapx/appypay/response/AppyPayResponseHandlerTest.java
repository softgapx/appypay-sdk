package com.softgapx.appypay.response;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.softgapx.appypay.dto.charge.CreateChargeResponse;
import com.softgapx.appypay.dto.common.ResponseStatus;
import com.softgapx.appypay.exception.InvalidPaymentRequestException;
import com.softgapx.appypay.exception.PaymentDeclinedException;
import com.softgapx.appypay.exception.PaymentExpiredException;
import com.softgapx.appypay.exception.PaymentGatewayUnavailableException;
import com.softgapx.appypay.exception.PaymentPendingException;

class AppyPayResponseHandlerTest {

    private final AppyPayResponseHandler handler =
            new AppyPayResponseHandler(AppyPayResponseHandler.defaultHandlers());

    @Test
    void shouldReturnResponseWhenSuccessful() {

        CreateChargeResponse response = response(true, 100, "OK");

        assertSame(response, handler.handle(response));
    }

    @Test
    void shouldThrowDeclinedForDeclinedCodes() {

        assertThrows(
                PaymentDeclinedException.class,
                () -> handler.handle(response(false, 209, "Saldo insuficiente"))
        );
    }

    @Test
    void shouldThrowExpiredForExpiredCodes() {

        assertThrows(
                PaymentExpiredException.class,
                () -> handler.handle(response(false, 210, "Expirado"))
        );
    }

    @Test
    void shouldThrowPendingForPendingCodes() {

        assertThrows(
                PaymentPendingException.class,
                () -> handler.handle(response(false, 101, "Pendente"))
        );
    }

    @Test
    void shouldThrowInvalidRequestForValidationCodes() {

        assertThrows(
                InvalidPaymentRequestException.class,
                () -> handler.handle(response(false, 717, "Inválido"))
        );
    }

    @Test
    void shouldThrowGatewayUnavailableForUnknownCode() {

        assertThrows(
                PaymentGatewayUnavailableException.class,
                () -> handler.handle(response(false, 9999, "Desconhecido"))
        );
    }

    @Test
    void shouldThrowGatewayUnavailableWhenResponseIsNull() {

        assertThrows(
                PaymentGatewayUnavailableException.class,
                () -> handler.handle(null)
        );
    }

    private CreateChargeResponse response(boolean successful, int code, String message) {

        return new CreateChargeResponse(
                "id-1", "ORDER001", null, null,
                new ResponseStatus(successful, "status", code, message, "GPO", null)
        );
    }
}
