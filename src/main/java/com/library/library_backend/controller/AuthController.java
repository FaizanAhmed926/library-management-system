package com.library.library_backend.controller;

import com.library.library_backend.exception.UserException;
import com.library.library_backend.payload.dto.UserDTO;
import com.library.library_backend.payload.request.ForgetPasswordRequest;
import com.library.library_backend.payload.request.LoginRequest;
import com.library.library_backend.payload.request.ResetPasswordRequest;
import com.library.library_backend.payload.response.ApiResponse;
import com.library.library_backend.payload.response.AuthResponse;
import com.library.library_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signupHandler(@RequestBody @Valid UserDTO req) throws UserException {
        AuthResponse res=authService.signup(req);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody @Valid LoginRequest req) throws UserException {
        AuthResponse res=authService.login(req.getEmail(), req.getPassword());
        System.out.println(res);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/forget-password")
    public  ResponseEntity<ApiResponse> forgetPassword(@RequestBody ForgetPasswordRequest request) throws UserException {
        authService.createPasswordResetToken(request.getEmail());
        ApiResponse res=new ApiResponse("A Reset link was send to your Email.",true);
        return  ResponseEntity.ok(res);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordRequest request) throws Exception {
        authService.resetPassword(request.getToken(),request.getPassword());
        ApiResponse res=new ApiResponse("Password reset successfull",true);
        return ResponseEntity.ok(res);
    }
}
