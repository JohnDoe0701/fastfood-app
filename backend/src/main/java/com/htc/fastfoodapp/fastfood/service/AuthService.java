package com.htc.fastfoodapp.fastfood.service;

import com.htc.fastfoodapp.fastfood.dto.AuthResponse;
import com.htc.fastfoodapp.fastfood.dto.LoginRequest;
import com.htc.fastfoodapp.fastfood.dto.RegisterRequest;
import com.htc.fastfoodapp.fastfood.dto.UserResponse;
import com.htc.fastfoodapp.fastfood.entity.Authority;
import com.htc.fastfoodapp.fastfood.entity.UserAccount;
import com.htc.fastfoodapp.fastfood.entity.UserType;
import com.htc.fastfoodapp.fastfood.repository.AuthorityRepository;
import com.htc.fastfoodapp.fastfood.repository.UserAccountRepository;
import com.htc.fastfoodapp.fastfood.security.JwtTokenProvider;
import com.htc.fastfoodapp.fastfood.service.mapper.UserAccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserAccountMapper userAccountMapper;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Validate passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Check if email already exists
        if (userAccountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Create new user
        Set<Authority> authorities = new HashSet<>();
        Authority userAuthority = authorityRepository.findByAuthority("ROLE_" + request.getUserType())
                .orElseGet(() -> {
                    Authority newAuthority = Authority.builder()
                            .authority("ROLE_" + request.getUserType())
                            .build();
                    return authorityRepository.save(newAuthority);
                });
        authorities.add(userAuthority);

        UserAccount userAccount = UserAccount.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .userType(request.getUserType())
                .authorities(authorities)
                .enabled(true)
                .build();

        UserAccount savedUser = userAccountRepository.save(userAccount);
        log.info("User registered successfully: {}", savedUser.getEmail());

        String token = jwtTokenProvider.generateToken(savedUser);
        UserResponse userResponse = userAccountMapper.toUserResponse(savedUser);

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .user(userResponse)
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserAccount userAccount = (UserAccount) authentication.getPrincipal();
            String token = jwtTokenProvider.generateToken(userAccount);
            UserResponse userResponse = userAccountMapper.toUserResponse(userAccount);

            log.info("User logged in successfully: {}", userAccount.getEmail());

            return AuthResponse.builder()
                    .accessToken(token)
                    .tokenType("Bearer")
                    .user(userResponse)
                    .build();
        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid email or password");
        }
    }
}
