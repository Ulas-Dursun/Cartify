# 🛒 Cartify — Order & Inventory Management API

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3-brightgreen?style=flat-square&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue?style=flat-square&logo=postgresql)
![JWT](https://img.shields.io/badge/JWT-Auth-purple?style=flat-square)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=flat-square&logo=docker)

Cartify is a production-grade REST API designed to manage e-commerce order lifecycles and real-time inventory tracking. Built with a focus on **data consistency** and **concurrency control** in a high-traffic environment.

---

## ✨ Key Features

- **Order State Machine** — Strict enforcement of transitions: `PENDING` → `CONFIRMED` → `SHIPPED` → `DELIVERED`.
- **Race Condition Prevention** — Utilizes **Pessimistic Locking** to ensure stock integrity during high-concurrency checkout operations.
- **Secure by Design** — Stateless JWT authentication with **Role-Based Access Control (RBAC)** for Users and Admins.
- **Audit-Ready Persistence** — Soft-delete mechanism for products to maintain referential integrity in historical orders.
- **Automated Inventory Sync** — Real-time stock restoration on order cancellations or failures.
- **Interactive API Docs** — Comprehensive documentation with **Swagger/OpenAPI 3**.

---

## 🛠 Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Backend** | Java 21 (LTS), Spring Boot 3.3, Spring Security |
| **Database** | PostgreSQL 17, Spring Data JPA (Hibernate) |
| **Testing** | JUnit 5, Mockito, **Testcontainers** |
| **Containerization** | Docker & Docker Compose |
| **Documentation** | Swagger UI |

---

## 🏗 Engineering Decisions (The "Why")

- **Concurrency Guard:** Why Pessimistic Locking? In a real-world scenario, multiple users might buy the last item simultaneously. I implemented database-level locking on the Product entity to prevent overselling.
- **Data Integrity:** Why Soft Deletes? Deleting a product from the database would orphan old order records. I used an `active` flag to preserve the business history while removing it from the storefront.
- **Reliable Testing:** Why Testcontainers? Instead of H2 (which behaves differently than Postgres), I used Testcontainers to run integration tests against a real PostgreSQL instance inside Docker.

---

## 🚦 Quick Start

### 1. Prerequisites
- Docker & Docker Compose installed.

### 2. Run with One Command
```bash
git clone [https://github.com/ulasdursun/cartify.git](https://github.com/ulasdursun/cartify.git)
cd cartify
docker-compose up --build
