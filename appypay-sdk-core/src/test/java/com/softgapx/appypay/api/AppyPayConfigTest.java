package com.softgapx.appypay.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;

import org.junit.jupiter.api.Test;

class AppyPayConfigTest {

    @Test
    void shouldBuildConfigurationWithExplicitValues() {

        AppyPayConfig config = AppyPayConfig.builder()
                .clientId("client-id")
                .clientSecret("client-secret")
                .resource("resource-id")
                .authUrl("https://auth.example.com")
                .gatewayUrl("https://gateway.example.com")
                .paymentMethodGpo("GPO")
                .paymentMethodRef("REF")
                .build();

        assertEquals("client-id", config.clientId());
        assertEquals("client-secret", config.clientSecret());
        assertEquals("resource-id", config.resource());
        assertEquals("https://auth.example.com", config.authUrl());
        assertEquals("https://gateway.example.com", config.gatewayUrl());
        assertEquals("GPO", config.paymentMethodGpo());
        assertEquals("REF", config.paymentMethodRef());
        assertEquals(Duration.ofMillis(5000), config.oauthConnectTimeout());
    }

    @Test
    void shouldPrefillSandboxUrlsAndResource() {

        AppyPayConfig config = AppyPayConfig.builder()
                .sandbox()
                .clientId("id")
                .clientSecret("secret")
                .paymentMethodGpo("GPO_x")
                .paymentMethodRef("REF_x")
                .build();

        assertEquals(
                "https://login.microsoftonline.com/appypaydev.onmicrosoft.com/oauth2/token",
                config.authUrl()
        );
        assertEquals("https://gwy-api-tst.appypay.co.ao/v2.0", config.gatewayUrl());
        assertEquals("2aed7612-de64-46b5-9e59-1f48f8902d14", config.resource());
    }

    @Test
    void shouldPrefillProductionUrlsAndResource() {

        AppyPayConfig config = AppyPayConfig.builder()
                .production()
                .clientId("id")
                .clientSecret("secret")
                .paymentMethodGpo("GPO_x")
                .paymentMethodRef("REF_x")
                .build();

        assertEquals("https://gwy-api.appypay.co.ao/v2.0", config.gatewayUrl());
        assertEquals("bee57785-7a19-4f1c-9c8d-aa03f2f0e333", config.resource());
    }

    @Test
    void shouldRejectMissingClientId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> AppyPayConfig.builder()
                        .sandbox()
                        .clientSecret("secret")
                        .paymentMethodGpo("GPO")
                        .paymentMethodRef("REF")
                        .build()
        );
    }

    @Test
    void shouldAllowOverridingTimeouts() {

        AppyPayConfig config = AppyPayConfig.builder()
                .sandbox()
                .clientId("id")
                .clientSecret("secret")
                .paymentMethodGpo("GPO")
                .paymentMethodRef("REF")
                .gatewayReadTimeout(Duration.ofSeconds(30))
                .build();

        assertEquals(Duration.ofSeconds(30), config.gatewayReadTimeout());
    }
}
