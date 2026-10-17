package com.softgapx.appypay.dto.charge;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GetChargeResponse(

        PaymentDetails payment

) {
}
