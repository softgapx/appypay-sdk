package com.softgapx.appypay.exception;

/** O pagamento ou a referência expirou. */
@SuppressWarnings("serial")
public class PaymentExpiredException extends AppyPayException {

    public PaymentExpiredException(String message) {
        super(message);
    }
}
