package com.softgapx.appypay.webhook.processor;

import com.softgapx.appypay.webhook.PaymentEvent;

public interface PaymentStatusProcessor {

    boolean supports(String status);

    void process(PaymentEvent event);
}
