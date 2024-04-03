package com.liga.semin.server.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExceptionResponse(
        @JsonProperty("error_message")
        String errorMessage
) {
}

