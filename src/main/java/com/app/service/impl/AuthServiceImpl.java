
package com.app.service.impl;

import java.security.SecureRandom;
import com.app.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.request.LoginRequestDto;
import com.app.dto.response.LoginResponseDto;
import com.app.entity.PasswordResetToken;
import com.app.entity.User;
import com.app.repository.PasswordResetTokenRepository;
import com.app.repository.UserRepository;
import com.app.service.AuthService;
import com.app.service.JWTUtil;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final EmailService emailService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    private final SecureRandom secureRandom = new SecureRandom();

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordResetTokenRepository tokenRepository,
            PasswordEncoder passwordEncoder,
            JWTUtil jwtUtil,
            EmailService emailService) {

        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        User user = userRepository
                .findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid email or password"));

        boolean passwordMatches = passwordEncoder.matches(
                loginRequestDto.getPassword(),
                user.getPassword());

        if (!passwordMatches) {
            throw new IllegalArgumentException(
                    "Invalid email or password");
        }

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build();

        String token = jwtUtil.generateToken(userDetails);

        return new LoginResponseDto(
                token,
                "Login successful",
                user.getRole().name(),
                user.getName(),
                user.getEmail()
        );
    }

    @Override
    @Transactional
    public void forgotPassword(String email) {

        userRepository.findByEmail(email).ifPresent(user -> {

            // Remove previous reset tokens for this user.
            tokenRepository.deleteByUserId(user.getId());
            tokenRepository.flush();

            // Generate a cryptographically secure random token.
            byte[] randomBytes = new byte[32];
            secureRandom.nextBytes(randomBytes);

            String token = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(randomBytes);

            PasswordResetToken resetToken =
                    new PasswordResetToken(
                            token,
                            user,
                            LocalDateTime.now().plusMinutes(15));

            tokenRepository.save(resetToken);
            String resetLink = frontendUrl
                    + "/reset-password?token="
                    + token;

            emailService.sendPasswordResetEmail(email, resetLink);
            // Email delivery will be connected in the next step.
        });
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken = tokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid or expired reset link."));

        if (resetToken.isUsed()
                || !resetToken.getExpiresAt().isAfter(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "Invalid or expired reset link.");
        }

        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }
}
