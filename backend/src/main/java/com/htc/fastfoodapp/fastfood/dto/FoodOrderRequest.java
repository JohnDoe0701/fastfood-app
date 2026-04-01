package com.htc.fastfoodapp.fastfood.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.htc.fastfoodapp.fastfood.entity.OrderStatus;

import jakarta.validation.constraints.NotNull;

public class FoodOrderRequest {

    @NotNull
    private Long userId;

    @NotNull
    private OrderStatus status;

    @NotNull
    private BigDecimal totalAmount;

    private LocalDateTime placedAt;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getPlacedAt() {
        return placedAt;
    }

    public void setPlacedAt(LocalDateTime placedAt) {
        this.placedAt = placedAt;
    }
}
