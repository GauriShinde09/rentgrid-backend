
package com.app.service;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.app.entity.User;
import com.app.repository.UserRepository;
import com.app.service.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JWTUtil jwtUtil,
            UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }


@Override
protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException {

    System.out.println("JWT FILTER: "
            + request.getMethod() + " "
            + request.getRequestURI());

    String authHeader = request.getHeader("Authorization");

    System.out.println("Authorization header present: "
            + (authHeader != null));

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        System.out.println("No Bearer token found.");
        filterChain.doFilter(request, response);
        return;
    }

    String token = authHeader.substring(7);

    try {
        boolean validToken = jwtUtil.validateToken(token);
        System.out.println("JWT token valid: " + validToken);

        if (validToken) {
            String email = jwtUtil.extractUsername(token);
            System.out.println("Email extracted from token: " + email);

            if (SecurityContextHolder.getContext()
                    .getAuthentication() == null) {

                User appUser = userRepository.findByEmail(email)
                        .orElse(null);

                if (appUser != null) {
                    UserDetails userDetails =
                            org.springframework.security.core.userdetails.User
                                    .withUsername(appUser.getEmail())
                                    .password(appUser.getPassword())
                                    .roles(appUser.getRole().name())
                                    .build();

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);

                    System.out.println("Database role: "
                            + appUser.getRole());
                    System.out.println("Granted authorities: "
                            + userDetails.getAuthorities());
                } else {
                    System.out.println("User not found in database.");
                }
            } else {
                System.out.println(
                        "SecurityContext already has authentication.");
            }
        }
    } catch (io.jsonwebtoken.JwtException
             | IllegalArgumentException ex) {
        System.out.println("JWT validation failed: "
                + ex.getMessage());
        SecurityContextHolder.clearContext();
    }

    filterChain.doFilter(request, response);
}

}
