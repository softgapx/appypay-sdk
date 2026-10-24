# Changelog

Todas as alterações relevantes deste projeto são documentadas aqui.
O formato segue [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/),
e o versionamento segue [Semantic Versioning](https://semver.org/lang/pt-BR/).

## [1.0.0] - 2026-10-24

### Added
- Primeira versão pública. Auto-configuração Spring Boot
  (`AppyPayAutoConfiguration`) para o `appypay-sdk-core` 1.0.0.
- Beans automáticos: `AppyPayConfig`, `AppyPaySdk`, `PaymentService`,
  `WebhookProcessingService` — todos `@ConditionalOnMissingBean`.
- Propriedades `appypay.*` via `AppyPayProperties`, com suporte a
  `environment` (`sandbox`/`production`), credenciais, métodos de
  pagamento e timeouts opcionais.
- Recolha automática de `WebhookEventStore` e `PaymentEventListener`
  definidos como beans pela aplicação consumidora.
- Desligável por completo via `appypay.enabled=false`.
