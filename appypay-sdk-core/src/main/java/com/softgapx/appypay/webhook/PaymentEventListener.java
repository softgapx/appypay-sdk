package com.softgapx.appypay.webhook;

/**
 * Implemente esta interface para reagir a eventos de pagamento processados
 * a partir dos webhooks da AppyPay (ex: atualizar o status de um pedido na
 * sua base de dados, enviar um e-mail de confirmação, etc.).
 * <p>
 * Registe a sua implementação via
 * {@code AppyPaySdk.builder(config).addPaymentEventListener(listener)}.
 */
public interface PaymentEventListener {

    void onPaymentEvent(PaymentEvent event);
}
