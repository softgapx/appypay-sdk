package com.softgapx.appypay.webhook.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softgapx.appypay.webhook.PaymentEvent;

public final class FailedProcessor implements PaymentStatusProcessor {

    private static final Logger log = LoggerFactory.getLogger(FailedProcessor.class);

    @Override
    public boolean supports(String status) {
        return status != null && "Failed".equalsIgnoreCase(status.trim());
    }

    @Override
    public void process(PaymentEvent event) {

        log.info(
                "Pagamento recusado: paymentId={}, merchantTransactionId={}",
                event.paymentId(),
                event.merchantTransactionId()
        );
    }
}
