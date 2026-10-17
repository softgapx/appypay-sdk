package com.softgapx.appypay.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Pedido de pagamento — ponto de entrada público do SDK (ver {@link PaymentService}).
 *
 * @param orderId         identificador único do pedido na sua aplicação (máx. 15 caracteres alfanuméricos)
 * @param amount          valor a cobrar
 * @param description     descrição curta e opcional (apenas letras, números e espaços)
 * @param paymentMethod   {@link PaymentMethod#GPO} (Multicaixa Express) ou {@link PaymentMethod#REF} (Referência)
 * @param phoneNumber     obrigatório para GPO (9 dígitos, começando por 9)
 * @param referenceNumber obrigatório para REF
 * @param dueDate         obrigatório para REF
 */
public record PaymentRequest(

        String orderId,

        BigDecimal amount,

        String description,

        PaymentMethod paymentMethod,

        String phoneNumber,

        String referenceNumber,

        LocalDateTime dueDate

) {
}
