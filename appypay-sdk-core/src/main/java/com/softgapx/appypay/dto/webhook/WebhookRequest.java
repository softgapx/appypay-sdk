package com.softgapx.appypay.dto.webhook;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softgapx.appypay.dto.common.Reference;
import com.softgapx.appypay.dto.common.ResponseStatus;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WebhookRequest(

        String id,

        String merchantTransactionId,

        BigDecimal amount,

        Reference reference,

        ResponseStatus responseStatus

) {
}
