package com.avanade.user.payloads.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestAddress(

        @Size(max = 9)
        @NotNull
        String postalCode,

        @NotNull
        String street,

        @NotNull
        String neighborhood,

        @NotNull
        String city,

        @NotNull
        String state,

        String additionalInfo,

        @NotNull
        String number) {
}