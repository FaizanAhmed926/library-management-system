package com.library.library_backend.service;

import com.library.library_backend.model.User;
import com.library.library_backend.payload.dto.UserDTO;

import java.util.List;

public interface UserService {
    public User getCurrentUser() throws Exception;
    public List<UserDTO> getAllUser();
}
