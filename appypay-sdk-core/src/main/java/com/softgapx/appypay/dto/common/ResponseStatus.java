package com.softgapx.appypay.dto.common;

public record ResponseStatus(

        boolean successful,

        String status,

        Integer code,

        String message,

        String source,

        SourceDetails sourceDetails

) {
}
