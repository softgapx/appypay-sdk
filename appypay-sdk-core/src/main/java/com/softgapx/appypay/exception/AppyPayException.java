package com.softgapx.appypay.exception;

/** Exceção base de todas as exceções lançadas pelo SDK da AppyPay. */
@SuppressWarnings("serial")
public class AppyPayException extends RuntimeException {

    public AppyPayException(String message) {
        super(message);
    }

    public AppyPayException(String message, Throwable cause) {
        super(message, cause);
    }
}
