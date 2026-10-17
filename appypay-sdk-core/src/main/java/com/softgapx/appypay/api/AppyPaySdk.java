package com.softgapx.appypay.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softgapx.appypay.auth.AccessTokenProvider;
import com.softgapx.appypay.auth.CachedAccessTokenProvider;
import com.softgapx.appypay.auth.OAuthService;
import com.softgapx.appypay.client.AppyPayClient;
import com.softgapx.appypay.gateway.AppyPayGateway;
import com.softgapx.appypay.gateway.PaymentGateway;
import com.softgapx.appypay.http.AppyPayHttpClient;
import com.softgapx.appypay.http.AppyPayJson;
import com.softgapx.appypay.mapper.ChargeMapper;
import com.softgapx.appypay.mapper.OAuthMapper;
import com.softgapx.appypay.mapper.PaymentEventMapper;
import com.softgapx.appypay.response.AppyPayResponseHandler;
import com.softgapx.appypay.usecase.CreatePayment;
import com.softgapx.appypay.usecase.GetPayment;
import com.softgapx.appypay.webhook.InMemoryWebhookEventStore;
import com.softgapx.appypay.webhook.LoggingPaymentEventListener;
import com.softgapx.appypay.webhook.PaymentEventListener;
import com.softgapx.appypay.webhook.WebhookEventStore;
import com.softgapx.appypay.webhook.WebhookProcessingService;
import com.softgapx.appypay.webhook.processor.ExpiredProcessor;
import com.softgapx.appypay.webhook.processor.FailedProcessor;
import com.softgapx.appypay.webhook.processor.PaymentStatusProcessor;
import com.softgapx.appypay.webhook.processor.PendingProcessor;
import com.softgapx.appypay.webhook.processor.SuccessProcessor;

/**
 * Ponto de entrada único do SDK da AppyPay — é a raiz de composição que
 * substitui o container de Injeção de Dependências do Spring: constrói e
 * liga manualmente todas as classes internas (cliente HTTP, mappers,
 * handlers, casos de uso) a partir de um {@link AppyPayConfig}.
 * <p>
 * É Java puro: pode ser usado em qualquer aplicação (Spring, Quarkus,
 * Jakarta EE, ou uma aplicação `main()` sem framework nenhum).
 *
 * <pre>{@code
 * AppyPaySdk sdk = AppyPaySdk.builder(config)
 *         .addPaymentEventListener(myListener)
 *         .webhookEventStore(myJdbcStore)
 *         .build();
 *
 * PaymentResponse response = sdk.paymentService().createPayment(request);
 * }</pre>
 */
public final class AppyPaySdk {

    private final AppyPayConfig config;
    private final PaymentService paymentService;
    private final WebhookProcessingService webhookProcessingService;

    private AppyPaySdk(
            AppyPayConfig config,
            PaymentService paymentService,
            WebhookProcessingService webhookProcessingService) {

        this.config = config;
        this.paymentService = paymentService;
        this.webhookProcessingService = webhookProcessingService;
    }

    /** Atalho para {@code AppyPaySdk.builder(config).build()}, usando todos os valores por omissão. */
    public static AppyPaySdk create(AppyPayConfig config) {
        return builder(config).build();
    }

    public static Builder builder(AppyPayConfig config) {
        return new Builder(config);
    }

    public AppyPayConfig config() {
        return config;
    }

    /** Fachada para criar, consultar e simular pagamentos. */
    public PaymentService paymentService() {
        return paymentService;
    }

    /** Serviço para processar as notificações de webhook recebidas da AppyPay. */
    public WebhookProcessingService webhookService() {
        return webhookProcessingService;
    }

    public static final class Builder {

        private final AppyPayConfig config;

        private WebhookEventStore webhookEventStore = new InMemoryWebhookEventStore();
        private final List<PaymentEventListener> listeners = new ArrayList<>();
        private final List<PaymentStatusProcessor> processors = new ArrayList<>();
        private ObjectMapper objectMapper;

        private Builder(AppyPayConfig config) {
            this.config = Objects.requireNonNull(config, "AppyPayConfig é obrigatório.");
        }

        /**
         * Substitui o armazenamento (em memória, por omissão) usado para
         * garantir a idempotência dos webhooks. Recomendado em produção
         * com múltiplas instâncias da aplicação.
         */
        public Builder webhookEventStore(WebhookEventStore webhookEventStore) {
            this.webhookEventStore = Objects.requireNonNull(webhookEventStore);
            return this;
        }

        /** Regista um listener adicional, notificado sempre que um webhook é processado com sucesso. */
        public Builder addPaymentEventListener(PaymentEventListener listener) {
            this.listeners.add(Objects.requireNonNull(listener));
            return this;
        }

        /** Fornece o seu próprio {@link ObjectMapper} Jackson, caso já tenha um configurado na aplicação. */
        public Builder objectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            return this;
        }

        public AppyPaySdk build() {

            ObjectMapper mapper = this.objectMapper != null
                    ? this.objectMapper
                    : AppyPayJson.newObjectMapper();

            AppyPayHttpClient oauthHttpClient = new AppyPayHttpClient(
                    config.oauthConnectTimeout(),
                    config.oauthReadTimeout()
            );

            AppyPayHttpClient gatewayHttpClient = new AppyPayHttpClient(
                    config.gatewayConnectTimeout(),
                    config.gatewayReadTimeout()
            );

            OAuthService oAuthService = new OAuthService(oauthHttpClient, config, new OAuthMapper(), mapper);
            AccessTokenProvider tokenProvider = new CachedAccessTokenProvider(oAuthService);

            AppyPayResponseHandler responseHandler =
                    new AppyPayResponseHandler(AppyPayResponseHandler.defaultHandlers());

            AppyPayClient client = new AppyPayClient(
                    gatewayHttpClient,
                    config.gatewayUrl(),
                    tokenProvider,
                    responseHandler,
                    mapper
            );

            ChargeMapper chargeMapper = new ChargeMapper(config);
            PaymentGateway paymentGateway = new AppyPayGateway(client, chargeMapper);

            CreatePayment createPayment = new CreatePayment(paymentGateway);
            GetPayment getPayment = new GetPayment(paymentGateway);

            PaymentService paymentService =
                    new AppyPayPaymentService(createPayment, getPayment, paymentGateway);

            List<PaymentStatusProcessor> statusProcessors = new ArrayList<>(List.of(
                    new SuccessProcessor(),
                    new FailedProcessor(),
                    new PendingProcessor(),
                    new ExpiredProcessor()
            ));
            statusProcessors.addAll(this.processors);

            List<PaymentEventListener> eventListeners = new ArrayList<>();
            eventListeners.add(new LoggingPaymentEventListener());
            eventListeners.addAll(this.listeners);

            WebhookProcessingService webhookProcessingService = new WebhookProcessingService(
                    paymentGateway,
                    webhookEventStore,
                    statusProcessors,
                    eventListeners,
                    new PaymentEventMapper()
            );

            return new AppyPaySdk(config, paymentService, webhookProcessingService);
        }
    }
}
