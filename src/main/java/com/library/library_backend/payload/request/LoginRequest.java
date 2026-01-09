package com.library.library_backend.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotNull(message = "User name or email is required")
    private String email;

    @NotNull(message = "Password is required")
    private  String password;
}
