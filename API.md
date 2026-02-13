# ShopStream Marketplace API Documentation

## Base URL
```
http://localhost:8080
```

## Authentication

### Register User
```http
POST /api/auth/signup
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}
```

### Login
```http
POST /api/auth/signin
Content-Type: application/json

{
  "username": "johndoe",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "roles": ["ROLE_USER"]
}
```

## Products

### Get All Products
```http
GET /api/products
```

### Get Product by ID
```http
GET /api/products/{id}
```

### Create Product (SELLER/ADMIN only)
```http
POST /api/products
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "stock": 10,
  "category": "Electronics",
  "tags": ["laptop", "computer"],
  "sellerId": 1
}
```

### Update Product (SELLER/ADMIN only)
```http
PUT /api/products/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Updated Laptop",
  "description": "Updated description",
  "price": 899.99,
  "stock": 5,
  "category": "Electronics"
}
```

### Delete Product (SELLER/ADMIN only)
```http
DELETE /api/products/{id}
Authorization: Bearer {token}
```

### Search Products
```http
GET /api/products/search?name=laptop
GET /api/products/search?tag=electronics
GET /api/products/search?minPrice=100&maxPrice=500
```

## Orders

### Get User Orders
```http
GET /api/orders
Authorization: Bearer {token}
```

### Get Order by ID
```http
GET /api/orders/{id}
Authorization: Bearer {token}
```

### Create Order
```http
POST /api/orders
Authorization: Bearer {token}
Content-Type: application/json

{
  "totalAmount": 999.99,
  "shippingAddress": "123 Main St, City, State",
  "billingAddress": "123 Main St, City, State",
  "paymentMethod": "credit_card",
  "orderItems": [
    {
      "productId": "product123",
      "productName": "Laptop",
      "price": 999.99,
      "quantity": 1
    }
  ]
}
```

### Update Order Status (ADMIN only)
```http
PUT /api/orders/{id}/status?status=SHIPPED
Authorization: Bearer {token}
```

## Public Endpoints

### Health Check
```http
GET /api/public/health
```

### Service Info
```http
GET /api/public/info
```

## Error Responses

### 400 Bad Request
```json
{
  "message": "Error: Username is already taken!"
}
```

### 401 Unauthorized
```json
{
  "message": "Error: Unauthorized"
}
```

### 403 Forbidden
```json
{
  "message": "Access Denied"
}
```

### 404 Not Found
```json
{
  "message": "Resource not found"
}
```

## User Roles

- **USER**: Can create orders, view products
- **SELLER**: Can manage products, view orders  
- **ADMIN**: Full access to all resources

## Rate Limiting

Currently no rate limiting is implemented. This will be added in future phases.

## CORS

CORS is enabled for all origins in development. In production, configure specific origins.
