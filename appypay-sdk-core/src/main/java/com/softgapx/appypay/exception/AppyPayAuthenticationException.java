package com.softgapx.appypay.exception;

/** Falha ao autenticar (OAuth2) junto da AppyPay. */
@SuppressWarnings("serial")
public class AppyPayAuthenticationException extends AppyPayException {

    public AppyPayAuthenticationException(String message) {
        super(message);
    }

    public AppyPayAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
