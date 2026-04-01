# Fastfood Swagger API Test Guide

Swagger UI URL:
- http://localhost:8080/swagger-ui.html

OpenAPI JSON URL:
- http://localhost:8080/v3/api-docs

## Recommended Test Order

1. Create Category
2. Create User
3. Create Menu Item (needs categoryId)
4. Create Order (needs userId)
5. Create Order Item (needs orderId and menuItemId)

## 1) Health

### GET /api/health
No request body needed.

Expected response:
```json
{
  "status": "UP"
}
```

## 2) Categories

### POST /api/categories
```json
{
  "name": "Burgers",
  "description": "Burger and sandwich items"
}
```

### PUT /api/categories/{id}
```json
{
  "name": "Burgers Updated",
  "description": "Updated category description"
}
```

### GET /api/categories
No request body needed.

### GET /api/categories/{id}
No request body needed.

### DELETE /api/categories/{id}
No request body needed.

## 3) Users

### POST /api/users
```json
{
  "fullName": "Akash Sharma",
  "email": "akash@example.com",
  "phone": "9876543210"
}
```

### PUT /api/users/{id}
```json
{
  "fullName": "Akash Sharma Updated",
  "email": "akash.updated@example.com",
  "phone": "9123456780"
}
```

### GET /api/users
No request body needed.

### GET /api/users/{id}
No request body needed.

### DELETE /api/users/{id}
No request body needed.

## 4) Menu Items

Important:
- categoryId must already exist in akfood_categories.

### POST /api/menu-items
```json
{
  "name": "Cheese Burger",
  "description": "Double patty burger",
  "price": 199.00,
  "available": true,
  "categoryId": 1
}
```

### PUT /api/menu-items/{id}
```json
{
  "name": "Cheese Burger XL",
  "description": "Updated burger",
  "price": 229.00,
  "available": true,
  "categoryId": 1
}
```

### GET /api/menu-items
No request body needed.

### GET /api/menu-items/{id}
No request body needed.

### DELETE /api/menu-items/{id}
No request body needed.

## 5) Orders

Important:
- userId must already exist in akfood_users.
- status allowed values:
  - CREATED
  - CONFIRMED
  - PREPARING
  - OUT_FOR_DELIVERY
  - DELIVERED
  - CANCELLED

### POST /api/orders
```json
{
  "userId": 1,
  "status": "CREATED",
  "totalAmount": 398.00,
  "placedAt": "2026-03-30T12:00:00"
}
```

You can also omit placedAt; backend will auto-fill it.

### PUT /api/orders/{id}
```json
{
  "userId": 1,
  "status": "CONFIRMED",
  "totalAmount": 398.00,
  "placedAt": "2026-03-30T12:30:00"
}
```

### GET /api/orders
No request body needed.

### GET /api/orders/{id}
No request body needed.

### DELETE /api/orders/{id}
No request body needed.

## 6) Order Items

Important:
- orderId must already exist in akfood_orders.
- menuItemId must already exist in akfood_menu_items.

### POST /api/order-items
```json
{
  "orderId": 1,
  "menuItemId": 1,
  "quantity": 2,
  "unitPrice": 199.00,
  "lineTotal": 398.00
}
```

### PUT /api/order-items/{id}
```json
{
  "orderId": 1,
  "menuItemId": 1,
  "quantity": 3,
  "unitPrice": 199.00,
  "lineTotal": 597.00
}
```

### GET /api/order-items
No request body needed.

### GET /api/order-items/{id}
No request body needed.

### DELETE /api/order-items/{id}
No request body needed.

## Common Validation Tips

- If you get 400 Category not found, create category first and use its id.
- If you get 400 User not found, create user first and use its id.
- If you get 400 Order/Menu item not found, verify ids exist.
- If you get unique constraint error on email, use a new email value.
