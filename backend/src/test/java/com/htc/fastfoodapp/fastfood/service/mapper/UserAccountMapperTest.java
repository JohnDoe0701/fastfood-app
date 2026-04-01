package com.htc.fastfoodapp.fastfood.service.mapper;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.htc.fastfoodapp.fastfood.dto.UserResponse;
import com.htc.fastfoodapp.fastfood.entity.Authority;
import com.htc.fastfoodapp.fastfood.entity.UserAccount;
import com.htc.fastfoodapp.fastfood.entity.UserType;

public class UserAccountMapperTest {

    private UserAccountMapper userAccountMapper;

    private UserAccount userAccount;

    @BeforeEach
    void setUp() {
        userAccountMapper = Mappers.getMapper(UserAccountMapper.class);
        Authority authority1 = Authority.builder()
                .id(1L)
                .authority("ROLE_CUSTOMER")
                .build();

        Authority authority2 = Authority.builder()
                .id(2L)
                .authority("ROLE_USER")
                .build();

        userAccount = UserAccount.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .password("encodedPassword")
                .userType(UserType.CUSTOMER)
                .enabled(true)
                .authorities(Set.of(authority1, authority2))
                .build();
    }

    @Test
    void testToUserResponseSuccess() {
        UserResponse response = userAccountMapper.toUserResponse(userAccount);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John Doe", response.getFullName());
        assertEquals("john@example.com", response.getEmail());
        assertEquals("1234567890", response.getPhone());
        assertEquals(UserType.CUSTOMER, response.getUserType());
        assertTrue(response.isEnabled());
        assertNotNull(response.getAuthorities());
        assertEquals(2, response.getAuthorities().size());
        assertTrue(response.getAuthorities().contains("ROLE_CUSTOMER"));
        assertTrue(response.getAuthorities().contains("ROLE_USER"));
    }

    @Test
    void testToUserResponseWithSingleAuthority() {
        Authority singleAuthority = Authority.builder()
                .id(1L)
                .authority("ROLE_ADMIN")
                .build();

        userAccount.setAuthorities(Set.of(singleAuthority));

        UserResponse response = userAccountMapper.toUserResponse(userAccount);

        assertNotNull(response);
        assertEquals(1, response.getAuthorities().size());
        assertTrue(response.getAuthorities().contains("ROLE_ADMIN"));
    }

    @Test
    void testToUserResponseWithNoAuthorities() {
        userAccount.setAuthorities(Set.of());

        UserResponse response = userAccountMapper.toUserResponse(userAccount);

        assertNotNull(response);
        assertEquals(0, response.getAuthorities().size());
    }

    @Test
    void testToUserResponseDisabledUser() {
        userAccount.setEnabled(false);

        UserResponse response = userAccountMapper.toUserResponse(userAccount);

        assertNotNull(response);
        assertFalse(response.isEnabled());
    }

    @Test
    void testToUserResponseWithDifferentUserType() {
        userAccount.setUserType(UserType.ADMIN);

        UserResponse response = userAccountMapper.toUserResponse(userAccount);

        assertNotNull(response);
        assertEquals(UserType.ADMIN, response.getUserType());
    }

    @Test
    void testToUserResponseWithRestaurantManager() {
        userAccount.setUserType(UserType.RESTAURANT_MANAGER);

        UserResponse response = userAccountMapper.toUserResponse(userAccount);

        assertNotNull(response);
        assertEquals(UserType.RESTAURANT_MANAGER, response.getUserType());
    }
}
