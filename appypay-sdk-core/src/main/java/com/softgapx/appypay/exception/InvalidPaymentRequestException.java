package com.softgapx.appypay.exception;

/** O pedido de pagamento é inválido (validação local ou rejeição da AppyPay). */
@SuppressWarnings("serial")
public class InvalidPaymentRequestException extends AppyPayException {

    public InvalidPaymentRequestException(String message) {
        super(message);
    }
}
