# Ecommerce Website

## Overview
This document provides an improved version of the ecommerce product requirements and high-level design.

The focus is not only on core features, but also on development-time corner cases, failure scenarios, consistency challenges, and operational concerns that are commonly missed in early design documents.

***

## Goals
- Build a scalable and reliable ecommerce platform.
- Ensure secure and smooth user journeys across browsing, cart, checkout, payment, and order tracking.
- Design for real-world production issues such as retries, duplicate requests, partial failures, stale data, and service outages.
- Keep the system extensible for future features such as recommendations, fraud detection, and multi-region deployments.

***

## Functional Requirements

### 1. User Management
- User registration using email or social login.
- Secure login and logout.
- Profile view and update.
- Password reset using secure email links.
- Email and phone verification support.
- Account lock or step-up verification after repeated failed login attempts.

#### Corner Cases
- Duplicate registration attempts with same email or phone.
- Social login account already mapped to another user.
- Expired or reused password reset links.
- Concurrent profile updates from multiple devices.
- User tries to access account after deactivation or suspension.

***

### 2. Product Catalog
- Browse products by category, brand, and filters.
- View product details such as images, description, price, offers, and specifications.
- Search products using keywords and filters.
- Show stock availability and delivery estimates.

#### Corner Cases
- Product exists in catalog but inventory is zero.
- Product is active in DB but not yet indexed in Elasticsearch.
- Product price changes while user is viewing details.
- Product variant mismatch such as size/color not available.
- Soft-deleted or unpublished products still appearing in cache/search.

***

### 3. Cart and Checkout
- Add or remove items from cart.
- Update quantity in cart.
- View cart summary with quantity, pricing, discounts, taxes, and total amount.
- Checkout with delivery address, shipping option, coupon, and payment method.
- Persist cart for logged-in users across sessions.

#### Corner Cases
- Item added to cart becomes out of stock before checkout.
- Product price or discount changes after cart addition.
- Coupon expires before order placement.
- Same user updates cart from multiple devices.
- Duplicate checkout submission due to refresh, retry, or impatient clicks.

***

### 4. Order Management
- Create order after successful checkout flow.
- View order history.
- Track order state from placed to delivered.
- Support order cancellation based on status.
- Support refund initiation for eligible cases.

#### Corner Cases
- Payment succeeds but order confirmation event is delayed.
- Order created but inventory reservation fails.
- Partial shipment for multi-item order.
- Cancellation request arrives during shipping transition.
- Duplicate order creation due to retried client request.

***

### 5. Payment
- Support multiple payment methods such as cards, UPI, net banking, wallets, and COD where allowed.
- Ensure secure payment processing using external gateways.
- Generate payment receipt after successful transaction.
- Support refund and reconciliation workflows.

#### Corner Cases
- Payment gateway times out but later marks transaction as successful.
- User refreshes payment page and retries payment.
- Duplicate payment callback from gateway.
- Payment succeeds for one attempt while another attempt fails.
- Refund requested for already refunded or partially refunded order.

***

### 6. Authentication and Session
- Secure authentication using token-based or OAuth2-based mechanisms.
- Session management with expiry and logout support.
- Device-aware sessions and token refresh support.

#### Corner Cases
- Access token expired but refresh token still valid.
- User logs out from one device but remains active on another.
- Token replay attempt from compromised client.
- Session invalidation after password reset.

***

### 7. Notifications and Reviews
- Send email or SMS notifications for order updates.
- Allow users to add product reviews and ratings.
- Restrict reviews to verified buyers if required.

#### Corner Cases
- Notification event delivered more than once.
- Email sent before order state is finalized.
- Review submitted multiple times for same order item.
- Abusive or spam review content.

***

## Non-Functional Requirements
- High availability for customer-facing APIs.
- Low latency for product browsing, search, and cart operations.
- Strong consistency for payment and order state transitions where required.
- Eventual consistency allowed for search indexing, analytics, and notifications.
- Secure storage of secrets and sensitive data.
- End-to-end observability using logs, metrics, and tracing.
- Horizontal scalability for stateless services.
- Idempotent APIs for critical flows such as checkout and payment.

***

## High-Level Design

### Architecture Style
The platform follows a microservices-based architecture to support scalability, service isolation, and independent deployment.

The system combines synchronous APIs for customer-facing workflows and asynchronous event-driven communication for backend processing.

### Core Components
- Load Balancer
- API Gateway
- User Service
- Product Service
- Inventory Service
- Search Service
- Cart Service
- Order Service
- Payment Service
- Notification Service
- Review Service
- MySQL
- MongoDB
- Redis
- Kafka
- Elasticsearch
- Kubernetes on EKS
- Helm
- Spring Cloud Config
- HCP Vault
- Observability stack

***

## Service Design

### 1. API Gateway
- Single entry point for all client requests.
- Handles authentication, request routing, throttling, and basic validation.
- Can be implemented using Kong.

#### Development Considerations
- Apply rate limits for login, cart, and checkout endpoints.
- Add request correlation IDs for tracing.
- Support idempotency keys for payment and checkout APIs.

***

### 2. User Service
- Manages registration, login, profile updates, password reset, and verification status.
- Stores user profile data in MySQL.

#### Development Considerations
- Use unique constraints on email and phone.
- Store password hashes using bcrypt or Argon2.
- Emit user-related events such as user.created or password.changed.

***

### 3. Product Service
- Manages product metadata, categories, pricing, variants, and product lifecycle state.
- Stores product master data in MySQL.

#### Development Considerations
- Separate product metadata from inventory count.
- Support product versioning or last-updated timestamps for cache invalidation.
- Publish product.updated events for downstream indexing and cache refresh.

***

### 4. Inventory Service
- Manages stock count, reservation, release, and stock deduction.
- Stores inventory data in MySQL.

#### Development Considerations
- Reserve stock during checkout or order creation.
- Release reserved stock on payment failure or timeout.
- Prevent overselling through row-level locking, optimistic locking, or atomic stock update patterns.

***

### 5. Search Service
- Provides product search with filters, sorting, and pagination.
- Uses Elasticsearch for indexing and query performance.

#### Development Considerations
- Search is eventually consistent with product DB.
- Rebuild index support is required for recovery.
- Missing index data should fall back to product service for critical product detail lookups.

***

### 6. Cart Service
- Manages active user carts.
- Uses MongoDB for flexible cart document storage.
- Uses Redis for fast cart retrieval and temporary caching.

#### Development Considerations
- Recalculate price at checkout, not only from cached cart state.
- Use TTL policies for abandoned carts where needed.
- Handle cart merge between guest and logged-in sessions.

***

### 7. Order Service
- Handles order placement, order history, status updates, cancellations, and refunds.
- Stores order data in MySQL.
- Coordinates with inventory, payment, and notification services.

#### Development Considerations
- Use a state machine for order transitions.
- Order creation must be idempotent.
- Use outbox pattern to publish reliable order events.

***

### 8. Payment Service
- Integrates with external payment gateways.
- Stores payment transaction data in MySQL.
- Publishes payment outcome events to Kafka.

#### Development Considerations
- Payment callback processing must be idempotent.
- Maintain payment attempt history per order.
- Reconciliation job required for timeout or unknown gateway outcomes.

***

### 9. Notification Service
- Sends order, payment, and shipment notifications.
- Consumes domain events from Kafka.
- Can integrate with Amazon SES or SNS.

#### Development Considerations
- Templates should be versioned.
- Notification sending should be retryable but deduplicated.
- Failed notifications should go to dead-letter handling.

***

### 10. Review Service
- Stores product reviews and ratings.
- Uses MongoDB for flexible review data.

#### Development Considerations
- Review should be linked to product and order item.
- Support moderation and abuse reporting.
- Do not block order flow on review system failure.

***

## Infrastructure and Platform Design

### Load Balancer
Distributes traffic across service instances and improves availability.

### Kubernetes and EKS
Used for orchestration, rolling deployments, autoscaling, service discovery, and self-healing.

### Helm
Used to version and manage deployments across environments.

### Config Management
Spring Cloud Config is used for centralized external configuration.

### Secrets Management
HCP Vault is used for secrets storage, rotation, access policies, and audit logs.

### Observability
Use centralized logging, metrics, dashboards, and distributed tracing.

Recommended stack:
- Prometheus for metrics
- Grafana for dashboards
- ELK or CloudWatch for logs
- OpenTelemetry and Jaeger for tracing

***

## Data and Communication Design

### MySQL
Used for strongly consistent data such as:
- Users
- Products
- Inventory
- Orders
- Payments

### MongoDB
Used for flexible document-oriented data such as:
- Carts
- Reviews

### Redis
Used for:
- Cart caching
- Session or token support where required
- Frequently accessed product data
- Rate limiting counters

### Kafka
Used for asynchronous event-driven communication.

Example events:
- user.created
- product.updated
- inventory.reserved
- inventory.released
- order.created
- order.cancelled
- payment.completed
- payment.failed
- refund.initiated
- notification.triggered

#### Development Considerations
- Use partition keys carefully, such as orderId or userId.
- Add dead-letter topics for failed consumers.
- Maintain schema contracts for events.
- Consumers must be idempotent because duplicate delivery is possible.

### Elasticsearch
Used for fast and relevant product search.

#### Development Considerations
- Search index must be rebuilt independently of primary DB.
- Index updates should be driven by product events.
- Search result freshness can lag behind source of truth by a short window.

***

## Consistency and Failure Handling

### Transaction Strategy
Distributed transactions across services should be avoided.

Recommended approach:
- Use local DB transactions inside each service.
- Publish integration events using the outbox pattern.
- Use saga-style orchestration or choreography for checkout flow.

### Idempotency
Critical APIs must support idempotency keys:
- Checkout
- Payment initiation
- Payment callback
- Refund initiation

### Retry Strategy
- Retries should use exponential backoff.
- Non-idempotent operations must not be retried blindly.
- External gateway failures should move to retry or reconciliation workflows.

### Dead-Letter Handling
- Failed Kafka events should move to DLQ topics.
- Failed notifications and reconciliation records should be observable and replayable.

***

## Typical Request Flows

### Product Search Flow
1. User sends search request.
2. Request goes through Load Balancer and API Gateway.
3. Gateway routes request to Search Service.
4. Search Service queries Elasticsearch.
5. If search index is stale, product details are validated from Product Service where necessary.
6. Results are returned to the user.

### Add to Cart Flow
1. User adds product to cart.
2. Request goes to Cart Service.
3. Cart Service validates product availability snapshot.
4. Cart data is stored in MongoDB and optionally cached in Redis.
5. Cart may publish analytics or cart activity events to Kafka.

### Checkout Flow
1. User proceeds to checkout.
2. Order Service validates cart, address, pricing, coupon, and product availability.
3. Inventory Service reserves stock.
4. Order Service creates order in pending state.
5. Payment Service initiates payment.
6. Payment result is published to Kafka or received by callback.
7. Order Service confirms or cancels order based on payment result.
8. Inventory is deducted on success or released on failure.
9. Notification Service sends user updates.

***

## State Models

### Order States
- PENDING_PAYMENT
- PAYMENT_SUCCESS
- PAYMENT_FAILED
- CONFIRMED
- PACKED
- SHIPPED
- DELIVERED
- CANCELLED
- REFUND_PENDING
- REFUNDED

### Payment States
- INITIATED
- PENDING
- SUCCESS
- FAILED
- UNKNOWN
- REFUNDED

### Inventory States
- AVAILABLE
- RESERVED
- DEDUCTED
- RELEASED

***

## Security Considerations
- Use TLS for all external and internal service communication where possible.
- Use IAM-based access for cloud resources.
- Encrypt sensitive data at rest and in transit.
- Store secrets only in Vault, not in config files or container images.
- Minimize PCI scope by using external payment tokenization.
- Add audit logging for login, payment, refund, and admin actions.

***

## Development and Release Considerations
- Maintain separate environments for dev, QA, staging, and production.
- Use feature flags for risky rollouts.
- Use canary or blue-green deployments for critical services.
- Backward compatibility should be maintained for APIs and Kafka events.
- Schema migrations must be versioned and rollback-aware.
- Add contract tests for service-to-service and event interfaces.

***

## Testing Strategy
- Unit tests for business logic and validation rules.
- Integration tests for DB, Kafka, Redis, and Elasticsearch behavior.
- Contract tests for APIs and events.
- End-to-end tests for login, search, cart, checkout, and refund workflows.
- Chaos or resilience testing for partial failures such as Kafka outage, payment timeout, and Redis unavailability.

***

##  Engineering Notes
- Product and inventory must be separated to avoid mixing catalog data with stock operations.
- Order and payment flows should be designed around failure recovery, not only happy path.
- Search and notifications should remain eventually consistent and non-blocking.
- All customer-facing critical flows should be observable with metrics, logs, and traces.
- Exactly-once delivery is difficult across distributed systems, so idempotent consumers and reconciliation jobs are safer.
- The checkout flow should be modeled as a controlled business state machine, not as loosely connected API calls.

***

## Future Enhancements
- Recommendation engine for personalized suggestions.
- Fraud detection for suspicious payment activity.
- Admin service for catalog, inventory, and order operations.
- Seller or marketplace model for multi-vendor support.
- Multi-region deployment for disaster recovery and lower latency.
- CDC-based indexing pipeline for stronger search synchronization.