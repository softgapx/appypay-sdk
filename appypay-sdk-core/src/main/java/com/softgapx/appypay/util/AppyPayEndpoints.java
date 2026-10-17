package com.softgapx.appypay.util;

public final class AppyPayEndpoints {

    private AppyPayEndpoints() {
    }

    /* Gateway - Charges */

    public static final String CHARGES = "/charges";

    public static final String CHARGE_BY_ID = "/charges/%s";

    /* Sandbox - Reference Payment Simulation */

    public static final String REFERENCE_PROCESSING = "/mocks/referenceProcessing";
}
