package com.softgapx.appypay.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Reference(

        String referenceNumber,
        String dueDate,
        String entity

) {
}
