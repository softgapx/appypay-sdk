package com.softgapx.appypay.dto.oauth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OAuthTokenResponse(

        @JsonProperty("token_type")
        String tokenType,

        @JsonProperty("expires_in")
        Long expiresIn,

        @JsonProperty("ext_expires_in")
        Long extExpiresIn,

        @JsonProperty("expires_on")
        String expiresOn,

        @JsonProperty("not_before")
        String notBefore,

        @JsonProperty("resource")
        String resource,

        @JsonProperty("access_token")
        String accessToken

) {
}
