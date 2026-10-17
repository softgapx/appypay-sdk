# AppyPay SDK — Core

Cliente Java para a API de Pagamentos da [AppyPay](https://appypay.co.ao),
suportando pagamentos via **Multicaixa Express (GPO)** e **Referência
Bancária (REF)**.

É uma biblioteca Java pura — sem Spring, sem JPA, sem nenhum framework como
dependência obrigatória. Funciona em qualquer aplicação Java 21+: Spring
Boot, Quarkus, Micronaut, Jakarta EE, ou uma aplicação `main()` sem
framework nenhum.

> Se o teu projeto é Spring Boot, considera usar o
> [`appypay-sdk-spring-boot-starter`](https://github.com/softgapx/appypay-sdk)
> em vez desta biblioteca diretamente — ele traz auto-configuração e
> injeção de dependências prontas.

## Funcionalidades

- Criação de pagamentos GPO (Multicaixa Express) e REF (Referência Bancária)
- Consulta do estado de um pagamento
- Simulação de pagamentos no ambiente Sandbox
- Processamento de notificações de webhook, com garantia de idempotência
- Gestão automática do token de acesso OAuth2 (obtenção e renovação)
- Hierarquia de exceções que traduz os códigos de erro da AppyPay em tipos
  Java específicos

## Requisitos

- Java 21 ou superior

## Instalação

**Maven**

```xml
<dependency>
    <groupId>com.softgapx</groupId>
    <artifactId>appypay-sdk-core</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Gradle**

```groovy
implementation 'com.softgapx:appypay-sdk-core:1.0.0'
```

## Início rápido

```java
AppyPayConfig config = AppyPayConfig.builder()
        .sandbox() // ou .production()
        .clientId(System.getenv("APPYPAY_CLIENT_ID"))
        .clientSecret(System.getenv("APPYPAY_CLIENT_SECRET"))
        .paymentMethodGpo(System.getenv("APPYPAY_PAYMENT_METHOD_GPO"))
        .paymentMethodRef(System.getenv("APPYPAY_PAYMENT_METHOD_REF"))
        .build();

AppyPaySdk sdk = AppyPaySdk.create(config);

PaymentResponse response = sdk.paymentService().createPayment(
        new PaymentRequest(
                "PEDIDO123",
                new BigDecimal("1000.00"),
                "Pagamento do pedido #123",
                PaymentMethod.GPO,
                "923456789",
                null,
                null
        )
);

System.out.println(response.status()); // ex: "Success"
```

As credenciais (`clientId`, `clientSecret`, `paymentMethodGpo`,
`paymentMethodRef`) são obtidas no portal da AppyPay — Sandbox
(`app-tst.appypay.co.ao`) ou Produção (`app.appypay.co.ao`), conforme o
ambiente.

## Configuração

`AppyPayConfig.builder()` pré-preenche automaticamente as URLs corretas
para cada ambiente:

```java
AppyPayConfig.builder().sandbox()     // ambiente de testes
AppyPayConfig.builder().production()  // ambiente de produção
```

Timeouts de rede têm valores por omissão sensatos, mas podem ser
ajustados:

```java
AppyPayConfig config = AppyPayConfig.builder()
        .sandbox()
        .clientId(...)
        .clientSecret(...)
        .paymentMethodGpo(...)
        .paymentMethodRef(...)
        .oauthConnectTimeout(Duration.ofSeconds(5))
        .oauthReadTimeout(Duration.ofSeconds(10))
        .gatewayConnectTimeout(Duration.ofSeconds(5))
        .gatewayReadTimeout(Duration.ofSeconds(15))
        .build();
```

## Criar um pagamento

### GPO (Multicaixa Express)

```java
PaymentResponse response = sdk.paymentService().createPayment(
        new PaymentRequest(
                "PEDIDO123",              // orderId — único, até 15 caracteres alfanuméricos
                new BigDecimal("1000.00"),// amount
                "Pagamento do pedido",    // description (opcional)
                PaymentMethod.GPO,
                "923456789",              // phoneNumber — obrigatório para GPO
                null,
                null
        )
);
```

### REF (Referência Bancária)

```java
PaymentResponse response = sdk.paymentService().createPayment(
        new PaymentRequest(
                "PEDIDOREF01",
                new BigDecimal("5000.00"),
                "Pagamento da fatura",
                PaymentMethod.REF,
                null,
                "123456789",                                 // referenceNumber
                LocalDateTime.of(2026, 12, 31, 23, 59, 59)    // dueDate
        )
);
```

A resposta inclui a entidade e o número de referência gerados pela
AppyPay, para apresentar ao cliente.

## Consultar um pagamento

```java
GetChargeResponse charge = sdk.paymentService().getPayment(response.paymentId());
```

## Simular pagamentos (apenas Sandbox)

O ambiente Sandbox não movimenta dinheiro real. Para simular que uma
referência foi paga:

```java
sdk.paymentService().simulateReferencePayment(
        new ReferencePaymentRequest(entity, referenceNumber)
);
```

Para GPO, o próprio Sandbox já simula o resultado com base no número de
telefone usado — não é preciso nenhuma chamada adicional:

| Número | Resultado simulado |
|---|---|
| `900000000` | Sucesso |
| `900000001` | Saldo insuficiente |
| `900000002` | Timeout |
| `900000003` | Rejeitado pelo cliente |

## Tratamento de erros

Todas as exceções lançadas pelo SDK estendem `AppyPayException`:

| Exceção | Quando ocorre |
|---|---|
| `InvalidPaymentRequestException` | Pedido inválido (validação local ou rejeição da AppyPay) |
| `PaymentDeclinedException` | Pagamento recusado (ex: saldo insuficiente) |
| `PaymentExpiredException` | Pagamento ou referência expirados |
| `PaymentPendingException` | Pagamento ainda pendente de confirmação |
| `AppyPayAuthenticationException` | Falha na autenticação OAuth2 (credenciais inválidas) |
| `PaymentGatewayUnavailableException` | Erro de comunicação ou resposta inesperada da AppyPay |

```java
try {
    sdk.paymentService().createPayment(request);
} catch (PaymentDeclinedException e) {
    // informar o cliente que o pagamento foi recusado
} catch (AppyPayException e) {
    // qualquer outro erro do SDK
}
```

## Processar webhooks

A AppyPay notifica a tua aplicação via webhook quando o estado de um
pagamento muda. No teu próprio endpoint HTTP (o SDK não expõe nenhum):

```java
@PostMapping("/webhooks/appypay")
public void receberWebhook(@RequestBody WebhookRequest webhook) {
    sdk.webhookService().process(webhook);
}
```

Internamente, o SDK volta a consultar a AppyPay para confirmar os
detalhes do pagamento (o webhook é tratado apenas como notificação),
garante que o mesmo evento não é processado duas vezes, e invoca os
listeners registados.

### Reagir a um evento de pagamento

```java
AppyPaySdk sdk = AppyPaySdk.builder(config)
        .addPaymentEventListener(event -> {
            // ex: atualizar o pedido na tua base de dados, notificar o cliente
        })
        .build();
```

### Idempotência dos webhooks

Por omissão, o SDK usa um armazenamento em memória
(`InMemoryWebhookEventStore`), adequado para desenvolvimento e testes,
mas que não sobrevive a reinícios nem é partilhado entre múltiplas
instâncias da aplicação. Para produção, fornece a tua própria
implementação de `WebhookEventStore`:

```java
public class JdbcWebhookEventStore implements WebhookEventStore {

    @Override
    public boolean exists(String transactionId, String status) {
        // consultar a tua base de dados
    }

    @Override
    public void save(String transactionId, String status, Instant processedAt) {
        // gravar na tua base de dados
    }
}
```

```java
AppyPaySdk sdk = AppyPaySdk.builder(config)
        .webhookEventStore(new JdbcWebhookEventStore(...))
        .build();
```

## Licença

Distribuído sob a licença Apache 2.0. Ver [LICENSE](LICENSE).
