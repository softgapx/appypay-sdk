package com.softgapx.appypay.exception;

/** O pagamento foi recusado pela AppyPay (ex: saldo insuficiente). */
@SuppressWarnings("serial")
public class PaymentDeclinedException extends AppyPayException {

    public PaymentDeclinedException(String message) {
        super(message);
    }
}
