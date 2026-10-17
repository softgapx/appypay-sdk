package com.softgapx.appypay.webhook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener padrão que apenas regista o evento em log. Registado
 * automaticamente pelo {@code AppyPaySdk} — não substitui os listeners
 * que o consumidor da lib adicionar, apenas complementa.
 */
public final class LoggingPaymentEventListener implements PaymentEventListener {

    private static final Logger log = LoggerFactory.getLogger(LoggingPaymentEventListener.class);

    @Override
    public void onPaymentEvent(PaymentEvent event) {

        log.info(
                "Payment event received: paymentId={}, merchantTransactionId={}, amount={}, status={}, paymentMethod={}",
                event.paymentId(),
                event.merchantTransactionId(),
                event.amount(),
                event.status(),
                event.paymentMethod()
        );
    }
}
