package com.softgapx.appypay.response.handler;

import java.util.Set;

import com.softgapx.appypay.dto.charge.CreateChargeResponse;
import com.softgapx.appypay.exception.PaymentPendingException;
import com.softgapx.appypay.response.AppyPayCodeHandler;

public final class PendingHandler implements AppyPayCodeHandler {

    private static final Set<Integer> CODES = Set.of(101, 319);

    @Override
    public boolean supports(Integer code) {
        return CODES.contains(code);
    }

    @Override
    public CreateChargeResponse handle(CreateChargeResponse response) {
        throw new PaymentPendingException(response.responseStatus().message());
    }
}
