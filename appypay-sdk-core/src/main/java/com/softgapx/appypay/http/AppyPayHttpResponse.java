package com.softgapx.appypay.http;

/** Resposta HTTP crua (status + corpo) devolvida pelo {@link AppyPayHttpClient}. */
public record AppyPayHttpResponse(

        int statusCode,

        String body

) {

    public boolean isSuccessful() {
        return statusCode >= 200 && statusCode < 300;
    }

    public boolean isClientError() {
        return statusCode >= 400 && statusCode < 500;
    }
}
