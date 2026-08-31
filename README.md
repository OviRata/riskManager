# Real-Time Risk Management Engine

An event-driven microservice architecture built to process streaming market trades and calculate financial risk exposure in real time.

This project demonstrates enterprise-grade backend patterns including message brokering, concurrency control, and live data streaming.

## 🚀 Tech Stack
* **Backend:** Java 26, Spring Boot 3
* **Message Broker:** Apache Kafka (with Zookeeper)
* **Database:** PostgreSQL
* **Real-Time Communication:** WebSockets, STOMP protocol
* **Infrastructure:** Docker, Docker Compose

## 🧠 Key Enterprise Features
* **Idempotent Processing:** Utilizes a `processed_trades` table to ensure that network retries or duplicate Kafka messages do not result in double-counting a trader's risk.
* **Concurrency Control:** Implements Pessimistic Locking at the database tier to prevent race conditions when multiple consumers process trades for the same asset simultaneously.
* **Dead Letter Queue (DLQ):** Routes malformed or unprocessable messages to a dedicated Kafka DLQ topic to prevent poison pills from crashing the main consumer group.
* **Live Dashboard:** Pushes sub-millisecond risk updates to a frontend web client via a persistent WebSocket connection, eliminating the need for HTTP polling.

## 🛠️ Quick Start

### Prerequisites
* Docker and Docker Compose installed and running.
* Java 26 (if compiling locally, though the Maven wrapper is included).

### Build and Run
1. **Compile the application:**
   ```bash
   ./mvnw clean package -DskipTests