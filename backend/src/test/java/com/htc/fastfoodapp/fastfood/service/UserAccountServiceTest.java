package com.htc.fastfoodapp.fastfood.service;

import com.htc.fastfoodapp.fastfood.dto.UserResponse;
import com.htc.fastfoodapp.fastfood.entity.Authority;
import com.htc.fastfoodapp.fastfood.entity.UserAccount;
import com.htc.fastfoodapp.fastfood.entity.UserType;
import com.htc.fastfoodapp.fastfood.repository.UserAccountRepository;
import com.htc.fastfoodapp.fastfood.service.mapper.UserAccountMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserAccountServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private UserAccountMapper userAccountMapper;

    @InjectMocks
    private UserAccountService userAccountService;

    private UserAccount userAccount;
    private UserResponse userResponse;
    private Authority authority;

    @BeforeEach
    void setUp() {
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

        userResponse = UserResponse.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .userType(UserType.CUSTOMER)
                .enabled(true)
                .authorities(Set.of("ROLE_CUSTOMER"))
                .build();
    }

    @Test
    void testGetUserByIdSuccess() {
        when(userAccountRepository.findById(1L)).thenReturn(Optional.of(userAccount));
        when(userAccountMapper.toUserResponse(userAccount)).thenReturn(userResponse);

        UserResponse result = userAccountService.getUserById(1L);

        assertNotNull(result);
        assertEquals("john@example.com", result.getEmail());
        assertEquals("John Doe", result.getFullName());

        verify(userAccountRepository).findById(1L);
        verify(userAccountMapper).toUserResponse(userAccount);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userAccountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userAccountService.getUserById(1L));
        verify(userAccountRepository).findById(1L);
    }

    @Test
    void testGetUserByEmailSuccess() {
        when(userAccountRepository.findByEmail("john@example.com")).thenReturn(Optional.of(userAccount));
        when(userAccountMapper.toUserResponse(userAccount)).thenReturn(userResponse);

        UserResponse result = userAccountService.getUserByEmail("john@example.com");

        assertNotNull(result);
        assertEquals("john@example.com", result.getEmail());

        verify(userAccountRepository).findByEmail("john@example.com");
    }

    @Test
    void testGetAllUsers() {
        UserAccount anotherUser = UserAccount.builder()
                .id(2L)
                .fullName("Jane Doe")
                .email("jane@example.com")
                .build();

        UserResponse anotherResponse = UserResponse.builder()
                .id(2L)
                .fullName("Jane Doe")
                .email("jane@example.com")
                .build();

        when(userAccountRepository.findAll()).thenReturn(List.of(userAccount, anotherUser));
        when(userAccountMapper.toUserResponse(userAccount)).thenReturn(userResponse);
        when(userAccountMapper.toUserResponse(anotherUser)).thenReturn(anotherResponse);

        List<UserResponse> result = userAccountService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(userAccountRepository).findAll();
        verify(userAccountMapper, times(2)).toUserResponse(any(UserAccount.class));
    }

    @Test
    void testUpdateUserSuccess() {
        UserAccount updateRequest = UserAccount.builder()
                .fullName("John Updated")
                .phone("9876543210")
                .build();

        when(userAccountRepository.findById(1L)).thenReturn(Optional.of(userAccount));
        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(userAccount);
        when(userAccountMapper.toUserResponse(userAccount)).thenReturn(userResponse);

        UserResponse result = userAccountService.updateUser(1L, updateRequest);

        assertNotNull(result);
        verify(userAccountRepository).findById(1L);
        verify(userAccountRepository).save(any(UserAccount.class));
    }

    @Test
    void testDeleteUserSuccess() {
        when(userAccountRepository.findById(1L)).thenReturn(Optional.of(userAccount));

        userAccountService.deleteUser(1L);

        verify(userAccountRepository).findById(1L);
        verify(userAccountRepository).delete(userAccount);
    }

    @Test
    void testDeleteUserNotFound() {
        when(userAccountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userAccountService.deleteUser(1L));
        verify(userAccountRepository, never()).delete(any(UserAccount.class));
    }

    @Test
    void testEnableUserSuccess() {
        when(userAccountRepository.findById(1L)).thenReturn(Optional.of(userAccount));
        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(userAccount);

        userAccountService.enableUser(1L);

        verify(userAccountRepository).findById(1L);
        verify(userAccountRepository).save(any(UserAccount.class));
        assertTrue(userAccount.isEnabled());
    }

    @Test
    void testDisableUserSuccess() {
        when(userAccountRepository.findById(1L)).thenReturn(Optional.of(userAccount));
        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(userAccount);

        userAccountService.disableUser(1L);

        verify(userAccountRepository).findById(1L);
        verify(userAccountRepository).save(any(UserAccount.class));
    }

    @Test
    void testExistsByEmailTrue() {
        when(userAccountRepository.findByEmail("john@example.com")).thenReturn(Optional.of(userAccount));

        boolean result = userAccountService.existsByEmail("john@example.com");

        assertTrue(result);
        verify(userAccountRepository).findByEmail("john@example.com");
    }

    @Test
    void testExistsByEmailFalse() {
        when(userAccountRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        boolean result = userAccountService.existsByEmail("nonexistent@example.com");

        assertFalse(result);
        verify(userAccountRepository).findByEmail("nonexistent@example.com");
    }
}
