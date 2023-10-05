package com.javafest.ServiceLagbe.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        var authorities = authentication.getAuthorities();
        var roles = authorities.stream().map(r -> r.getAuthority()).findFirst();
        System.out.println("HELLO");

        if (roles.orElse("").equals("WORKER")) {
            response.sendRedirect("/worker/dashboard");
        } else if (roles.orElse("").equals("GENERAL")) {
            response.sendRedirect("/general/dashboard");
        } else {
            response.sendRedirect("/error");
        }
    }
}
