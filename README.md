# Ecommerce Website

## Overview
This document provides a simplified and more senior-level version of the product requirements and high-level design for an ecommerce platform.

The goal is to present the system in a clean, structured, and engineering-focused README format.

***

## Functional Requirements

### 1. User Management
- User registration using email or social login.
- Secure login and logout.
- Profile view and update.
- Password reset using secure email links.

### 2. Product Catalog
- Browse products by category.
- View product details such as images, description, price, and specifications.
- Search products using keywords and filters.

### 3. Cart and Checkout
- Add or remove items from cart.
- View cart summary with quantity, pricing, and total amount.
- Checkout with delivery address, shipping option, and payment method.

### 4. Order Management
- Place orders successfully after payment.
- View order history.
- Track order status from confirmation to delivery.

### 5. Payment
- Support multiple payment methods such as cards, UPI, net banking, and wallets.
- Ensure secure payment processing.
- Generate payment receipt after successful transaction.

### 6. Authentication and Session
- Secure authentication using token-based or OAuth2-based mechanisms.
- Session management with expiry and logout support.

### 7. Notifications and Reviews
- Send email or SMS notifications for order updates.
- Allow users to add product reviews and ratings.

***

## High-Level Design

### Architecture Style
The platform follows a microservices-based architecture to support scalability, independent deployments, and better fault isolation.

### Core Components
- Load Balancer
- API Gateway
- Microservices
- MySQL
- MongoDB
- Redis
- Kafka
- Elasticsearch
- Kubernetes on EKS
- Helm
- Spring Cloud Config
- HCP Vault

***

## Service Design

### 1. API Gateway
- Single entry point for all client requests.
- Handles routing, authentication, rate limiting, and request validation.
- Can be implemented using Kong.

### 2. User Service
- Manages registration, login, profile updates, and password reset.
- Stores user data in MySQL.

### 3. Product Service
- Manages product listings, categories, prices, and stock details.
- Stores product master data in MySQL.

### 4. Search Service
- Provides fast product search with filters and keyword support.
- Uses Elasticsearch for indexing and querying products.

### 5. Cart Service
- Manages active user carts.
- Uses MongoDB for flexible cart data.
- Uses Redis for low-latency cart access and caching.

### 6. Order Service
- Handles order placement, order history, and order tracking.
- Stores order data in MySQL.
- Communicates asynchronously with payment and notification services through Kafka.

### 7. Payment Service
- Integrates with external payment gateways.
- Stores transaction logs in MySQL.
- Publishes payment success or failure events to Kafka.

### 8. Notification Service
- Sends email and SMS notifications.
- Consumes events from Kafka.
- Can integrate with Amazon SES or SNS.

### 9. Review Service
- Stores user reviews and ratings.
- Uses MongoDB for flexible review documents.
- Can later integrate sentiment analysis if needed.

***

## Infrastructure and Platform Design

### Load Balancer
Distributes incoming traffic across application instances to improve availability and fault tolerance.

### Kubernetes and EKS
Used for container orchestration, scaling, service deployment, and self-healing.

### Helm
Used to package and manage Kubernetes deployments in a reusable way.

### Config Management
Spring Cloud Config is used to externalize service configuration.

### Secrets Management
HCP Vault is used for storing secrets, rotating credentials, and maintaining audit logs.

***

## Data and Communication Design

### MySQL
Used for strongly consistent transactional data such as:
- Users
- Orders
- Payments
- Product master data

### MongoDB
Used for flexible and document-style data such as:
- Carts
- Reviews

### Redis
Used for caching and fast-access use cases such as:
- Cart data
- Sessions
- Frequently accessed product information

### Kafka
Used for asynchronous event-driven communication between services.

Example events:
- order.created
- payment.completed
- payment.failed
- notification.triggered

### Elasticsearch
Used to support fast and relevant product search.

***

## Typical Request Flows

### Product Search Flow
1. User sends search request.
2. Request goes through Load Balancer and API Gateway.
3. Gateway routes request to Search Service.
4. Search Service queries Elasticsearch.
5. Results are returned to the user.

### Add to Cart Flow
1. User adds product to cart.
2. Request goes to Cart Service.
3. Cart data is stored in MongoDB and cached in Redis.
4. Optional cart event is published to Kafka.

### Checkout Flow
1. User proceeds to checkout.
2. Order Service creates the order.
3. Payment Service processes the payment.
4. Payment result is published to Kafka.
5. Order Service updates order status.
6. Notification Service sends confirmation.

***

## Senior Engineering Notes
- Prefer event-driven communication for non-blocking workflows.
- Use idempotency for payment and order APIs to avoid duplicate processing.
- Keep transactional boundaries clear between Order and Payment services.
- Use observability tools for logs, metrics, and distributed tracing.
- Secure all internal and external communication using TLS and IAM-based access control.
- Design services for horizontal scaling and failure recovery.

***

## Future Enhancements
- Inventory service for stock reservation and consistency.
- Recommendation engine for personalized product suggestions.
- Fraud detection for payment flows.
- Admin dashboard for catalog and order operations.
- Multi-region deployment for high availability.