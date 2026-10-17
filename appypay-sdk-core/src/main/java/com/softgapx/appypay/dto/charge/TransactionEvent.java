package com.softgapx.appypay.dto.charge;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softgapx.appypay.dto.common.ResponseStatus;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TransactionEvent(

        Long id,

        String transactionId,

        String type,

        String providerTransactionId,

        String merchantReceipt,

        Boolean actionStatus,

        String createdDate,

        ResponseStatus responseStatus

) {
}
