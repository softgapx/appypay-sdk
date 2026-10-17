package com.softgapx.appypay.response.handler;

import java.util.Set;

import com.softgapx.appypay.dto.charge.CreateChargeResponse;
import com.softgapx.appypay.exception.PaymentExpiredException;
import com.softgapx.appypay.response.AppyPayCodeHandler;

public final class ExpiredHandler implements AppyPayCodeHandler {

    private static final Set<Integer> CODES = Set.of(210, 211, 245);

    @Override
    public boolean supports(Integer code) {
        return CODES.contains(code);
    }

    @Override
    public CreateChargeResponse handle(CreateChargeResponse response) {
        throw new PaymentExpiredException(response.responseStatus().message());
    }
}
