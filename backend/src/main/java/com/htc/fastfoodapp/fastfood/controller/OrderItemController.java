package com.htc.fastfoodapp.fastfood.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.htc.fastfoodapp.fastfood.dto.OrderItemRequest;
import com.htc.fastfoodapp.fastfood.entity.FoodOrder;
import com.htc.fastfoodapp.fastfood.entity.MenuItem;
import com.htc.fastfoodapp.fastfood.entity.OrderItem;
import com.htc.fastfoodapp.fastfood.repository.FoodOrderRepository;
import com.htc.fastfoodapp.fastfood.repository.MenuItemRepository;
import com.htc.fastfoodapp.fastfood.repository.OrderItemRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemRepository orderItemRepository;
    private final FoodOrderRepository foodOrderRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderItemController(OrderItemRepository orderItemRepository,
            FoodOrderRepository foodOrderRepository,
            MenuItemRepository menuItemRepository) {
        this.orderItemRepository = orderItemRepository;
        this.foodOrderRepository = foodOrderRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @GetMapping
    public List<OrderItem> getAll() {
        return orderItemRepository.findAll();
    }

    @GetMapping("/{id}")
    public OrderItem getById(@PathVariable Long id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found"));
    }

    @PostMapping
    public ResponseEntity<OrderItem> create(@Valid @RequestBody OrderItemRequest request) {
        OrderItem orderItem = new OrderItem();
        applyRequest(orderItem, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderItemRepository.save(orderItem));
    }

    @PutMapping("/{id}")
    public OrderItem update(@PathVariable Long id, @Valid @RequestBody OrderItemRequest request) {
        OrderItem existing = getById(id);
        applyRequest(existing, request);
        return orderItemRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        OrderItem existing = getById(id);
        orderItemRepository.delete(existing);
        return ResponseEntity.noContent().build();
    }

    private void applyRequest(OrderItem orderItem, OrderItemRequest request) {
        FoodOrder order = foodOrderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found"));

        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Menu item not found"));

        orderItem.setOrder(order);
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(request.getQuantity());
        orderItem.setUnitPrice(request.getUnitPrice());
        orderItem.setLineTotal(request.getLineTotal());
    }
}
