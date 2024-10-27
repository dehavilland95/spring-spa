package ru.volobuev.security.controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    private final AuthenticationManager authenticationManager;

    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

//    @PostMapping("/login")
//    public String login(@RequestParam String username, @RequestParam String password) {
//        System.out.println("!!!@@@@@!!!!!!");
//        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
//        authenticationManager.authenticate(authentication);
//        return "Login successful!";
//    }
}
