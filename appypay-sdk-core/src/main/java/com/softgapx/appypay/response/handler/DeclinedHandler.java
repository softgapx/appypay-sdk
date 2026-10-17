package com.softgapx.appypay.response.handler;

import java.util.Set;

import com.softgapx.appypay.dto.charge.CreateChargeResponse;
import com.softgapx.appypay.exception.PaymentDeclinedException;
import com.softgapx.appypay.response.AppyPayCodeHandler;

public final class DeclinedHandler implements AppyPayCodeHandler {

    private static final Set<Integer> CODES = Set.of(209, 223, 231);

    @Override
    public boolean supports(Integer code) {
        return CODES.contains(code);
    }

    @Override
    public CreateChargeResponse handle(CreateChargeResponse response) {
        throw new PaymentDeclinedException(response.responseStatus().message());
    }
}
