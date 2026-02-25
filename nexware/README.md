# Order Placement Service

Production-ready Spring Boot REST API for placing orders with atomic transactions and concurrency safety.

---

## Quick Start

```bash
# Run (H2 in-memory, seed data auto-loaded)
mvn spring-boot:run

# Run tests
mvn test

# Run with MySQL
export DB_USERNAME=root DB_PASSWORD=secret
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

Server: `http://localhost:8080`  
H2 Console: `http://localhost:8080/h2-console` → JDBC URL: `jdbc:h2:mem:orderdb`

---

## API

### POST `/api/orders` — Place an Order

**Request**
```json
{
    "userId": 1,
    "productId": 1,
    "quantity": 2
}
```

**201 Created**
```json
{
    "orderId": 1,
    "userId": 1,
    "userName": "Alice Johnson",
    "productId": 1,
    "productName": "Laptop Pro 15",
    "quantity": 2,
    "totalAmount": 2599.98,
    "status": "CONFIRMED",
    "createdAt": "2024-01-15 10:30:00"
}
```

**Error responses**

| Status | Code                | Scenario                          |
|--------|---------------------|-----------------------------------|
| 400    | `VALIDATION_FAILED` | Missing/invalid request fields    |
| 404    | `NOT_FOUND`         | User or product doesn't exist     |
| 409    | `INSUFFICIENT_STOCK`| Requested qty exceeds stock       |
| 409    | `CONCURRENT_UPDATE` | Optimistic lock conflict (retry)  |
| 500    | `INTERNAL_ERROR`    | Unexpected server error           |

---

## Concurrency Design

### Problem
Two threads read `stock = 1` simultaneously → both pass the check → both deduct → `stock = -1`.

### Solution: Pessimistic Write Lock

`ProductRepository.findByIdWithLock()` issues `SELECT ... FOR UPDATE`, acquiring an exclusive row-level
database lock on the product. Any concurrent transaction attempting the same lock will **block** at the
database level until the first transaction commits.

```
Thread A                         Thread B
  │  SELECT stock FOR UPDATE ────┤ ← blocked here
  │  stock=10, pass check        │
  │  stock = 10 - 2 = 8          │
  │  INSERT order                │
  │  COMMIT ─────────────────────┤ ← unblocked
                                 │  SELECT stock FOR UPDATE
                                 │  stock = 8  ← sees correct value
                                 │  continues...
```

Stock deduction + order insert share one `@Transactional` boundary — they either both commit or both roll back.

---

## Seed Data

| ID | User         |
|----|--------------|
| 1  | Alice Johnson|
| 2  | Bob Smith    |
| 3  | Charlie Brown|

| ID | Product             | Price   | Stock |
|----|---------------------|---------|-------|
| 1  | Laptop Pro 15       | 1299.99 | 50    |
| 2  | Wireless Mouse      |   29.99 | 200   |
| 3  | USB-C Hub           |   49.99 | 100   |
| 4  | Mech Keyboard       |   89.99 |  75   |
| 5  | 4K Monitor          |  399.99 |  30   |

---

## Project Structure

```
src/main/java/com/nexware/orderplacement/
├── OrderPlacementApplication.java
├── controller/   OrderController
├── service/      OrderService (interface)
│   └── impl/     OrderServiceImpl
├── repository/   UserRepository, ProductRepository (with lock), OrderRepository
├── model/        User, Product, Order, OrderStatus
├── dto/          OrderRequest, OrderResponse, ErrorResponse
├── exception/    UserNotFoundException, ProductNotFoundException,
│                 InsufficientStockException, GlobalExceptionHandler
└── config/       DataInitializer
```
