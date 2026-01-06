package com.library.library_backend.payload.dto;

import com.library.library_backend.domain.AuthProvider;
import com.library.library_backend.domain.UserRole;
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
    private String email;
    private  String password;
    private  String phone;
    private String fullName;
    private UserRole role;
    private  String username;
    private LocalDateTime lastLogin;
}
