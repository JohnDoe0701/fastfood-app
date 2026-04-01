package com.htc.fastfoodapp.fastfood.dto;

import com.htc.fastfoodapp.fastfood.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private UserType userType;
    private boolean enabled;
    private Set<String> authorities;
}
