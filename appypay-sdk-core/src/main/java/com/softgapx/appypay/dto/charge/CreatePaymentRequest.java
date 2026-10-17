package com.softgapx.appypay.dto.charge;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.softgapx.appypay.api.PaymentMethod;

/**
 * Pedido de pagamento na API pública do SDK.
 * <p>
 * A validação de regras de negócio (formato do orderId, obrigatoriedade
 * de phoneNumber para GPO, etc.) é feita por {@link PaymentRequestValidator},
 * chamada automaticamente pelo caso de uso {@code CreatePayment}. Não há
 * dependência de nenhum framework de validação (Bean Validation/Spring);
 * a validação é código Java simples, sem reflexão nem anotações mágicas.
 */
public record CreatePaymentRequest(

        String orderId,

        BigDecimal amount,

        String description,

        PaymentMethod paymentMethod,

        String phoneNumber,

        String referenceNumber,

        LocalDateTime dueDate

) {
}
