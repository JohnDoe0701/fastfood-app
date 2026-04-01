package com.htc.fastfoodapp.fastfood.controller;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.htc.fastfoodapp.fastfood.dto.UserResponse;
import com.htc.fastfoodapp.fastfood.entity.UserAccount;
import com.htc.fastfoodapp.fastfood.entity.UserType;
import com.htc.fastfoodapp.fastfood.service.UserAccountService;

@ExtendWith(MockitoExtension.class)
public class UserAccountControllerTest {

    @InjectMocks
    private UserAccountController userAccountController;

    @Mock
    private UserAccountService userAccountService;

    private UserResponse userResponse;
    private UserAccount userAccount;

    @BeforeEach
    void setUp() {
        userResponse = UserResponse.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .userType(UserType.CUSTOMER)
                .enabled(true)
                .authorities(Set.of("ROLE_CUSTOMER"))
                .build();;

        userAccount = UserAccount.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .userType(UserType.CUSTOMER)
                .enabled(true)
                .build();
    }

    @Test
    void testGetAllUsers() {
        UserResponse anotherUser = UserResponse.builder()
                .id(2L)
                .fullName("Jane Doe")
                .email("jane@example.com")
                .build();

        when(userAccountService.getAllUsers()).thenReturn(List.of(userResponse, anotherUser));

        var response = userAccountController.getAll();

        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0).getEmail()).isEqualTo("john@example.com");
        verify(userAccountService).getAllUsers();
    }

    @Test
    void testGetUserById() {
        when(userAccountService.getUserById(1L)).thenReturn(userResponse);

        var response = userAccountController.getById(1L);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("john@example.com");
        assertThat(response.getBody().getFullName()).isEqualTo("John Doe");
        verify(userAccountService).getUserById(1L);
    }

    @Test
    void testUpdateUser() {
        UserAccount updateRequest = UserAccount.builder()
                .fullName("John Updated")
                .phone("9876543210")
                .build();

        UserResponse updatedResponse = UserResponse.builder()
                .id(1L)
                .fullName("John Updated")
                .email("john@example.com")
                .phone("9876543210")
                .userType(UserType.CUSTOMER)
                .build();

        when(userAccountService.updateUser(anyLong(), any(UserAccount.class)))
                .thenReturn(updatedResponse);

        var response = userAccountController.update(1L, updateRequest);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFullName()).isEqualTo("John Updated");
        verify(userAccountService).updateUser(anyLong(), any(UserAccount.class));
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userAccountService).deleteUser(1L);

        var response = userAccountController.delete(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(userAccountService).deleteUser(1L);
    }

    @Test
    void testEnableUser() {
        doNothing().when(userAccountService).enableUser(1L);

        var response = userAccountController.enableUser(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(userAccountService).enableUser(1L);
    }

    @Test
    void testDisableUser() {
        doNothing().when(userAccountService).disableUser(1L);

        var response = userAccountController.disableUser(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(userAccountService).disableUser(1L);
    }

    @Test
    void testGetAllUsersReturnsMultipleUsers() {
        List<UserResponse> userList = List.of(userResponse);
        when(userAccountService.getAllUsers()).thenReturn(userList);

        var response = userAccountController.getAll();

        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody().get(0).getId()).isEqualTo(1L);
    }

    @Test
    void testUserResponseNotNull() {
        when(userAccountService.getUserById(1L)).thenReturn(userResponse);

        var response = userAccountController.getById(1L);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isNotNull();
    }
}
