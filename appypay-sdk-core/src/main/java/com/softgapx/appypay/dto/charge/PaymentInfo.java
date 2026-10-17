package com.softgapx.appypay.dto.charge;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record PaymentInfo(

        String phoneNumber,

        String referenceNumber,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime dueDate

) {
}
