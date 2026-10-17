package com.softgapx.appypay.response.handler;

import java.util.Set;

import com.softgapx.appypay.dto.charge.CreateChargeResponse;
import com.softgapx.appypay.exception.PaymentGatewayUnavailableException;
import com.softgapx.appypay.response.AppyPayCodeHandler;

public final class GatewayErrorHandler implements AppyPayCodeHandler {

    private static final Set<Integer> CODES = Set.of(301, 306, 308, 411, 900, 901);

    @Override
    public boolean supports(Integer code) {
        return CODES.contains(code);
    }

    @Override
    public CreateChargeResponse handle(CreateChargeResponse response) {
        throw new PaymentGatewayUnavailableException(response.responseStatus().message());
    }
}
