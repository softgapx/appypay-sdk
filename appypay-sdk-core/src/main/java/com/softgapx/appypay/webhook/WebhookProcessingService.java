package com.softgapx.appypay.webhook;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softgapx.appypay.dto.charge.GetChargeResponse;
import com.softgapx.appypay.dto.charge.PaymentDetails;
import com.softgapx.appypay.dto.webhook.WebhookRequest;
import com.softgapx.appypay.gateway.PaymentGateway;
import com.softgapx.appypay.mapper.PaymentEventMapper;
import com.softgapx.appypay.webhook.processor.PaymentStatusProcessor;

/**
 * Processa as notificações de webhook recebidas da AppyPay.
 * <p>
 * A aplicação consumidora expõe o seu próprio endpoint HTTP para receber
 * o webhook (ex: {@code POST /webhooks/appypay}), desserializa o corpo
 * para {@link WebhookRequest} e chama {@link #process(WebhookRequest)}.
 * Este serviço não depende de nenhum framework web.
 */
public final class WebhookProcessingService {

    private static final Logger log = LoggerFactory.getLogger(WebhookProcessingService.class);

    private final PaymentGateway paymentGateway;
    private final WebhookEventStore store;
    private final List<PaymentStatusProcessor> processors;
    private final List<PaymentEventListener> listeners;
    private final PaymentEventMapper eventMapper;

    public WebhookProcessingService(
            PaymentGateway paymentGateway,
            WebhookEventStore store,
            List<PaymentStatusProcessor> processors,
            List<PaymentEventListener> listeners,
            PaymentEventMapper eventMapper) {

        this.paymentGateway = paymentGateway;
        this.store = store;
        this.processors = processors;
        this.listeners = listeners;
        this.eventMapper = eventMapper;
    }

    public void process(WebhookRequest webhook) {

        if (webhook == null || webhook.id() == null) {

            throw new IllegalArgumentException(
                    "Webhook inválido: payment id é obrigatório."
            );
        }

        /*
         * O Webhook da AppyPay é utilizado apenas como notificação. Para
         * garantir a consistência dos dados, consultamos novamente a
         * Charge diretamente na API.
         */
        GetChargeResponse response = paymentGateway.getCharge(webhook.id());

        if (response == null || response.payment() == null) {

            throw new IllegalStateException(
                    "A AppyPay não retornou os detalhes do pagamento."
            );
        }

        PaymentDetails payment = response.payment();

        if (payment.id() == null) {

            throw new IllegalStateException(
                    "A AppyPay não retornou o ID do pagamento."
            );
        }

        /*
         * A AppyPay pode retornar o status com espaços (ex: "Success "),
         * por isso normalizamos o valor antes de utilizá-lo.
         */
        String status = normalizeStatus(payment.status());

        if (status == null) {

            throw new IllegalStateException(
                    "A AppyPay não retornou o status do pagamento."
            );
        }

        String transactionId = payment.id();

        /*
         * Idempotência: a mesma transação pode gerar o mesmo webhook mais
         * de uma vez. transactionId + status representam um evento já
         * processado.
         */
        if (store.exists(transactionId, status)) {

            log.info(
                    "Webhook já processado para transactionId={} e status={}.",
                    transactionId,
                    status
            );

            return;
        }

        /* Transformamos o DTO específico da AppyPay num evento próprio do SDK. */
        PaymentEvent event = eventMapper.toEvent(response);

        /* Localizamos o processor responsável pelo estado. */
        PaymentStatusProcessor processor = processors.stream()
                .filter(p -> p.supports(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Status de pagamento não suportado: " + status
                ));

        processor.process(event);

        notifyListeners(event);

        /*
         * Registamos o evento como processado. Implementações de
         * WebhookEventStore apoiadas em base de dados devem tratar
         * violações de unicidade (escrita concorrente duplicada) de
         * forma silenciosa/idempotente.
         */
        store.save(transactionId, status, Instant.now());

        log.info(
                "Webhook processado com sucesso. transactionId={}, status={}",
                transactionId,
                status
        );
    }

    private String normalizeStatus(String status) {

        if (status == null) {
            return null;
        }

        String normalized = status.trim();

        return normalized.isEmpty() ? null : normalized;
    }

    private void notifyListeners(PaymentEvent event) {

        for (PaymentEventListener listener : listeners) {
            listener.onPaymentEvent(event);
        }
    }
}
