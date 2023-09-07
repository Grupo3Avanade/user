package com.avanade.user.payloads.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public record ResponseStandardError(Integer status, String message, String timestamp) {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String toString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "StandardErrorDTO{" +
                    "status=" + status +
                    ", message='" + message + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    '}';
        }
    }
}
