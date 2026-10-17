package com.softgapx.appypay.api;

import com.softgapx.appypay.dto.charge.GetChargeResponse;
import com.softgapx.appypay.dto.reference.ReferencePaymentRequest;

/**
 * Fachada principal do SDK para operações de pagamento. Obtida através de
 * {@code AppyPaySdk.paymentService()}.
 */
public interface PaymentService {

    /** Cria uma cobrança (GPO ou REF) na AppyPay. */
    PaymentResponse createPayment(PaymentRequest request);

    /** Consulta o estado atual de uma cobrança pelo seu transactionId. */
    GetChargeResponse getPayment(String transactionId);

    /**
     * Disponível apenas no ambiente Sandbox: simula o pagamento de uma
     * referência bancária já criada, conforme a documentação
     * "Como Simular Pagamentos no Ambiente Sandbox".
     */
    void simulateReferencePayment(ReferencePaymentRequest request);
}
