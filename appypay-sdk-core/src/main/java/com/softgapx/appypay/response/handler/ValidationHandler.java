package com.softgapx.appypay.response.handler;

import java.util.Set;

import com.softgapx.appypay.dto.charge.CreateChargeResponse;
import com.softgapx.appypay.exception.InvalidPaymentRequestException;
import com.softgapx.appypay.response.AppyPayCodeHandler;

public final class ValidationHandler implements AppyPayCodeHandler {

    private static final Set<Integer> CODES = Set.of(717, 718, 719, 720, 726, 760);

    @Override
    public boolean supports(Integer code) {
        return CODES.contains(code);
    }

    @Override
    public CreateChargeResponse handle(CreateChargeResponse response) {
        throw new InvalidPaymentRequestException(response.responseStatus().message());
    }
}
