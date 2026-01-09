package com.library.library_backend.payload.dto;

import com.library.library_backend.domain.AuthProvider;
import com.library.library_backend.domain.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Password is required")
    private  String password;
    private  String phone;

    @NotNull(message = "FullName is required")
    private String fullName;
    private UserRole role;
    private  String username;
    private LocalDateTime lastLogin;
}
