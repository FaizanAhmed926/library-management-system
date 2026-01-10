package com.library.library_backend.service.impl;

import com.library.library_backend.mapper.UserMapper;
import com.library.library_backend.model.User;
import com.library.library_backend.payload.dto.UserDTO;
import com.library.library_backend.repository.UserRepository;
import com.library.library_backend.service.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private  final UserRepository userRepository;

    @Override
    public User getCurrentUser() throws Exception {
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByEmail(email);
        if (user==null){
            throw new Exception("User not found!");
        }
        return user;
    }

    @Override
    public List<UserDTO> getAllUser() {
        List<User> users=userRepository.findAll();
        return  users.stream().map(
                UserMapper::toDTO
        ).collect(Collectors.toList());
    }
}
