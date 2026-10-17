package com.softgapx.appypay.client;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softgapx.appypay.auth.AccessTokenProvider;
import com.softgapx.appypay.dto.charge.CreateChargeRequest;
import com.softgapx.appypay.dto.charge.CreateChargeResponse;
import com.softgapx.appypay.dto.charge.GetChargeResponse;
import com.softgapx.appypay.dto.reference.ReferencePaymentRequest;
import com.softgapx.appypay.exception.PaymentGatewayUnavailableException;
import com.softgapx.appypay.http.AppyPayHttpClient;
import com.softgapx.appypay.http.AppyPayHttpResponse;
import com.softgapx.appypay.response.AppyPayResponseHandler;
import com.softgapx.appypay.util.AppyPayEndpoints;
import com.softgapx.appypay.util.OAuthConstants;

/**
 * Cliente de baixo nível do gateway de pagamentos da AppyPay (endpoints
 * {@code /charges}, {@code /charges/{id}} e {@code /mocks/referenceProcessing}),
 * conforme a documentação oficial. Não expõe estado de negócio — é usado
 * internamente por {@link com.softgapx.appypay.gateway.AppyPayGateway}.
 */
public final class AppyPayClient {

    private final AppyPayHttpClient httpClient;
    private final String gatewayUrl;
    private final AccessTokenProvider tokenProvider;
    private final AppyPayResponseHandler responseHandler;
    private final ObjectMapper objectMapper;

    public AppyPayClient(
            AppyPayHttpClient httpClient,
            String gatewayUrl,
            AccessTokenProvider tokenProvider,
            AppyPayResponseHandler responseHandler,
            ObjectMapper objectMapper) {

        this.httpClient = httpClient;
        this.gatewayUrl = gatewayUrl;
        this.tokenProvider = tokenProvider;
        this.responseHandler = responseHandler;
        this.objectMapper = objectMapper;
    }

    public CreateChargeResponse createCharge(CreateChargeRequest request) {

        AppyPayHttpResponse response = httpClient.post(
                gatewayUrl + AppyPayEndpoints.CHARGES,
                authenticatedJsonHeaders(),
                writeJson(request)
        );

        return responseHandler.handle(
                readJson(response, CreateChargeResponse.class)
        );
    }

    public GetChargeResponse getCharge(String transactionId) {

        String uri = gatewayUrl + String.format(AppyPayEndpoints.CHARGE_BY_ID, transactionId);

        AppyPayHttpResponse response = httpClient.get(uri, authenticatedHeaders());

        return readJson(response, GetChargeResponse.class);
    }

    public void simulateReferencePayment(String entity, String referenceNumber) {

        ReferencePaymentRequest request = new ReferencePaymentRequest(entity, referenceNumber);

        AppyPayHttpResponse response = httpClient.post(
                gatewayUrl + AppyPayEndpoints.REFERENCE_PROCESSING,
                authenticatedJsonHeaders(),
                writeJson(request)
        );

        if (!response.isSuccessful()) {

            throw new PaymentGatewayUnavailableException(
                    "Falha ao simular pagamento por referência (HTTP " + response.statusCode() + ")."
            );
        }
    }

    private Map<String, String> authenticatedHeaders() {

        return Map.of(
                "Authorization", OAuthConstants.BEARER + tokenProvider.getAccessToken(),
                "Accept", "application/json"
        );
    }

    private Map<String, String> authenticatedJsonHeaders() {

        return Map.of(
                "Authorization", OAuthConstants.BEARER + tokenProvider.getAccessToken(),
                "Content-Type", "application/json",
                "Accept", "application/json"
        );
    }

    private String writeJson(Object body) {

        try {

            return objectMapper.writeValueAsString(body);

        } catch (Exception ex) {

            throw new PaymentGatewayUnavailableException(
                    "Não foi possível serializar o pedido para a AppyPay.", ex
            );
        }
    }

    private <T> T readJson(AppyPayHttpResponse response, Class<T> type) {

        if (response.body() == null || response.body().isBlank()) {

            throw new PaymentGatewayUnavailableException(
                    "A AppyPay não retornou corpo na resposta (HTTP " + response.statusCode() + ")."
            );
        }

        try {

            return objectMapper.readValue(response.body(), type);

        } catch (Exception ex) {

            throw new PaymentGatewayUnavailableException(
                    "Não foi possível interpretar a resposta da AppyPay.", ex
            );
        }
    }
}
