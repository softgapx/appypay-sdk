package com.softgapx.appypay.exception;

/** O pagamento está pendente de confirmação. */
@SuppressWarnings("serial")
public class PaymentPendingException extends AppyPayException {

    public PaymentPendingException(String message) {
        super(message);
    }
}
