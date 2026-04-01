package com.htc.fastfoodapp.fastfood.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.htc.fastfoodapp.fastfood.entity.FoodOrder;

public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {
    List<FoodOrder> findByUserId(Long userId);
}
