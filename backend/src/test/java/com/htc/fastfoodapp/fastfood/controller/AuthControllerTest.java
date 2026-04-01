package com.htc.fastfoodapp.fastfood.controller;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.htc.fastfoodapp.fastfood.dto.AuthResponse;
import com.htc.fastfoodapp.fastfood.dto.LoginRequest;
import com.htc.fastfoodapp.fastfood.dto.RegisterRequest;
import com.htc.fastfoodapp.fastfood.dto.UserResponse;
import com.htc.fastfoodapp.fastfood.entity.UserType;
import com.htc.fastfoodapp.fastfood.service.AuthService;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private AuthResponse authResponse;
    private UserResponse userResponse;

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

        userResponse = UserResponse.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .userType(UserType.CUSTOMER)
                .enabled(true)
                .authorities(Set.of("ROLE_CUSTOMER"))
                .build();

        authResponse = AuthResponse.builder()
                .accessToken("jwt-token")
                .tokenType("Bearer")
                .user(userResponse)
                .build();
    }

    @Test
    void testRegisterSuccess() {
        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        var response = authController.register(registerRequest);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAccessToken()).isEqualTo("jwt-token");
        assertThat(response.getBody().getTokenType()).isEqualTo("Bearer");
        assertThat(response.getBody().getUser().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testLoginSuccess() {
        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        var response = authController.login(loginRequest);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAccessToken()).isEqualTo("jwt-token");
        assertThat(response.getBody().getTokenType()).isEqualTo("Bearer");
        assertThat(response.getBody().getUser().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testRegisterReturnsUserInfo() {
        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        var response = authController.register(registerRequest);

        assertThat(response.getBody().getUser()).isNotNull();
        assertThat(response.getBody().getUser().getFullName()).isEqualTo("John Doe");
        assertThat(response.getBody().getUser().getPhone()).isEqualTo("1234567890");
    }

    @Test
    void testLoginReturnsToken() {
        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        var response = authController.login(loginRequest);

        assertThat(response.getBody().getAccessToken()).isNotNull();
        assertThat(response.getBody().getAccessToken()).isNotEmpty();
    }

    @Test
    void testAuthResponseTokenType() {
        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        var response = authController.register(registerRequest);

        assertThat(response.getBody().getTokenType()).isEqualTo("Bearer");
    }

    @Test
    void testRegisterWithCorrectUserType() {
        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        var response = authController.register(registerRequest);

        assertThat(response.getBody().getUser().getUserType()).isEqualTo(UserType.CUSTOMER);
    }

    @Test
    void testLoginWithValidCredentials() {
        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        var response = authController.login(loginRequest);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUser()).isNotNull();
    }
}
