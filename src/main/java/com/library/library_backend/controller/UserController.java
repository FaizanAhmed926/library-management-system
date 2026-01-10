package com.library.library_backend.controller;

import com.library.library_backend.model.User;
import com.library.library_backend.payload.dto.UserDTO;
import com.library.library_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return  ResponseEntity.ok(
          userService.getAllUser()
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile() throws Exception {
        return ResponseEntity.ok(
                userService.getCurrentUser()
        );
    }
}
