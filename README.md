# ShopStream Marketplace

AI-powered marketplace with React frontend and Spring Boot backend.

## Phase 1 Features

### Backend
- **Authentication & Users**: JWT-based authentication with BCrypt password hashing
- **Product Management**: CRUD operations for products (MongoDB)
- **Order Management**: CRUD operations for orders (PostgreSQL)
- **Multi-database support**: PostgreSQL, MongoDB, Redis
- **RESTful API**: Complete API with proper security and validation

### Frontend
- **React UI**: Modern React 18 with Vite and Tailwind CSS
- **Authentication**: Login/Register pages with JWT integration
- **Product Management**: Full CRUD interface for products
- **Order Management**: Order creation and tracking interface
- **Responsive Design**: Mobile-friendly with Tailwind CSS

## Tech Stack

### Backend
- **Framework**: Spring Boot 3.2.0, Java 17
- **Databases**: PostgreSQL, MongoDB, Redis
- **Security**: Spring Security, JWT
- **Build Tool**: Maven
- **API**: RESTful with proper validation

### Frontend
- **Framework**: React 18 with Vite
- **Styling**: Tailwind CSS
- **HTTP Client**: Axios
- **Routing**: React Router
- **Forms**: React Hook Form

## Quick Start

### Prerequisites

- Java 17+
- Node.js 18+
- Maven 3.6+
- Docker & Docker Compose

### Development Setup

1. **Clone the project:**
   ```bash
   git clone <repository-url>
   cd ShopStream
   ```

2. **Start everything with one command:**
   ```bash
   ./start.sh
   ```

   This will:
   - Build the backend (Maven)
   - Build the frontend (npm)
   - Start all services with Docker Compose

3. **Access the application:**
   - **Frontend**: http://localhost:3000
   - **Backend API**: http://localhost:8080
   - **Health Check**: http://localhost:8080/api/public/health

### API Endpoints

#### Authentication
- `POST /api/auth/signup` - User registration
- `POST /api/auth/signin` - User login

#### Products
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create product (SELLER/ADMIN)
- `PUT /api/products/{id}` - Update product (SELLER/ADMIN)
- `DELETE /api/products/{id}` - Delete product (SELLER/ADMIN)

#### Orders
- `GET /api/orders` - Get user orders
- `GET /api/orders/{id}` - Get order by ID
- `POST /api/orders` - Create order (USER)
- `PUT /api/orders/{id}` - Update order (USER/ADMIN)

#### Public
- `GET /api/public/health` - Health check
- `GET /api/public/info` - Service information

### Database Configuration

- **PostgreSQL**: `localhost:5432` (users, orders)
- **MongoDB**: `localhost:27017` (products)
- **Redis**: `localhost:6379` (sessions)

### Default Configuration

The application uses the following default settings:

```yaml
server.port: 8080
spring.datasource.url: jdbc:postgresql://localhost:5432/shopstream
spring.data.mongodb.uri: mongodb://localhost:27017/shopstream
spring.redis.host: localhost
spring.redis.port: 6379
```

### User Roles

- **USER**: Can create orders, view products
- **SELLER**: Can manage products, view orders
- **ADMIN**: Full access to all resources

## Development

### Project Structure

```
ShopStream/
├── backend/                    # Spring Boot Backend
│   ├── src/main/java/com/shopstream/marketplace/
│   │   ├── config/            # Configuration classes
│   │   ├── controller/        # REST controllers
│   │   ├── model/            # Entity models
│   │   ├── payload/          # Request/Response DTOs
│   │   ├── repository/       # Data access layer
│   │   ├── security/         # Security configuration
│   │   └── service/          # Business logic layer
│   ├── pom.xml               # Maven configuration
│   └── Dockerfile            # Backend container
├── frontend/                  # React Frontend
│   ├── src/
│   │   ├── components/       # React components
│   │   ├── pages/           # Page components
│   │   ├── services/        # API services
│   │   └── config/          # Configuration
│   ├── package.json          # npm dependencies
│   └── Dockerfile            # Frontend container
├── docker-compose.yml        # Multi-service orchestration
└── start.sh                 # One-command startup
```

### Testing

Run tests with:
```bash
mvn test
```

### Building for Production

```bash
mvn clean package
docker-compose -f docker-compose.yml up --build
```

## Next Phases

- Phase 2: AI-powered recommendations
- Phase 3: Advanced analytics
- Phase 4: Microservices architecture
