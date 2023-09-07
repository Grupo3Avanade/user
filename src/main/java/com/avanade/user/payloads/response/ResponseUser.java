package com.avanade.user.payloads.response;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public record ResponseUser(
        UUID id,
        String name,
        String email,
        LocalDate birthday,
        ResponseAddress address,
        Date createdAt,
        Date updatedAt) {
}
