package com.softgapx.appypay.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AppyPaySdkTest {

    @Test
    void shouldCreateSdkWithValidConfig() {

        AppyPayConfig config = AppyPayConfig.builder()
                .sandbox()
                .clientId("client-id")
                .clientSecret("client-secret")
                .paymentMethodGpo("GPO")
                .paymentMethodRef("REF")
                .build();

        AppyPaySdk sdk = AppyPaySdk.create(config);

        assertNotNull(sdk);
        assertEquals(config, sdk.config());
        assertNotNull(sdk.paymentService());
        assertNotNull(sdk.webhookService());
    }

    @Test
    void shouldRejectNullConfig() {

        assertThrows(
                NullPointerException.class,
                () -> AppyPaySdk.create(null)
        );
    }
}
