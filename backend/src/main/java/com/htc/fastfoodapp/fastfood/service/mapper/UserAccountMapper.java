package com.htc.fastfoodapp.fastfood.service.mapper;

import com.htc.fastfoodapp.fastfood.dto.UserResponse;
import com.htc.fastfoodapp.fastfood.entity.UserAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {

    @Mapping(target = "authorities", expression = "java(userAccount.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(java.util.stream.Collectors.toSet()))")
    UserResponse toUserResponse(UserAccount userAccount);
}
