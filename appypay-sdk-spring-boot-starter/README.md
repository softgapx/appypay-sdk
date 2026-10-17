# AppyPay SDK — Spring Boot Starter

Auto-configuração Spring Boot para o
[`appypay-sdk-core`](https://github.com/softgapx/appypay-sdk), o cliente
Java para a API de Pagamentos da [AppyPay](https://appypay.co.ao).

Adiciona esta dependência a uma aplicação Spring Boot e um `PaymentService`
já configurado fica disponível para `@Autowired` — sem escrever nenhuma
classe `@Configuration`.

## Requisitos

- Java 21 ou superior
- Spring Boot 3.x

## Instalação

**Maven**

```xml
<dependency>
    <groupId>com.softgapx</groupId>
    <artifactId>appypay-sdk-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Gradle**

```groovy
implementation 'com.softgapx:appypay-sdk-spring-boot-starter:1.0.0'
```

## Configuração

```yaml
appypay:
  environment: sandbox        # ou "production"
  client-id: ${APPYPAY_CLIENT_ID}
  client-secret: ${APPYPAY_CLIENT_SECRET}
  payment-method-gpo: ${APPYPAY_PAYMENT_METHOD_GPO}
  payment-method-ref: ${APPYPAY_PAYMENT_METHOD_REF}
```

As credenciais são obtidas no portal da AppyPay — Sandbox
(`app-tst.appypay.co.ao`) ou Produção (`app.appypay.co.ao`), conforme o
ambiente.

## Início rápido

```java
@Service
public class PedidoService {

    private final PaymentService paymentService;

    public PedidoService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public PaymentResponse pagar(Pedido pedido) {
        return paymentService.createPayment(new PaymentRequest(
                pedido.getId(),
                pedido.getValor(),
                pedido.getDescricao(),
                PaymentMethod.GPO,
                pedido.getTelefoneCliente(),
                null,
                null
        ));
    }
}
```

Não é preciso mais nenhuma configuração — o starter já construiu o
`AppyPayConfig`, o `AppyPaySdk`, e expôs o `PaymentService` como bean.

Para os detalhes de uso da API (criar pagamentos GPO/REF, consultar
estado, simular pagamentos no Sandbox, tratamento de erros, processar
webhooks), consulta o README do
[`appypay-sdk-core`](https://github.com/softgapx/appypay-sdk) — o
starter expõe exatamente a mesma API, apenas já configurada.

## Reagir a eventos de pagamento

Regista quantos listeners precisares como beans — todos são recolhidos
automaticamente:

```java
@Bean
public PaymentEventListener meuListener() {
    return event -> {
        // atualizar o pedido, notificar o cliente, etc.
    };
}
```

## Processar webhooks

Cria o teu próprio endpoint e delega ao `WebhookProcessingService`:

```java
@RestController
public class WebhookController {

    private final WebhookProcessingService webhookService;

    public WebhookController(WebhookProcessingService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping("/webhooks/appypay")
    public void receber(@RequestBody WebhookRequest webhook) {
        webhookService.process(webhook);
    }
}
```

## Idempotência de webhooks com base de dados

Por omissão, a idempotência dos webhooks é garantida em memória — não
recomendado para produção com múltiplas instâncias. Define o teu próprio
`WebhookEventStore` como bean e o starter usa-o automaticamente:

```java
@Bean
public WebhookEventStore webhookEventStore(JdbcTemplate jdbc) {
    return new MinhaImplementacaoJdbc(jdbc);
}
```

## Desligar a auto-configuração

Se preferires configurar os beans manualmente (por exemplo, para usar
múltiplas instâncias de `AppyPaySdk` com credenciais diferentes):

```yaml
appypay:
  enabled: false
```

## Propriedades disponíveis

| Propriedade | Obrigatória | Omissão |
|---|---|---|
| `appypay.enabled` | Não | `true` |
| `appypay.environment` | Não | `sandbox` |
| `appypay.client-id` | Sim | — |
| `appypay.client-secret` | Sim | — |
| `appypay.payment-method-gpo` | Sim | — |
| `appypay.payment-method-ref` | Sim | — |
| `appypay.oauth-connect-timeout-ms` | Não | valor por omissão do SDK |
| `appypay.oauth-read-timeout-ms` | Não | valor por omissão do SDK |
| `appypay.gateway-connect-timeout-ms` | Não | valor por omissão do SDK |
| `appypay.gateway-read-timeout-ms` | Não | valor por omissão do SDK |

## Beans expostos

Todos podem ser substituídos definindo o teu próprio bean do mesmo tipo
(o starter recua automaticamente):

- `AppyPayConfig`
- `AppyPaySdk`
- `PaymentService`
- `WebhookProcessingService`

## Licença

Distribuído sob a licença Apache 2.0. Ver [LICENSE](LICENSE).
