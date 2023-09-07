package com.avanade.user.payloads.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RequestUser(

        @NotNull
        String name,

        @Size(max = 100)
        @NotNull
        @NotEmpty
        @Email
        String email,

        @NotNull
        LocalDate birthday,

        @Valid
        @NotNull
        RequestAddress address
) {
}
