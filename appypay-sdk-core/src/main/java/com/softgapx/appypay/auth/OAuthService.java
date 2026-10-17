package com.softgapx.appypay.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softgapx.appypay.api.AppyPayConfig;
import com.softgapx.appypay.dto.oauth.OAuthTokenResponse;
import com.softgapx.appypay.exception.AppyPayAuthenticationException;
import com.softgapx.appypay.exception.PaymentGatewayUnavailableException;
import com.softgapx.appypay.http.AppyPayHttpClient;
import com.softgapx.appypay.http.AppyPayHttpResponse;
import com.softgapx.appypay.mapper.OAuthMapper;

import java.util.Map;

/**
 * Obtém o Access Token junto do endpoint OAuth2 (Client Credentials) da
 * AppyPay, conforme a documentação "Autenticação na API AppyPay".
 */
public final class OAuthService {

    private final AppyPayHttpClient httpClient;
    private final AppyPayConfig config;
    private final OAuthMapper mapper;
    private final ObjectMapper objectMapper;

    public OAuthService(
            AppyPayHttpClient httpClient,
            AppyPayConfig config,
            OAuthMapper mapper,
            ObjectMapper objectMapper) {

        this.httpClient = httpClient;
        this.config = config;
        this.mapper = mapper;
        this.objectMapper = objectMapper;
    }

    public AccessToken authenticate() {

        String formBody = mapper.toFormBody(config);

        AppyPayHttpResponse response = httpClient.post(
                config.authUrl(),
                Map.of("Content-Type", "application/x-www-form-urlencoded"),
                formBody
        );

        if (response.isClientError()) {

            throw new AppyPayAuthenticationException(
                    "Falha na autenticação OAuth2 da AppyPay. "
                            + "Verifique Client ID, Client Secret e Resource."
            );
        }

        if (!response.isSuccessful()) {

            throw new PaymentGatewayUnavailableException(
                    "Erro ao comunicar com o servidor OAuth da AppyPay (HTTP "
                            + response.statusCode() + ")."
            );
        }

        OAuthTokenResponse tokenResponse = parse(response.body());

        if (tokenResponse.accessToken() == null || tokenResponse.accessToken().isBlank()) {

            throw new AppyPayAuthenticationException(
                    "A AppyPay não retornou um Access Token."
            );
        }

        if (tokenResponse.expiresIn() == null || tokenResponse.expiresIn() <= 60) {

            throw new AppyPayAuthenticationException(
                    "A AppyPay retornou um tempo de expiração de token inválido."
            );
        }

        return mapper.toAccessToken(tokenResponse);
    }

    private OAuthTokenResponse parse(String body) {

        try {

            return objectMapper.readValue(body, OAuthTokenResponse.class);

        } catch (Exception ex) {

            throw new PaymentGatewayUnavailableException(
                    "Não foi possível interpretar a resposta OAuth2 da AppyPay.", ex
            );
        }
    }
}
