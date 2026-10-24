# AppyPay SDK

SDK não-oficial em Java para a API de Pagamentos da [AppyPay](https://appypay.co.ao)
(Angola), com suporte a **Multicaixa Express (GPO)** e **Referência Bancária
(REF)**.

Este repositório é um monorepo com dois módulos, publicados separadamente
no Maven Central:

| Módulo | Para quem | Maven Central |
|---|---|---|
| [`appypay-sdk-core`](./appypay-sdk-core) | Qualquer aplicação Java 21+ (Spring, Quarkus, Micronaut, Java puro) | `com.softgapx:appypay-sdk-core:1.0.0` |
| [`appypay-sdk-spring-boot-starter`](./appypay-sdk-spring-boot-starter) | Aplicações Spring Boot 3.x — auto-configuração, zero `@Configuration` manual | `com.softgapx:appypay-sdk-spring-boot-starter:1.0.0` |

## Por onde começar

- **Usas Spring Boot?** Instala o `appypay-sdk-spring-boot-starter` e segue o
  [README do starter](./appypay-sdk-spring-boot-starter/README.md) —
  basta configurar o `application.yml` e injetar o `PaymentService`.
- **Qualquer outro caso** (Java puro, Quarkus, Micronaut, ou Spring com
  configuração manual)? Instala o `appypay-sdk-core` diretamente e segue o
  [README do core](./appypay-sdk-core/README.md).

## Funcionalidades

- Criação de pagamentos GPO (Multicaixa Express) e REF (Referência Bancária)
- Consulta do estado de um pagamento
- Simulação de pagamentos no ambiente Sandbox
- Processamento de notificações de webhook, com garantia de idempotência
- Gestão automática do token de acesso OAuth2 (obtenção e renovação)

## Instalação

```xml
<!-- Java puro, Quarkus, Micronaut, etc. -->
<dependency>
    <groupId>com.softgapx</groupId>
    <artifactId>appypay-sdk-core</artifactId>
    <version>1.0.0</version>
</dependency>
```

```xml
<!-- Spring Boot -->
<dependency>
    <groupId>com.softgapx</groupId>
    <artifactId>appypay-sdk-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Este SDK é oficial?

Não. É um SDK de terceiros, desenvolvido pela [SoftGapX](https://softgapx.com),
sem afiliação com a AppyPay.

## Licença

Distribuído sob a licença Apache 2.0. Ver [LICENSE](LICENSE).
