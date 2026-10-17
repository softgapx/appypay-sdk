package com.softgapx.appypay.response;

import java.util.List;

import com.softgapx.appypay.dto.charge.CreateChargeResponse;
import com.softgapx.appypay.dto.common.ResponseStatus;
import com.softgapx.appypay.exception.PaymentGatewayUnavailableException;
import com.softgapx.appypay.response.handler.DeclinedHandler;
import com.softgapx.appypay.response.handler.ExpiredHandler;
import com.softgapx.appypay.response.handler.GatewayErrorHandler;
import com.softgapx.appypay.response.handler.PendingHandler;
import com.softgapx.appypay.response.handler.SuccessHandler;
import com.softgapx.appypay.response.handler.ValidationHandler;

/**
 * Interpreta a resposta de criação de cobrança da AppyPay e traduz os
 * códigos de erro do gateway (ver documentação "Criar uma Cobrança") em
 * exceções de domínio do SDK.
 * <p>
 * Usa o padrão Strategy: cada {@link AppyPayCodeHandler} sabe tratar um
 * grupo de códigos. Isto torna trivial adicionar suporte a novos códigos
 * sem alterar esta classe — basta implementar {@link AppyPayCodeHandler}
 * e incluí-lo na lista passada ao construtor.
 */
public final class AppyPayResponseHandler {

    private final List<AppyPayCodeHandler> handlers;

    public AppyPayResponseHandler(List<AppyPayCodeHandler> handlers) {
        this.handlers = handlers;
    }

    /** Lista de handlers usada por omissão pelo {@code AppyPaySdk}, cobrindo todos os códigos documentados. */
    public static List<AppyPayCodeHandler> defaultHandlers() {

        return List.of(
                new SuccessHandler(),
                new PendingHandler(),
                new DeclinedHandler(),
                new ExpiredHandler(),
                new GatewayErrorHandler(),
                new ValidationHandler()
        );
    }

    public CreateChargeResponse handle(CreateChargeResponse response) {

        if (response == null) {

            throw new PaymentGatewayUnavailableException(
                    "A AppyPay não retornou uma resposta válida."
            );
        }

        ResponseStatus status = response.responseStatus();

        if (status == null) {

            throw new PaymentGatewayUnavailableException(
                    "A AppyPay não retornou o responseStatus."
            );
        }

        if (status.successful()) {
            return response;
        }

        return handleFailure(response, status);
    }

    private CreateChargeResponse handleFailure(CreateChargeResponse response, ResponseStatus status) {

        Integer code = status.code() != null ? status.code() : -1;
        String message = status.message() != null ? status.message() : "Erro desconhecido retornado pela AppyPay.";

        for (AppyPayCodeHandler handler : handlers) {

            if (handler.supports(code)) {
                return handler.handle(response);
            }
        }

        throw new PaymentGatewayUnavailableException(
                "Código AppyPay desconhecido: " + code + " - " + message
        );
    }
}
