package com.softgapx.appypay.dto.charge;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softgapx.appypay.dto.common.Reference;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentDetails(

        String id,

        String merchantTransactionId,

        String type,

        String operation,

        BigDecimal amount,

        String currency,

        String status,

        String description,

        Boolean disputes,

        BigDecimal applicationFeeAmount,

        String paymentMethod,

        LocalDateTime createdDate,

        LocalDateTime updatedDate,

        List<Object> options,

        Reference reference,

        ElectronicReceipt electronicReceipt,

        List<TransactionEvent> transactionEvents

) {
}
