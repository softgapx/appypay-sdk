package com.softgapx.appypay.response;

import com.softgapx.appypay.dto.charge.CreateChargeResponse;

/**
 * Estratégia de tratamento para um grupo de códigos de resposta da AppyPay
 * (ex: recusado, expirado, pendente). Ver implementações no pacote
 * {@code response.handler}.
 */
public interface AppyPayCodeHandler {

    boolean supports(Integer code);

    /** Trata a resposta — tipicamente lança a exceção de domínio correspondente. */
    CreateChargeResponse handle(CreateChargeResponse response);
}
