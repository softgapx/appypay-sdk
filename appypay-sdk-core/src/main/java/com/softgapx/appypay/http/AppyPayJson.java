package com.softgapx.appypay.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Fábrica do {@link ObjectMapper} usado internamente pelo SDK para
 * serializar/desserializar os payloads da API da AppyPay.
 * <p>
 * Centralizado aqui para que toda a biblioteca use exatamente a mesma
 * configuração (suporte a LocalDateTime, tolerância a campos desconhecidos).
 */
public final class AppyPayJson {

    private AppyPayJson() {
    }

    public static ObjectMapper newObjectMapper() {

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return mapper;
    }
}
