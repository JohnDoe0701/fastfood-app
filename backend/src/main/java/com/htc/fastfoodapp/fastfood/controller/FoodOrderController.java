package com.htc.fastfoodapp.fastfood.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.htc.fastfoodapp.fastfood.dto.FoodOrderRequest;
import com.htc.fastfoodapp.fastfood.entity.FoodOrder;
import com.htc.fastfoodapp.fastfood.entity.UserAccount;
import com.htc.fastfoodapp.fastfood.repository.FoodOrderRepository;
import com.htc.fastfoodapp.fastfood.repository.UserAccountRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class FoodOrderController {

    private final FoodOrderRepository foodOrderRepository;
    private final UserAccountRepository userAccountRepository;

    public FoodOrderController(FoodOrderRepository foodOrderRepository, UserAccountRepository userAccountRepository) {
        this.foodOrderRepository = foodOrderRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @GetMapping
    public List<FoodOrder> getAll() {
        return foodOrderRepository.findAll();
    }

    @GetMapping("/{id}")
    public FoodOrder getById(@PathVariable Long id) {
        return foodOrderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    @PostMapping
    public ResponseEntity<FoodOrder> create(@Valid @RequestBody FoodOrderRequest request) {
        FoodOrder order = new FoodOrder();
        applyRequest(order, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(foodOrderRepository.save(order));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_MANAGER')")
    public FoodOrder update(@PathVariable Long id, @Valid @RequestBody FoodOrderRequest request) {
        FoodOrder existing = getById(id);
        applyRequest(existing, request);
        return foodOrderRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        FoodOrder existing = getById(id);
        foodOrderRepository.delete(existing);
        return ResponseEntity.noContent().build();
    }

    private void applyRequest(FoodOrder order, FoodOrderRequest request) {
        UserAccount user = userAccountRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        order.setUser(user);
        order.setStatus(request.getStatus());
        order.setTotalAmount(request.getTotalAmount());
        if (request.getPlacedAt() != null) {
            order.setPlacedAt(request.getPlacedAt());
        }
    }
}
