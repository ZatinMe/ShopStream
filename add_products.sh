#!/bin/bash

echo "Adding sample products to ShopStream..."

# Create a simple product using curl
curl -X POST "http://localhost:8080/api/products" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "MacBook Pro 16-inch",
    "description": "Apple MacBook Pro with M2 Pro chip, 16GB RAM, 512GB SSD",
    "price": "2499.99",
    "stock": 5,
    "category": "Electronics",
    "tags": ["laptop", "apple", "macbook", "computer"],
    "sellerId": 1
  }' --max-time 30

echo -e "\n\n"

curl -X POST "http://localhost:8080/api/products" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15 Pro",
    "description": "Latest iPhone with A17 Pro chip, 256GB storage",
    "price": "999.99",
    "stock": 10,
    "category": "Electronics",
    "tags": ["phone", "apple", "iphone", "mobile"],
    "sellerId": 1
  }' --max-time 30

echo -e "\n\n"

curl -X POST "http://localhost:8080/api/products" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sony WH-1000XM5 Headphones",
    "description": "Premium noise-canceling wireless headphones",
    "price": "399.99",
    "stock": 8,
    "category": "Electronics",
    "tags": ["headphones", "sony", "wireless", "audio"],
    "sellerId": 1
  }' --max-time 30

echo -e "\n\n"

curl -X POST "http://localhost:8080/api/products" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nike Air Max 270",
    "description": "Comfortable running shoes with Air Max technology",
    "price": "150.00",
    "stock": 15,
    "category": "Fashion",
    "tags": ["shoes", "nike", "running", "sports"],
    "sellerId": 1
  }' --max-time 30

echo -e "\n\n"

curl -X POST "http://localhost:8080/api/products" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Coffee Maker Deluxe",
    "description": "Programmable coffee maker with 12-cup capacity",
    "price": "89.99",
    "stock": 12,
    "category": "Home & Kitchen",
    "tags": ["coffee", "kitchen", "appliance", "home"],
    "sellerId": 1
  }' --max-time 30

echo -e "\n\nDone adding products!"
