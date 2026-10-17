package com.softgapx.appypay.auth;

import java.time.Instant;

public record AccessToken(

        String value,

        Instant expiresAt

) {
}
