package com.softgapx.appypay.spring;

import java.time.Duration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.softgapx.appypay.api.AppyPayConfig;
import com.softgapx.appypay.api.AppyPaySdk;
import com.softgapx.appypay.api.PaymentService;
import com.softgapx.appypay.webhook.PaymentEventListener;
import com.softgapx.appypay.webhook.WebhookEventStore;
import com.softgapx.appypay.webhook.WebhookProcessingService;

/**
 * Auto-configuração Spring Boot do SDK da AppyPay.
 * <p>
 * Ativa-se automaticamente quando {@code appypay-sdk-core} está no
 * classpath e {@code appypay.enabled} não é {@code false} (ver
 * {@link AppyPayProperties}). Constrói o {@code AppyPaySdk} exatamente
 * como faria uma aplicação Java pura — não há nenhuma "mágica" além de
 * ligar as propriedades do Spring ao {@code AppyPayConfig.Builder} do
 * core.
 * <p>
 * Todos os beans são {@code @ConditionalOnMissingBean}: se a aplicação
 * já definir o seu próprio bean (ex: um {@code AppyPayConfig}
 * personalizado, ou um {@code WebhookEventStore} apoiado em base de
 * dados), essa definição tem prioridade sobre a desta auto-configuração.
 */
@AutoConfiguration
@ConditionalOnClass(AppyPaySdk.class)
@ConditionalOnProperty(prefix = "appypay", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(AppyPayProperties.class)
public class AppyPayAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AppyPayConfig appyPayConfig(AppyPayProperties properties) {

        AppyPayConfig.Builder builder = AppyPayConfig.builder();

        if ("production".equalsIgnoreCase(properties.getEnvironment())) {
            builder.production();
        } else {
            builder.sandbox();
        }

        builder.clientId(properties.getClientId())
                .clientSecret(properties.getClientSecret())
                .paymentMethodGpo(properties.getPaymentMethodGpo())
                .paymentMethodRef(properties.getPaymentMethodRef());

        applyIfPresent(properties.getOauthConnectTimeoutMs(), builder::oauthConnectTimeout);
        applyIfPresent(properties.getOauthReadTimeoutMs(), builder::oauthReadTimeout);
        applyIfPresent(properties.getGatewayConnectTimeoutMs(), builder::gatewayConnectTimeout);
        applyIfPresent(properties.getGatewayReadTimeoutMs(), builder::gatewayReadTimeout);

        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public AppyPaySdk appyPaySdk(
            AppyPayConfig appyPayConfig,
            ObjectProvider<WebhookEventStore> webhookEventStore,
            ObjectProvider<PaymentEventListener> listeners) {

        AppyPaySdk.Builder builder = AppyPaySdk.builder(appyPayConfig);

        webhookEventStore.ifAvailable(builder::webhookEventStore);
        listeners.orderedStream().forEach(builder::addPaymentEventListener);

        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public PaymentService paymentService(AppyPaySdk appyPaySdk) {
        return appyPaySdk.paymentService();
    }

    @Bean
    @ConditionalOnMissingBean
    public WebhookProcessingService webhookProcessingService(AppyPaySdk appyPaySdk) {
        return appyPaySdk.webhookService();
    }

    private void applyIfPresent(Long millis, java.util.function.Function<Duration, AppyPayConfig.Builder> setter) {

        if (millis != null) {
            setter.apply(Duration.ofMillis(millis));
        }
    }
}
