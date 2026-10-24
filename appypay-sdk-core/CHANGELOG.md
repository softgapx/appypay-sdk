# Changelog

Todas as alterações relevantes deste projeto são documentadas aqui.
O formato segue [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/),
e o versionamento segue [Semantic Versioning](https://semver.org/lang/pt-BR/).

## [1.0.0] - 2026-10-24

### Added
- Primeira versão pública do SDK: criação e consulta de pagamentos
  GPO (Multicaixa Express) e REF (Referência Bancária) via AppyPay.
- `AppyPayConfig` com `Builder` e presets `.sandbox()`/`.production()`.
- `AppyPaySdk` como raiz de composição, com `Builder` para registar
  `PaymentEventListener` e `WebhookEventStore` personalizados.
- Simulação de pagamento por referência no ambiente Sandbox
  (`simulateReferencePayment`).
- Processamento de webhooks com garantia de idempotência via
  `WebhookEventStore`, com `InMemoryWebhookEventStore` como omissão.
- Tratamento de erros do gateway via hierarquia de exceções
  (`PaymentDeclinedException`, `PaymentExpiredException`,
  `PaymentPendingException`, `InvalidPaymentRequestException`,
  `PaymentGatewayUnavailableException`, `AppyPayAuthenticationException`).
- Zero dependências de framework — Java 21 puro, apenas
  `jackson-databind`/`jackson-datatype-jsr310` e `slf4j-api`.
