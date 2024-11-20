package ru.volobuev.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.volobuev.security.dto.UserDTO;
import ru.volobuev.security.service.UserService;

@RestController
@RequestMapping("/user")
public class UsersController {
    @Autowired
    private UserService userService;

    @PostMapping("/getMyInfo")
    public UserDTO getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username);
    }
}