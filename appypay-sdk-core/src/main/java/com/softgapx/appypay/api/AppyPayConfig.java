package com.softgapx.appypay.api;

import java.time.Duration;
import java.util.Objects;

/**
 * Configuração de acesso à API da AppyPay: credenciais, URLs de ambiente
 * (Sandbox/Produção) e timeouts de rede.
 * <p>
 * É um objeto imutável, construído através de {@link #builder()}. Use
 * {@link Builder#sandbox()} ou {@link Builder#production()} para
 * pré-preencher automaticamente as URLs e o Resource ID corretos do
 * ambiente, conforme a documentação oficial da AppyPay — só é preciso
 * fornecer as credenciais e os identificadores dos métodos de pagamento.
 *
 * <pre>{@code
 * AppyPayConfig config = AppyPayConfig.builder()
 *         .sandbox()
 *         .clientId(System.getenv("APPYPAY_CLIENT_ID"))
 *         .clientSecret(System.getenv("APPYPAY_CLIENT_SECRET"))
 *         .paymentMethodGpo(System.getenv("APPYPAY_PAYMENT_METHOD_GPO"))
 *         .paymentMethodRef(System.getenv("APPYPAY_PAYMENT_METHOD_REF"))
 *         .build();
 * }</pre>
 */
public record AppyPayConfig(

        String clientId,
        String clientSecret,
        String resource,
        String authUrl,
        String gatewayUrl,
        String paymentMethodGpo,
        String paymentMethodRef,
        Duration oauthConnectTimeout,
        Duration oauthReadTimeout,
        Duration gatewayConnectTimeout,
        Duration gatewayReadTimeout

) {

    public AppyPayConfig {

        requireNonBlank(clientId, "clientId");
        requireNonBlank(clientSecret, "clientSecret");
        requireNonBlank(resource, "resource");
        requireNonBlank(authUrl, "authUrl");
        requireNonBlank(gatewayUrl, "gatewayUrl");
        requireNonBlank(paymentMethodGpo, "paymentMethodGpo");
        requireNonBlank(paymentMethodRef, "paymentMethodRef");

        Objects.requireNonNull(oauthConnectTimeout, "oauthConnectTimeout é obrigatório");
        Objects.requireNonNull(oauthReadTimeout, "oauthReadTimeout é obrigatório");
        Objects.requireNonNull(gatewayConnectTimeout, "gatewayConnectTimeout é obrigatório");
        Objects.requireNonNull(gatewayReadTimeout, "gatewayReadTimeout é obrigatório");
    }

    private static void requireNonBlank(String value, String fieldName) {

        if (value == null || value.isBlank()) {

            throw new IllegalArgumentException(
                    fieldName + " é obrigatório e não pode estar em branco."
            );
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        /*
         * Valores oficiais do ambiente Sandbox da AppyPay,
         * conforme a documentação "Ambiente de Testes (Sandbox)
         * vs. Ambiente de Produção".
         */
        private static final String SANDBOX_AUTH_URL =
                "https://login.microsoftonline.com/appypaydev.onmicrosoft.com/oauth2/token";
        private static final String SANDBOX_GATEWAY_URL =
                "https://gwy-api-tst.appypay.co.ao/v2.0";
        private static final String SANDBOX_RESOURCE =
                "2aed7612-de64-46b5-9e59-1f48f8902d14";

        /*
         * Valores oficiais do ambiente de Produção. O domínio do
         * endpoint de autenticação ("auth.apppay.co.ao") é copiado
         * literalmente da documentação da AppyPay — vale a pena
         * confirmar com o gestor de conta antes de ir para produção.
         */
        private static final String PRODUCTION_AUTH_URL =
                "https://login.microsoftonline.com/auth.apppay.co.ao/oauth2/token";
        private static final String PRODUCTION_GATEWAY_URL =
                "https://gwy-api.appypay.co.ao/v2.0";
        private static final String PRODUCTION_RESOURCE =
                "bee57785-7a19-4f1c-9c8d-aa03f2f0e333";

        private static final Duration DEFAULT_OAUTH_CONNECT_TIMEOUT = Duration.ofMillis(5000);
        private static final Duration DEFAULT_OAUTH_READ_TIMEOUT = Duration.ofMillis(10000);
        private static final Duration DEFAULT_GATEWAY_CONNECT_TIMEOUT = Duration.ofMillis(5000);
        private static final Duration DEFAULT_GATEWAY_READ_TIMEOUT = Duration.ofMillis(15000);

        private String clientId;
        private String clientSecret;
        private String resource;
        private String authUrl;
        private String gatewayUrl;
        private String paymentMethodGpo;
        private String paymentMethodRef;
        private Duration oauthConnectTimeout = DEFAULT_OAUTH_CONNECT_TIMEOUT;
        private Duration oauthReadTimeout = DEFAULT_OAUTH_READ_TIMEOUT;
        private Duration gatewayConnectTimeout = DEFAULT_GATEWAY_CONNECT_TIMEOUT;
        private Duration gatewayReadTimeout = DEFAULT_GATEWAY_READ_TIMEOUT;

        private Builder() {
        }

        /** Pré-preenche authUrl, gatewayUrl e resource para o ambiente Sandbox. */
        public Builder sandbox() {
            this.authUrl = SANDBOX_AUTH_URL;
            this.gatewayUrl = SANDBOX_GATEWAY_URL;
            this.resource = SANDBOX_RESOURCE;
            return this;
        }

        /** Pré-preenche authUrl, gatewayUrl e resource para o ambiente de Produção. */
        public Builder production() {
            this.authUrl = PRODUCTION_AUTH_URL;
            this.gatewayUrl = PRODUCTION_GATEWAY_URL;
            this.resource = PRODUCTION_RESOURCE;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        /** Só é necessário chamar isto se não usar {@link #sandbox()}/{@link #production()}. */
        public Builder resource(String resource) {
            this.resource = resource;
            return this;
        }

        public Builder authUrl(String authUrl) {
            this.authUrl = authUrl;
            return this;
        }

        public Builder gatewayUrl(String gatewayUrl) {
            this.gatewayUrl = gatewayUrl;
            return this;
        }

        public Builder paymentMethodGpo(String paymentMethodGpo) {
            this.paymentMethodGpo = paymentMethodGpo;
            return this;
        }

        public Builder paymentMethodRef(String paymentMethodRef) {
            this.paymentMethodRef = paymentMethodRef;
            return this;
        }

        public Builder oauthConnectTimeout(Duration timeout) {
            this.oauthConnectTimeout = timeout;
            return this;
        }

        public Builder oauthReadTimeout(Duration timeout) {
            this.oauthReadTimeout = timeout;
            return this;
        }

        public Builder gatewayConnectTimeout(Duration timeout) {
            this.gatewayConnectTimeout = timeout;
            return this;
        }

        public Builder gatewayReadTimeout(Duration timeout) {
            this.gatewayReadTimeout = timeout;
            return this;
        }

        public AppyPayConfig build() {

            return new AppyPayConfig(
                    clientId,
                    clientSecret,
                    resource,
                    authUrl,
                    gatewayUrl,
                    paymentMethodGpo,
                    paymentMethodRef,
                    oauthConnectTimeout,
                    oauthReadTimeout,
                    gatewayConnectTimeout,
                    gatewayReadTimeout
            );
        }
    }
}
