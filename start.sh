#!/bin/bash

echo "🚀 Starting ShopStream Marketplace (Frontend + Backend)..."

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker."
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose."
    exit 1
fi

echo "📦 Building backend..."
cd backend
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Backend build failed. Please check the errors above."
    exit 1
fi

echo "📦 Building frontend..."
cd ../frontend
npm install
npm run build

if [ $? -ne 0 ]; then
    echo "❌ Frontend build failed. Please check the errors above."
    exit 1
fi

echo "🐳 Starting services with Docker Compose..."
cd ..
docker-compose up -d

echo "⏳ Waiting for services to start..."
sleep 15

echo "✅ ShopStream Marketplace is now running!"
echo ""
echo "🌐 Frontend: http://localhost:3000"
echo "🔧 Backend API: http://localhost:8080"
echo "📊 Health Check: http://localhost:8080/api/public/health"
echo ""
echo "📋 Available services:"
echo "  - PostgreSQL: localhost:5432"
echo "  - MongoDB: localhost:27017"
echo "  - Redis: localhost:6379"
echo ""
echo "🔍 To view logs: docker-compose logs -f"
echo "🛑 To stop: docker-compose down"