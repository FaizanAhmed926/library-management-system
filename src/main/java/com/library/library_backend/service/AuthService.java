package com.library.library_backend.service;

import com.library.library_backend.exception.UserException;
import com.library.library_backend.payload.dto.UserDTO;
import com.library.library_backend.payload.response.AuthResponse;

public interface AuthService {
    AuthResponse login(String username , String password);
    AuthResponse signup(UserDTO req) throws UserException;

    void createPasswordResetToken(String email);
    void resetPassword(String token, String newPassword);
}
