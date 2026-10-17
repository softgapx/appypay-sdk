package com.softgapx.appypay.dto.common;

public record SourceDetails(

        Integer attempt,

        String type,

        String code,

        String message

) {
}
