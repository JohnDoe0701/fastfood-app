package com.htc.fastfoodapp.fastfood.service;

import com.htc.fastfoodapp.fastfood.dto.AuthResponse;
import com.htc.fastfoodapp.fastfood.dto.LoginRequest;
import com.htc.fastfoodapp.fastfood.dto.RegisterRequest;
import com.htc.fastfoodapp.fastfood.entity.Authority;
import com.htc.fastfoodapp.fastfood.entity.UserAccount;
import com.htc.fastfoodapp.fastfood.entity.UserType;
import com.htc.fastfoodapp.fastfood.repository.AuthorityRepository;
import com.htc.fastfoodapp.fastfood.repository.UserAccountRepository;
import com.htc.fastfoodapp.fastfood.security.JwtTokenProvider;
import com.htc.fastfoodapp.fastfood.service.mapper.UserAccountMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserAccountMapper userAccountMapper;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private UserAccount userAccount;
    private Authority authority;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequest.builder()
                .fullName("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .password("password123")
                .confirmPassword("password123")
                .userType(UserType.CUSTOMER)
                .build();

        loginRequest = LoginRequest.builder()
                .email("john@example.com")
                .password("password123")
                .build();

        authority = Authority.builder()
                .id(1L)
                .authority("ROLE_CUSTOMER")
                .build();

        userAccount = UserAccount.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .password("encodedPassword")
                .userType(UserType.CUSTOMER)
                .authorities(Set.of(authority))
                .enabled(true)
                .build();
    }

    @Test
    void testRegisterSuccess() {
        when(userAccountRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(authorityRepository.findByAuthority(anyString())).thenReturn(Optional.of(authority));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(userAccount);
        when(jwtTokenProvider.generateToken(any(UserAccount.class))).thenReturn("jwt-token");
        when(userAccountMapper.toUserResponse(any(UserAccount.class))).thenReturn(
                new com.htc.fastfoodapp.fastfood.dto.UserResponse(
                        1L, "John Doe", "john@example.com", "1234567890",
                        UserType.CUSTOMER, true, Set.of("ROLE_CUSTOMER")
                )
        );

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
        assertNotNull(response.getUser());
        assertEquals("john@example.com", response.getUser().getEmail());

        verify(userAccountRepository).save(any(UserAccount.class));
        verify(jwtTokenProvider).generateToken(any(UserAccount.class));
    }

    @Test
    void testRegisterPasswordMismatch() {
        registerRequest.setConfirmPassword("differentPassword");

        assertThrows(IllegalArgumentException.class, () -> authService.register(registerRequest));
        verify(userAccountRepository, never()).save(any(UserAccount.class));
    }

    @Test
    void testRegisterEmailAlreadyExists() {
        when(userAccountRepository.findByEmail(anyString())).thenReturn(Optional.of(userAccount));

        assertThrows(IllegalArgumentException.class, () -> authService.register(registerRequest));
        verify(userAccountRepository, never()).save(any(UserAccount.class));
    }

    @Test
    void testLoginSuccess() {
        UsernamePasswordAuthenticationToken auth = mock(UsernamePasswordAuthenticationToken.class);
        when(auth.getPrincipal()).thenReturn(userAccount);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(jwtTokenProvider.generateToken(any(UserAccount.class))).thenReturn("jwt-token");
        when(userAccountMapper.toUserResponse(any(UserAccount.class))).thenReturn(
                new com.htc.fastfoodapp.fastfood.dto.UserResponse(
                        1L, "John Doe", "john@example.com", "1234567890",
                        UserType.CUSTOMER, true, Set.of("ROLE_CUSTOMER")
                )
        );

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
        assertNotNull(response.getUser());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateToken(any(UserAccount.class));
    }

    @Test
    void testLoginInvalidCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));

        assertThrows(IllegalArgumentException.class, () -> authService.login(loginRequest));
        verify(jwtTokenProvider, never()).generateToken(any(UserAccount.class));
    }
}
