package com.softgapx.appypay.webhook.processor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SuccessProcessorTest {

    private final SuccessProcessor processor = new SuccessProcessor();

    @Test
    void shouldSupportSuccessStatusCaseInsensitive() {
        assertTrue(processor.supports("Success"));
        assertTrue(processor.supports(" success "));
        assertFalse(processor.supports("Failed"));
        assertFalse(processor.supports(null));
    }
}
