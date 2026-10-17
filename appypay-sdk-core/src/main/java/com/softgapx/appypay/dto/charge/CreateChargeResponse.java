package com.softgapx.appypay.dto.charge;

import java.math.BigDecimal;

import com.softgapx.appypay.dto.common.Reference;
import com.softgapx.appypay.dto.common.ResponseStatus;

public record CreateChargeResponse(

        String id,
        String merchantTransactionId,
        BigDecimal amount,
        Reference reference,
        ResponseStatus responseStatus

) {
}
