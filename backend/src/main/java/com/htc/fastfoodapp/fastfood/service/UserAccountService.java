package com.htc.fastfoodapp.fastfood.service;

import com.htc.fastfoodapp.fastfood.dto.UserResponse;
import com.htc.fastfoodapp.fastfood.entity.UserAccount;
import com.htc.fastfoodapp.fastfood.repository.UserAccountRepository;
import com.htc.fastfoodapp.fastfood.service.mapper.UserAccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserAccountMapper userAccountMapper;

    public UserResponse getUserById(Long id) {
        UserAccount userAccount = userAccountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return userAccountMapper.toUserResponse(userAccount);
    }

    public UserResponse getUserByEmail(String email) {
        UserAccount userAccount = userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return userAccountMapper.toUserResponse(userAccount);
    }

    public List<UserResponse> getAllUsers() {
        return userAccountRepository.findAll().stream()
                .map(userAccountMapper::toUserResponse)
                .toList();
    }

    public UserResponse updateUser(Long id, UserAccount updateRequest) {
        UserAccount userAccount = userAccountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        userAccount.setFullName(updateRequest.getFullName());
        userAccount.setPhone(updateRequest.getPhone());

        UserAccount updated = userAccountRepository.save(userAccount);
        log.info("User updated: {}", updated.getEmail());

        return userAccountMapper.toUserResponse(updated);
    }

    public void deleteUser(Long id) {
        UserAccount userAccount = userAccountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        userAccountRepository.delete(userAccount);
        log.info("User deleted: {}", userAccount.getEmail());
    }

    public void enableUser(Long id) {
        UserAccount userAccount = userAccountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        userAccount.setEnabled(true);
        userAccountRepository.save(userAccount);
        log.info("User enabled: {}", userAccount.getEmail());
    }

    public void disableUser(Long id) {
        UserAccount userAccount = userAccountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        userAccount.setEnabled(false);
        userAccountRepository.save(userAccount);
        log.info("User disabled: {}", userAccount.getEmail());
    }

    public boolean existsByEmail(String email) {
        return userAccountRepository.findByEmail(email).isPresent();
    }
}
