package com.softgapx.appypay.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CachedAccessTokenProviderTest {

    private OAuthService oauthService;
    private CachedAccessTokenProvider provider;

    @BeforeEach
    void setUp() {

        oauthService = mock(OAuthService.class);
        provider = new CachedAccessTokenProvider(oauthService);
    }

    @Test
    void shouldAuthenticateAndReturnTokenWhenNoTokenIsCached() {

        AccessToken accessToken = new AccessToken("token-123", Instant.now().plusSeconds(300));

        when(oauthService.authenticate()).thenReturn(accessToken);

        String result = provider.getAccessToken();

        assertEquals("token-123", result);
        verify(oauthService).authenticate();
    }

    @Test
    void shouldReturnCachedTokenWithoutAuthenticatingAgain() {

        AccessToken accessToken = new AccessToken("token-123", Instant.now().plusSeconds(300));

        when(oauthService.authenticate()).thenReturn(accessToken);

        String firstResult = provider.getAccessToken();
        String secondResult = provider.getAccessToken();

        assertEquals("token-123", firstResult);
        assertSame(firstResult, secondResult);

        verify(oauthService, times(1)).authenticate();
    }

    @Test
    void shouldRefreshTokenWhenCachedTokenIsExpired() {

        AccessToken expiredToken = new AccessToken("expired-token", Instant.now().minusSeconds(60));
        AccessToken newToken = new AccessToken("new-token", Instant.now().plusSeconds(300));

        when(oauthService.authenticate())
                .thenReturn(expiredToken)
                .thenReturn(newToken);

        String firstResult = provider.getAccessToken();
        String secondResult = provider.getAccessToken();

        assertEquals("expired-token", firstResult);
        assertEquals("new-token", secondResult);

        verify(oauthService, times(2)).authenticate();
    }
}
