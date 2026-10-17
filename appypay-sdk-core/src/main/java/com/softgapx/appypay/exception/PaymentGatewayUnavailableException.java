package com.softgapx.appypay.exception;

/** Erro de comunicação com a AppyPay ou resposta inesperada do gateway. */
@SuppressWarnings("serial")
public class PaymentGatewayUnavailableException extends AppyPayException {

    public PaymentGatewayUnavailableException(String message) {
        super(message);
    }

    public PaymentGatewayUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
