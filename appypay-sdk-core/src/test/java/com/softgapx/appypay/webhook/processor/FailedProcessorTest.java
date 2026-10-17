package com.softgapx.appypay.webhook.processor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FailedProcessorTest {

    private final FailedProcessor processor = new FailedProcessor();

    @Test
    void shouldSupportFailedStatusOnly() {
        assertTrue(processor.supports("Failed"));
        assertFalse(processor.supports("Success"));
    }
}
