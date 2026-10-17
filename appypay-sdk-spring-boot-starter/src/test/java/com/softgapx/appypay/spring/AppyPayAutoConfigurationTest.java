package com.softgapx.appypay.spring;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.softgapx.appypay.api.AppyPayConfig;
import com.softgapx.appypay.api.AppyPaySdk;
import com.softgapx.appypay.api.PaymentService;
import com.softgapx.appypay.webhook.WebhookEventStore;
import com.softgapx.appypay.webhook.WebhookProcessingService;

class AppyPayAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(AppyPayAutoConfiguration.class));

    @Test
    void shouldCreateAllBeansWithMinimalProperties() {

        contextRunner
                .withPropertyValues(
                        "appypay.environment=sandbox",
                        "appypay.client-id=test-client",
                        "appypay.client-secret=test-secret",
                        "appypay.payment-method-gpo=GPO_test",
                        "appypay.payment-method-ref=REF_test"
                )
                .run((AssertableApplicationContext context) -> {

                    assertThat(context).hasSingleBean(AppyPayConfig.class);
                    assertThat(context).hasSingleBean(AppyPaySdk.class);
                    assertThat(context).hasSingleBean(PaymentService.class);
                    assertThat(context).hasSingleBean(WebhookProcessingService.class);

                    AppyPayConfig config = context.getBean(AppyPayConfig.class);
                    assertThat(config.clientId()).isEqualTo("test-client");
                    assertThat(config.gatewayUrl()).isEqualTo("https://gwy-api-tst.appypay.co.ao/v2.0");
                });
    }

    @Test
    void shouldBackOffWhenDisabled() {

        contextRunner
                .withPropertyValues("appypay.enabled=false")
                .run(context -> assertThat(context).doesNotHaveBean(AppyPaySdk.class));
    }

    @Test
    void shouldRespectUserDefinedWebhookEventStore() {

        contextRunner
                .withUserConfiguration(CustomWebhookEventStoreConfig.class)
                .withPropertyValues(
                        "appypay.client-id=test-client",
                        "appypay.client-secret=test-secret",
                        "appypay.payment-method-gpo=GPO_test",
                        "appypay.payment-method-ref=REF_test"
                )
                .run(context -> {

                    assertThat(context).hasSingleBean(WebhookEventStore.class);
                    assertThat(context.getBean(WebhookEventStore.class))
                            .isInstanceOf(FakeWebhookEventStore.class);
                });
    }

    @Configuration
    static class CustomWebhookEventStoreConfig {

        @Bean
        WebhookEventStore webhookEventStore() {
            return new FakeWebhookEventStore();
        }
    }

    static class FakeWebhookEventStore implements WebhookEventStore {

        @Override
        public boolean exists(String transactionId, String status) {
            return false;
        }

        @Override
        public void save(String transactionId, String status, java.time.Instant processedAt) {
        }
    }
}
