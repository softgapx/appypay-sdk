package com.softgapx.appypay.webhook;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementação padrão de {@link WebhookEventStore}, guardada em memória.
 * <p>
 * Adequada para desenvolvimento e testes. Para produção com múltiplas
 * instâncias, forneça a sua própria implementação (ex: apoiada em base
 * de dados) via {@code AppyPaySdk.builder(config).webhookEventStore(...)}.
 */
public final class InMemoryWebhookEventStore implements WebhookEventStore {

    private final Set<String> processedEvents = ConcurrentHashMap.newKeySet();

    @Override
    public boolean exists(String transactionId, String status) {
        return processedEvents.contains(key(transactionId, status));
    }

    @Override
    public void save(String transactionId, String status, Instant processedAt) {
        processedEvents.add(key(transactionId, status));
    }

    private String key(String transactionId, String status) {
        return transactionId + "::" + status;
    }
}
