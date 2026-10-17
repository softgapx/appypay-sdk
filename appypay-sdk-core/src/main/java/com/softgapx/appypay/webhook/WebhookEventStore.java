package com.softgapx.appypay.webhook;

import java.time.Instant;

/**
 * Abstração de persistência para garantir a idempotência dos webhooks
 * (a mesma transação/status não deve ser processada duas vezes).
 * <p>
 * O SDK <b>não</b> impõe nenhuma tecnologia de base de dados — a
 * aplicação consumidora implementa esta interface com o que já usa
 * (JPA, MongoDB, Redis, uma tabela SQL simples, etc.) e regista a
 * implementação em {@code AppyPaySdk.builder(config).webhookEventStore(...)}.
 * <p>
 * Por omissão, o SDK usa {@link InMemoryWebhookEventStore}, que funciona
 * bem para desenvolvimento local e testes, mas não é adequado para
 * produção com múltiplas instâncias da aplicação (não é partilhado nem
 * persistente entre reinícios).
 */
public interface WebhookEventStore {

    /** Indica se este evento (transactionId + status) já foi processado. */
    boolean exists(String transactionId, String status);

    /**
     * Regista o evento como processado.
     * <p>
     * Implementações que persistam em base de dados devem garantir
     * unicidade (ex: constraint UNIQUE em transactionId+status) para
     * proteger contra escritas concorrentes duplicadas.
     */
    void save(String transactionId, String status, Instant processedAt);
}
