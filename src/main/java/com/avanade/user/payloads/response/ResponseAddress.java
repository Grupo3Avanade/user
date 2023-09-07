package com.avanade.user.payloads.response;

import java.util.UUID;

public record ResponseAddress(
        UUID id,
        String postalCode,
        String street,
        String neighborhood,
        String city,
        String state,
        String additionalInfo,
        String number) {
}
