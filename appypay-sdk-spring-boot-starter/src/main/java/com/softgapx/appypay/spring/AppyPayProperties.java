package com.softgapx.appypay.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propriedades de configuração da AppyPay, lidas de {@code application.properties}
 * ou {@code application.yml} sob o prefixo {@code appypay}.
 *
 * <pre>{@code
 * appypay:
 *   environment: sandbox
 *   client-id: ${APPYPAY_CLIENT_ID}
 *   client-secret: ${APPYPAY_CLIENT_SECRET}
 *   payment-method-gpo: ${APPYPAY_PAYMENT_METHOD_GPO}
 *   payment-method-ref: ${APPYPAY_PAYMENT_METHOD_REF}
 * }</pre>
 */
@ConfigurationProperties(prefix = "appypay")
public class AppyPayProperties {

    /** Desliga toda a auto-configuração deste starter quando definido como {@code false}. */
    private boolean enabled = true;

    /** {@code sandbox} ou {@code production}. Por omissão, {@code sandbox}. */
    private String environment = "sandbox";

    /** Client ID gerado no portal da AppyPay (Sandbox ou Produção). */
    private String clientId;

    /** Client Secret gerado no portal da AppyPay. */
    private String clientSecret;

    /** Identificador do método de pagamento GPO (Multicaixa Express), no formato {@code GPO_<api_key>}. */
    private String paymentMethodGpo;

    /** Identificador do método de pagamento REF (Referência), no formato {@code REF_<api_key>}. */
    private String paymentMethodRef;

    /** Timeout de ligação ao endpoint OAuth2, em milissegundos. Omitir para usar o valor por omissão do SDK. */
    private Long oauthConnectTimeoutMs;

    /** Timeout de leitura do endpoint OAuth2, em milissegundos. Omitir para usar o valor por omissão do SDK. */
    private Long oauthReadTimeoutMs;

    /** Timeout de ligação ao gateway de pagamentos, em milissegundos. Omitir para usar o valor por omissão do SDK. */
    private Long gatewayConnectTimeoutMs;

    /** Timeout de leitura do gateway de pagamentos, em milissegundos. Omitir para usar o valor por omissão do SDK. */
    private Long gatewayReadTimeoutMs;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getPaymentMethodGpo() {
        return paymentMethodGpo;
    }

    public void setPaymentMethodGpo(String paymentMethodGpo) {
        this.paymentMethodGpo = paymentMethodGpo;
    }

    public String getPaymentMethodRef() {
        return paymentMethodRef;
    }

    public void setPaymentMethodRef(String paymentMethodRef) {
        this.paymentMethodRef = paymentMethodRef;
    }

    public Long getOauthConnectTimeoutMs() {
        return oauthConnectTimeoutMs;
    }

    public void setOauthConnectTimeoutMs(Long oauthConnectTimeoutMs) {
        this.oauthConnectTimeoutMs = oauthConnectTimeoutMs;
    }

    public Long getOauthReadTimeoutMs() {
        return oauthReadTimeoutMs;
    }

    public void setOauthReadTimeoutMs(Long oauthReadTimeoutMs) {
        this.oauthReadTimeoutMs = oauthReadTimeoutMs;
    }

    public Long getGatewayConnectTimeoutMs() {
        return gatewayConnectTimeoutMs;
    }

    public void setGatewayConnectTimeoutMs(Long gatewayConnectTimeoutMs) {
        this.gatewayConnectTimeoutMs = gatewayConnectTimeoutMs;
    }

    public Long getGatewayReadTimeoutMs() {
        return gatewayReadTimeoutMs;
    }

    public void setGatewayReadTimeoutMs(Long gatewayReadTimeoutMs) {
        this.gatewayReadTimeoutMs = gatewayReadTimeoutMs;
    }
}
