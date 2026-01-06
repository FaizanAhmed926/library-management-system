package com.library.library_backend.service.impl;

import com.library.library_backend.configuration.JwtProvider;
import com.library.library_backend.domain.UserRole;
import com.library.library_backend.exception.UserException;
import com.library.library_backend.model.User;
import com.library.library_backend.payload.dto.UserDTO;
import com.library.library_backend.payload.response.AuthResponse;
import com.library.library_backend.repository.UserRepository;
import com.library.library_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private final JwtProvider  jwtProvider;

    @Override
    public AuthResponse login(String username, String password) {
        return null;
    }

    @Override
    public AuthResponse signup(UserDTO req) throws UserException {
        User user=userRepository.findByEmail(req.getEmail());
        if (user!=null){
            throw new UserException("Email id Already register");
        }
        User createdUser=new User();
        createdUser.setEmail(req.getEmail());
        createdUser.setPassword(passwordEncoder.encode(req.getPassword()));
        createdUser.setPhone(req.getPhone());
        createdUser.setFullName(req.getFullName());
        createdUser.setLastLogin(LocalDateTime.now());
        createdUser.setRole(UserRole.ROLE_USER);

        User savedUser=userRepository.save(createdUser);

        Authentication auth=new UsernamePasswordAuthenticationToken(savedUser.getEmail(),savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt=jwtProvider.generateToken(auth);

        AuthResponse response=new AuthResponse();
        response.setJwt(jwt);
        response.setTitle("Welcome "+createdUser.getFullName());
        response.setMessage("register success");
        response.setUser(user);
        return null;
    }

    @Override
    public void createPasswordResetToken(String email) {

    }

    @Override
    public void resetPassword(String token, String newPassword) {

    }
}
