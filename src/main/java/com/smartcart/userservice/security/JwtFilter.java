package com.smartcart.userservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter{
    private JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException,
            IOException {
        // 1️⃣ Read Authorization header
        String header=request.getHeader("Authoriztion");

        // 2️⃣ If header missing or malformed → reject
        if(header==null || !header.startsWith("Bearer ")){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        // 3️⃣ Extract token
        String token=header.substring(7);

        try{
            // 4️⃣ VALIDATION happens here
            jwtService.validateToken(token);

            // 5️⃣ If no exception → token is valid
            // Move to controller
            filterChain.doFilter(request,response);
        } catch (Exception e) {
            // 6️⃣ Invalid or expired token
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
