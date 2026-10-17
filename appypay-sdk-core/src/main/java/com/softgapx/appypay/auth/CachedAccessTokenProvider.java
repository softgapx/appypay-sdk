package com.softgapx.appypay.auth;

import java.time.Instant;

/**
 * Implementação de {@link AccessTokenProvider} que mantém o token em cache
 * em memória e só volta a autenticar-se junto da AppyPay quando o token
 * expira. Thread-safe (sincronizado no refresh).
 * <p>
 * Não é um {@code @Component}/{@code @Service} — é uma classe Java simples,
 * instanciada explicitamente pelo {@code AppyPaySdk} no arranque.
 */
public final class CachedAccessTokenProvider implements AccessTokenProvider {

    private final OAuthService oauthService;

    private volatile String token;
    private volatile Instant expiresAt;

    public CachedAccessTokenProvider(OAuthService oauthService) {
        this.oauthService = oauthService;
    }

    @Override
    public synchronized String getAccessToken() {

        if (token == null || expired()) {
            refresh();
        }

        return token;
    }

    private boolean expired() {
        return expiresAt == null || Instant.now().isAfter(expiresAt);
    }

    private void refresh() {

        AccessToken accessToken = oauthService.authenticate();

        this.token = accessToken.value();
        this.expiresAt = accessToken.expiresAt();
    }
}
