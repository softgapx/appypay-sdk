package com.softgapx.appypay.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

import com.softgapx.appypay.exception.PaymentGatewayUnavailableException;

/**
 * Cliente HTTP interno do SDK, baseado em {@code java.net.http.HttpClient}
 * (nativo do JDK desde a 11). Não depende de nenhuma biblioteca de terceiros
 * nem de nenhum framework — é usado tanto para o endpoint de autenticação
 * OAuth2 como para o gateway de pagamentos da AppyPay.
 */
public final class AppyPayHttpClient {

    private final HttpClient httpClient;
    private final Duration requestTimeout;

    public AppyPayHttpClient(Duration connectTimeout, Duration requestTimeout) {

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .build();

        this.requestTimeout = requestTimeout;
    }

    public AppyPayHttpResponse post(String uri, Map<String, String> headers, String body) {

        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(uri))
                .timeout(requestTimeout)
                .POST(HttpRequest.BodyPublishers.ofString(body == null ? "" : body));

        return send(builder, headers);
    }

    public AppyPayHttpResponse get(String uri, Map<String, String> headers) {

        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(uri))
                .timeout(requestTimeout)
                .GET();

        return send(builder, headers);
    }

    private AppyPayHttpResponse send(HttpRequest.Builder builder, Map<String, String> headers) {

        headers.forEach(builder::header);

        try {

            HttpResponse<String> response =
                    httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());

            return new AppyPayHttpResponse(
                    response.statusCode(),
                    response.body()
            );

        } catch (IOException ex) {

            throw new PaymentGatewayUnavailableException(
                    "Erro de I/O ao comunicar com a AppyPay.", ex
            );

        } catch (InterruptedException ex) {

            Thread.currentThread().interrupt();

            throw new PaymentGatewayUnavailableException(
                    "Comunicação com a AppyPay foi interrompida.", ex
            );
        }
    }
}
