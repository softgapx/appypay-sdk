package com.softgapx.appypay.response.handler;

import java.util.Set;

import com.softgapx.appypay.dto.charge.CreateChargeResponse;
import com.softgapx.appypay.response.AppyPayCodeHandler;

public final class SuccessHandler implements AppyPayCodeHandler {

    private static final Set<Integer> CODES = Set.of(100);

    @Override
    public boolean supports(Integer code) {
        return CODES.contains(code);
    }

    @Override
    public CreateChargeResponse handle(CreateChargeResponse response) {
        return response;
    }
}
