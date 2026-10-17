package com.softgapx.appypay.mapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import com.softgapx.appypay.api.AppyPayConfig;
import com.softgapx.appypay.auth.AccessToken;
import com.softgapx.appypay.dto.oauth.OAuthTokenResponse;
import com.softgapx.appypay.util.OAuthConstants;

/** Converte entre a configuração do SDK e o formato do endpoint OAuth2 (Client Credentials) da AppyPay. */
public final class OAuthMapper {

    public String toFormBody(AppyPayConfig config) {

        return OAuthConstants.CLIENT_ID + "=" + encode(config.clientId())
                + "&" + OAuthConstants.CLIENT_SECRET + "=" + encode(config.clientSecret())
                + "&" + OAuthConstants.GRANT_TYPE_PARAM + "=" + encode(OAuthConstants.GRANT_TYPE)
                + "&" + OAuthConstants.RESOURCE + "=" + encode(config.resource());
    }

    public AccessToken toAccessToken(OAuthTokenResponse response) {

        return new AccessToken(
                response.accessToken(),
                Instant.now().plusSeconds(response.expiresIn() - 60)
        );
    }

    private String encode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }
}
