# FastFood App - Login Credentials

**Generated:** 31/3/2026, 3:32:46 pm

## 🚀 Quick Start

- **Frontend:** http://localhost:5173
- **Backend API:** http://localhost:8080
- **Database:** MySQL on localhost:3306 (database: hms)

## 👥 Test Accounts

### 👨‍💼 ADMIN

**Name:** Admin User
**Email:** admin@akfood.com
**Password:** Admin@123456
**Role:** ADMIN

### 👨‍🍳 RESTAURANT_MANAGER

**Name:** Manager User
**Email:** manager@akfood.com
**Password:** Manager@123456
**Role:** RESTAURANT_MANAGER

### 👤 CUSTOMER

**Name:** Customer User
**Email:** customer@akfood.com
**Password:** Customer@123456
**Role:** CUSTOMER

## 🔑 Role Permissions

### 👤 CUSTOMER
- ✅ Register & Login
- ✅ Browse menu items
- ✅ Place orders
- ✅ View own orders
- ✅ Update profile
- ✅ Delete account

### 👨‍🍳 RESTAURANT_MANAGER
- ✅ All CUSTOMER permissions
- ⚠️ No additional features yet (available for future expansion)

### 👨‍💼 ADMIN
- ✅ All CUSTOMER permissions
- ✅ View all users
- ✅ Delete any user
- ✅ Enable/Disable users

## 📦 Database Contents

### Categories Created:
- **Burgers** - Delicious burgers made with fresh ingredients
- **Pizza** - Traditional and specialty pizzas
- **Tacos** - Authentic Mexican tacos
- **Sides** - Fries, wings, and other sides
- **Beverages** - Drinks and beverages
- **Desserts** - Sweet desserts and treats

### Sample Menu Items by Category:

**Burgers:**
- Classic Burger (₹250) - Beef patty with lettuce, tomato, and mayo
- Cheese Burger (₹290) - Classic burger with melted cheddar cheese
- Double Burger (₹380) - Two beef patties with double cheese
- Spicy Burger (₹300) - Burger with jalapeños and spicy mayo

**Pizza:**
- Margherita (₹350) - Fresh mozzarella, basil, tomato
- Pepperoni Pizza (₹400) - Classic pepperoni with extra cheese
- Veggie Pizza (₹380) - Loaded with fresh vegetables
- Seafood Pizza (₹500) - Shrimp and calamari on thin crust

**Tacos:**
- Chicken Taco (₹120) - Seasoned chicken with fresh toppings
- Beef Taco (₹130) - Spiced ground beef with cilantro
- Fish Taco (₹150) - Crispy fish with lime and cabbage
- Veggie Taco (₹110) - Bean and vegetable filled taco

**Sides:**
- French Fries (₹100) - Crispy golden fries
- Chicken Wings (₹220) - 6 pieces of spicy chicken wings
- Onion Rings (₹120) - Crispy fried onion rings
- Mac & Cheese (₹180) - Creamy macaroni and cheese

**Beverages:**
- Coke (₹50) - Cold coca cola
- Iced Tea (₹60) - Freshly made iced tea
- Lemonnade (₹70) - Fresh lemonade
- Milkshake (₹120) - Vanilla, chocolate, or strawberry

**Desserts:**
- Chocolate Cake (₹180) - Rich chocolate cake with frosting
- Ice Cream (₹100) - Vanilla, chocolate, or strawberry
- Brownie (₹120) - Fudgy chocolate brownie
- Cheesecake (₹200) - New York style cheesecake

## 🧪 Testing Workflow

### As a Customer:
1. Open http://localhost:5173
2. Login with customer credentials
3. Browse menu categories
4. Add items to cart
5. Place an order
6. View order status

### As an Admin:
1. Login with admin credentials
2. Use backend API to manage users
3. View all registered users via `GET /api/users`
4. Enable/disable users via `PUT /api/users/{id}/enable`, `PUT /api/users/{id}/disable`
5. Delete users via `DELETE /api/users/{id}`

## 🔗 Common API Endpoints

### Authentication
- `POST /api/auth/register` - Register new customer
- `POST /api/auth/login` - Login and get JWT token

### Menu
- `GET /api/categories` - List all categories
- `GET /api/menu-items` - List all menu items

### Users (Admin only)
- `GET /api/users` - List all users
- `GET /api/users/{id}` - Get user details
- `PUT /api/users/{id}" - Update user
- `DELETE /api/users/{id}" - Delete user

### Orders
- `POST /api/orders` - Create order
- `GET /api/orders` - Get own orders
- `GET /api/orders/{id}" - Get order details

## ⚡ Notes

- All passwords are hashed with BCrypt
- JWT tokens expire after 24 hours
- API requires JWT token in `Authorization: Bearer {token}` header
- All endpoints return proper error messages and HTTP status codes

