package com.htc.fastfoodapp.fastfood.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.htc.fastfoodapp.fastfood.entity.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByAvailableTrue();
}
