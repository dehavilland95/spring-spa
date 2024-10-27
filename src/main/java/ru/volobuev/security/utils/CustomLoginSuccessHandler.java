package ru.volobuev.security.utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Логика обработки успешного входа
        String redirectUrl = determineTargetUrl(authentication);
        response.sendRedirect(redirectUrl); // Перенаправление на нужную страницу
    }
    private String determineTargetUrl(Authentication authentication) {
        // Ваша логика для определения целевого URL в зависимости от ролей пользователя
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            return "/admin"; // Перенаправление на страницу администратора
        } else {
            return "/user"; // Перенаправление для остальных пользователей
        }
    }
}
